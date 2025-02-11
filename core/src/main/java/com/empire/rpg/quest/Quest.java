package com.empire.rpg.quest;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class Quest {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Texture questBoardTexture;
    private ArrayList<QuestItem> questList;    // Liste des quêtes disponibles
    private static final float SQUARE_SIZE = 48;
    private static final float INTERACTION_DISTANCE = 50;
    private static final float DISPLAY_DISTANCE = 500;
    private static final Vector2 squarePosition = new Vector2(52 * 48, 45 * 49);
    private boolean showInteractionFrame = false; // Contrôle l'affichage du cadre d'interaction
    private int selectedQuestIndex = -1; // Indice de la quête sélectionnée
    private String descriptionText = ""; // Texte de la description de la quête sélectionnée
    private boolean showDescription = false;

    public Quest(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.font = new BitmapFont();
        this.questList = new ArrayList<>();
        this.shapeRenderer = new ShapeRenderer();

        // Charger la texture pour le tableau des quêtes
        this.questBoardTexture = new Texture("exclamation.png");

        // Vérifier si le fichier existe dans le répertoire local, sinon le copier depuis le répertoire interne
        FileHandle localFile = Gdx.files.local("quests.json");
        if (!localFile.exists()) {
            FileHandle internalFile = Gdx.files.internal("quests.json");
            localFile.writeString(internalFile.readString(), false);
        }

        loadQuestsFromFile("quests.json"); // Chargement des quêtes depuis le fichier JSON
    }

    // Chargement des quêtes depuis un fichier JSON
    public void loadQuestsFromFile(String filePath) {
        Json json = new Json();
        FileHandle file = Gdx.files.local(filePath);
        if (file.exists()) {
            QuestItem[] quests = json.fromJson(QuestItem[].class, file);
            for (QuestItem quest : quests) {
                questList.add(quest);
            }
        }
    }

    private void saveQuestsToFile(String filePath) {
        System.out.println("Appel de saveQuestsToFile avec fichier " + filePath);
        Json json = new Json();
        FileHandle file = Gdx.files.local(filePath);
        String jsonData = json.prettyPrint(questList);  // Sérialisation des quêtes dans un format lisible
        file.writeString(jsonData, false);
    }

    private void saveQuestStatus(int questId, int newStatus) {
        System.out.println("Appel de saveQuestStatus pour questId " + questId + " avec statut " + newStatus);
        // Modification directe de l'objet QuestItem dans questList au lieu de manipuler directement le JSON
        for (QuestItem quest : questList) {
            if (quest.getId() == questId) {
                quest.setStatus(newStatus);
                break;
            }
        }
        // Sauvegarder après la modification
        saveQuestsToFile("quests.json");
    }

    // Gérer les entrées utilisateur pour la sélection de quêtes et l'acceptation
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            QuestItem nextQuest = getNextQuestByIndex(selectedQuestIndex, +1);
            if (nextQuest != null) {
                selectedQuestIndex = questList.indexOf(nextQuest);
                updateDescription();
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            QuestItem prevQuest = getNextQuestByIndex(selectedQuestIndex, -1);
            if (prevQuest != null) {
                selectedQuestIndex = questList.indexOf(prevQuest);
                updateDescription();
            }
        }

        // Lorsque l'utilisateur appuie sur ENTER pour accepter une quête
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && selectedQuestIndex >= 0) {
            QuestItem selectedQuest = questList.get(selectedQuestIndex);

            // Vérifier que le statut de la quête n'est pas déjà en cours
            if (selectedQuest.getStatus() != 1) {
                selectedQuest.setStatus(1);
                saveQuestStatus(selectedQuest.getId(), 1); // Modifier l'état seulement si nécessaire
                saveQuestsToFile("quests.json");
            }

            // Reset l'index de la quête sélectionnée et d'autres variables d'état
            selectedQuestIndex = -1;
            descriptionText = "";
        }
    }

    // Méthode auxiliaire pour obtenir la prochaine quête en fonction de l'ID, en ignorant celles dont getProgress != 0
    private QuestItem getNextQuestByIndex(int currentIndex, int step) {
        int size = questList.size();

        // Si la liste est vide, retourner null immédiatement pour éviter la division par zéro
        if (size == 0) {
            return null;  // Ou gérer autrement si aucune quête n'est disponible
        }

        int nextIndex = currentIndex;

        do {
            // Calculer l'index suivant en boucle
            nextIndex = (nextIndex + step + size) % size;

            QuestItem quest = questList.get(nextIndex);

            // Si la quête a getProgress == 0, elle est valide pour la sélection
            if (quest.getStatus() == 0 && quest.isUnique() == false) {
                return quest;
            }

        } while (nextIndex != currentIndex); // Boucle tant qu'on n'est pas revenu au point de départ

        return null; // Aucune quête valide trouvée
    }

    // Mettre à jour la description de la quête sélectionnée
    private void updateDescription() {
        if (selectedQuestIndex >= 0 && selectedQuestIndex < questList.size()) {
            QuestItem selectedQuest = questList.get(selectedQuestIndex);
            if (selectedQuest.getStatus() == 0) {
                descriptionText = selectedQuest.getDescription();
            } else {
                descriptionText = ""; // Pas de description si la progression n'est pas 0
            }
        } else {
            descriptionText = ""; // Réinitialiser la description si aucune quête valide n'est sélectionnée
        }
    }

    // Méthode pour afficher les éléments visuels
    public void render(SpriteBatch batch, Vector2 playerPosition) {
        handleInput();  // Garder cette ligne pour traiter les entrées clavier

        // Affichage du tableau de quêtes si le joueur est à une distance définie
        if (isPlayerNearSquare(playerPosition)) {
            batch.begin();
            batch.draw(questBoardTexture, squarePosition.x + 19, squarePosition.y + SQUARE_SIZE);
            batch.end();
        }

        // Affichage de la fenêtre d'interaction si nécessaire
        if (showInteractionFrame) {
            drawInteractionFrame(playerPosition);
        }
    }

    private void drawInteractionFrame(Vector2 playerPosition) {
        float frameWidth = 700;
        float frameHeight = 400;
        float frameX = playerPosition.x - frameWidth / 2;
        float frameY = playerPosition.y - frameHeight / 2;
        float padding = 15;
        float descriptionPadding = 50;

        // Cadre d'interaction
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        // Dessiner la barre de séparation sous le texte "TABLEAU DE QUÊTES"
        batch.begin();
        String messageTop = "TABLEAU DE QUÊTES";
        if (!messageTop.isEmpty()) {
            GlyphLayout layout = new GlyphLayout(font, messageTop);
            font.setColor(1, 1, 1, 1);
            float textTopX = frameX + (frameWidth - layout.width) / 2;
            float textTopY = frameY + frameHeight - padding;
            font.draw(batch, messageTop, textTopX, textTopY);
            batch.end();
            // Dessiner la ligne de séparation
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            float separatorY = textTopY - layout.height - 10;
            shapeRenderer.line(frameX + padding, separatorY, frameX + frameWidth - padding, separatorY);
            shapeRenderer.end();
        }
        batch.begin();

        // Dessiner la liste de quêtes
        int displayIndex = 0;
        float questStartY = frameY + frameHeight - (40 + padding + 20);
        for (int i = 0; i < questList.size(); i++) {
            QuestItem quest = questList.get(i);
            if (quest.getStatus() == 0 && quest.isUnique() == false) {  // Afficher uniquement les quêtes acceptés et non uniques
                font.setColor(1, 1, 1, 1);
                if (i == selectedQuestIndex) {
                    font.draw(batch, "-> " + quest.getTitle(), frameX + 10, questStartY - (displayIndex * 30));
                } else {
                    font.draw(batch, quest.getTitle(), frameX + 10, questStartY - (displayIndex * 30));
                }
                displayIndex++;
            }
        }
        batch.end();  // Fin du bloc de SpriteBatch

        // Vérifier l'appui sur "D" pour afficher la description si une quête est sélectionnée
        if (selectedQuestIndex >= 0 && Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            showDescription = !showDescription;  // Bascule de l'état d'affichage de la description
            if (showDescription) {
                descriptionText = questList.get(selectedQuestIndex).getDescription();
            }
        }

        // Afficher la description de la quête sélectionnée sur la droite si showDescription est activé
        if (showDescription && !descriptionText.isEmpty()) {
            batch.begin();
            font.setColor(1, 1, 1, 1);
            float descriptionX = frameX + frameWidth - 340;
            float descriptionY = frameY + frameHeight - descriptionPadding;
            font.draw(batch, "Description : ", descriptionX, descriptionY);

            // Diviser la description sans couper les mots
            float maxLineWidth = 280; // Largeur max des lignes pour la description
            String[] words = descriptionText.split(" ");
            StringBuilder lineBuilder = new StringBuilder();
            float currentY = descriptionY - 20;

            for (String word : words) {
                String testLine = lineBuilder + (lineBuilder.length() > 0 ? " " : "") + word;
                GlyphLayout testLayout = new GlyphLayout(font, testLine);

                // Vérifier si le mot peut être ajouté à la ligne actuelle
                if (testLayout.width <= maxLineWidth) {
                    lineBuilder.append((lineBuilder.length() > 0 ? " " : "")).append(word);
                } else {
                    // Afficher la ligne et passer à la suivante
                    font.draw(batch, lineBuilder.toString(), descriptionX, currentY);
                    currentY -= 20; // Ajuster l'espacement entre les lignes
                    lineBuilder = new StringBuilder(word);
                }
            }
            // Dessiner la dernière ligne restante
            if (lineBuilder.length() > 0) {
                font.draw(batch, lineBuilder.toString(), descriptionX, currentY);
                currentY -= 35;
            }

            // Afficher l'auteur sous la description
            QuestItem selectedQuest = questList.get(selectedQuestIndex);
            if (selectedQuest != null) {
                font.draw(batch, "Auteur : " + selectedQuest.getAuthor(), descriptionX, currentY);
                currentY -= 35; // Espacement pour les récompenses
            }

            // Afficher les récompenses
            if (selectedQuest.getReward() != null) {
                Rewards reward = selectedQuest.getReward();
                String rewardText = "Récompenses : Astrales: " + reward.getGold();
                font.draw(batch, rewardText, descriptionX, currentY);
            }
            batch.end();
        }

        // Dessiner la ligne verticale de séparation entre les quêtes et la description
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float verticalLineX = frameX + frameWidth - 350;  // Position de la ligne verticale entre les deux sections
        shapeRenderer.line(verticalLineX, frameY + 50, verticalLineX, frameY + frameHeight - 36);
        shapeRenderer.end();


        float fixedSeparatorY = frameY + frameHeight - 350;
        // Dessiner une ligne de séparation avant le texte "Appuyez sur ENTER pour accepter"
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float separatorBeforeAcceptY = questStartY - (displayIndex * 30 + 90) - 5;  // Position juste avant "Appuyez sur ENTER"
        shapeRenderer.line(frameX + padding, fixedSeparatorY, frameX + frameWidth - padding, fixedSeparatorY);
        shapeRenderer.end();

        // Afficher un bouton "Appuyez sur ENTER pour accepter" centré
        batch.begin();  // Démarrer un autre bloc de SpriteBatch
        float fixedAcceptTextY = fixedSeparatorY - 20;  // Fixer la position Y sous la ligne de séparation
        if (selectedQuestIndex >= 0 && selectedQuestIndex < questList.size()) {
            String acceptText = "APPUYEZ SUR ENTRÉE POUR ACCEPTER";
            GlyphLayout acceptLayout = new GlyphLayout(font, acceptText);
            float acceptTextX = frameX + (frameWidth - acceptLayout.width) / 2;  // Centrer le texte
            font.draw(batch, acceptText, acceptTextX, fixedAcceptTextY);  // Positionner juste sous la ligne de séparation
        }
        batch.end();  // Fin du bloc de SpriteBatch
    }

    // Méthode pour gérer la proximité du joueur avec le tableau de quêtes
    private boolean isPlayerNearSquare(Vector2 playerPosition) {
        return playerPosition.dst(squarePosition) < DISPLAY_DISTANCE; // Affichage du tableau de quêtes à 60 unités
    }

    // Méthode pour vérifier si le joueur peut interagir avec le tableau de quêtes
    private boolean isPlayerWithinInteractionDistance(Vector2 playerPosition) {
        return playerPosition.dst(squarePosition) < INTERACTION_DISTANCE; // Interaction possible à 50 unités
    }

    // Méthode pour activer/désactiver le cadre d'interaction
    public void setInteractionFrame(boolean show) {
        this.showInteractionFrame = show;
    }

    // Activer/désactiver l'affichage de l'interface d'interaction
    public void setShowInteractionFrame(boolean show) {
        this.showInteractionFrame = show;
    }

    public ArrayList<QuestItem> getQuestList() {
        return this.questList;
    }

    public void dispose() {
        font.dispose();
        questBoardTexture.dispose();
    }
}
