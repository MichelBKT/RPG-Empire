package com.empire.rpg.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.util.ArrayList;

public class QuestPlayer {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private ArrayList<QuestItem> questList;

    private boolean showQuestPlayerFrame = false;
    private boolean showDescription = false;
    private String descriptionText = "";
    private int selectedQuestIndex = -1;

    public QuestPlayer(OrthographicCamera camera, SpriteBatch batch, ArrayList<QuestItem> questList) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.questList = questList;
    }

    // Méthode pour obtenir la liste des quêtes
    public ArrayList<QuestItem> getQuestList() {
        return questList;
    }

    // Méthode utilitaire pour vérifier si une quête existe déjà dans la liste
    private boolean questExists(QuestItem newQuest) {
        for (QuestItem quest : questList) {
            if (quest.getId() == newQuest.getId()) { // Comparaison d'ID avec ==
                return true;
            }
        }
        return false;
    }


    // Méthode pour mettre à jour la liste des quêtes sans doublons
    public void updateQuestList(ArrayList<QuestItem> newQuestList) {
        for (QuestItem newQuest : newQuestList) {
            if (!questExists(newQuest)) {
                questList.add(newQuest); // Ajoute uniquement si la quête n'est pas un doublon
            }
        }
    }

    // Méthode principale de rendu
    public void render() {
        if (showQuestPlayerFrame) {
            drawQuestPlayerFrame();
        }
    }

    private void drawQuestPlayerFrame() {
        float frameWidth = 700;
        float frameHeight = 400;
        float frameX = camera.position.x - frameWidth / 2;
        float frameY = camera.position.y - frameHeight / 2;
        float padding = 15;

        drawBackground(frameX, frameY, frameWidth, frameHeight);
        drawTitle(frameX, frameY, frameWidth, frameHeight, padding);

        handleInput();
        drawQuestList(frameX, frameY, frameWidth, frameHeight, padding);

        if (showDescription && selectedQuestIndex >= 0) {
            updateDescription();  // Mise à jour de la description
            drawDescription(frameX, frameY, frameWidth, frameHeight);
        }

        drawAcceptText(frameX, frameY, frameWidth);
        drawVerticalSeparator(frameX, frameWidth, frameY, frameHeight);
    }

    private void drawBackground(float frameX, float frameY, float frameWidth, float frameHeight) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();
    }

    private void drawTitle(float frameX, float frameY, float frameWidth, float frameHeight, float padding) {
        batch.begin();
        String title = "QUÊTES EN COURS";
        GlyphLayout layout = new GlyphLayout(font, title);
        font.setColor(1, 1, 1, 1);
        float titleX = frameX + (frameWidth - layout.width) / 2;
        float titleY = frameY + frameHeight - padding;
        font.draw(batch, title, titleX, titleY);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float separatorY = titleY - layout.height - 10;
        shapeRenderer.line(frameX, separatorY, frameX + frameWidth, separatorY);
        shapeRenderer.end();
    }

    private void handleInput() {
        // Utilisation des touches Z (haut) et S (bas) pour la navigation
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            // Se déplacer vers le haut, mais seulement parmi les quêtes avec statut > 0
            selectedQuestIndex = getNextQuestIndex(-1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            // Se déplacer vers le bas, mais seulement parmi les quêtes avec statut > 0
            selectedQuestIndex = getNextQuestIndex(1);
        }

        if (selectedQuestIndex >= 0 && Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            showDescription = !showDescription;
            if (showDescription) {
                descriptionText = questList.get(selectedQuestIndex).getDescription();
            }
        }
    }

    // Cette méthode retourne l'index de la prochaine quête active (status > 0)
    private int getNextQuestIndex(int direction) {
        int newIndex = selectedQuestIndex;
        int size = questList.size();
        int currentCount = 0;

        while (currentCount < size) {
            newIndex = (newIndex + direction + size) % size;
            QuestItem quest = questList.get(newIndex);
            if (quest.getStatus() > 0) {
                return newIndex;  // Si la quête est active, on retourne l'index
            }
            currentCount++;
        }
        return selectedQuestIndex;  // Si aucune quête n'est trouvée, on reste sur l'actuelle
    }

    private void drawQuestList(float frameX, float frameY, float frameWidth, float frameHeight, float padding) {
        batch.begin();
        int displayIndex = 0;
        float questStartY = frameY + frameHeight - 50;
        for (int i = 0; i < questList.size(); i++) {
            QuestItem quest = questList.get(i);
            if (quest.getStatus() > 0) {  // Afficher uniquement les quêtes acceptées
                font.setColor(1, 1, 1, 1);
                if (i == selectedQuestIndex) {
                    font.draw(batch, "-> " + quest.getTitle(), frameX + 15, questStartY - (displayIndex * 25));  // Décalage à droite
                } else {
                    font.draw(batch, quest.getTitle(), frameX + 15, questStartY - (displayIndex * 25));  // Décalage à droite
                }
                displayIndex++;
            }
        }
        batch.end();
    }

    private void drawDescription(float frameX, float frameY, float frameWidth, float frameHeight) {
        batch.begin();
        float descriptionX = frameX + frameWidth - 425;
        float descriptionY = frameY + frameHeight - 55;

        // Afficher la description de la quête
        font.draw(batch, "Description : ", descriptionX, descriptionY);

        float maxLineWidth = 410;
        String[] words = descriptionText.split(" ");
        StringBuilder lineBuilder = new StringBuilder();
        float currentY = descriptionY - 20;

        for (String word : words) {
            String testLine = lineBuilder + (lineBuilder.length() > 0 ? " " : "") + word;
            GlyphLayout testLayout = new GlyphLayout(font, testLine);

            if (testLayout.width <= maxLineWidth) {
                lineBuilder.append((lineBuilder.length() > 0 ? " " : "")).append(word);
            } else {
                font.draw(batch, lineBuilder.toString(), descriptionX, currentY);
                currentY -= 20;
                lineBuilder = new StringBuilder(word);
            }
        }
        if (lineBuilder.length() > 0) {
            font.draw(batch, lineBuilder.toString(), descriptionX, currentY);
            currentY -= 35;
        }

        // Afficher les étapes de la quête, si elles existent
        if (questList.get(selectedQuestIndex).getSteps() != null && !questList.get(selectedQuestIndex).getSteps().isEmpty()) {
            font.draw(batch, "Étapes : ", descriptionX, currentY);
            currentY -= 25;

            for (QuestStep step : questList.get(selectedQuestIndex).getSteps()) {
                font.draw(batch, " - " + step.getName(), descriptionX, currentY);
                currentY -= 25;
            }
        }


        // Afficher la récompense de la quête
        QuestItem selectedQuest = questList.get(selectedQuestIndex);
        Rewards reward = selectedQuest.getReward();
        if (reward != null) {
            String rewardText = "Astrales: " + reward.getGold();
            font.draw(batch, rewardText, descriptionX, currentY);
        }

        batch.end();
    }


    private void drawAcceptText(float frameX, float frameY, float frameWidth) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // Dessiner la ligne de séparation avant le texte
        float separatorY = frameY + 50; // La position de la ligne de séparation juste au-dessus du texte
        shapeRenderer.line(frameX, separatorY, frameX + frameWidth, separatorY); // Ligne horizontale
        shapeRenderer.end();

        batch.begin();
        String acceptText = "APPUYEZ SUR \"G\" POUR FERMER";
        GlyphLayout acceptLayout = new GlyphLayout(font, acceptText);
        float acceptTextX = frameX + (frameWidth - acceptLayout.width) / 2;
        float acceptTextY = frameY + 30;
        font.draw(batch, acceptText, acceptTextX, acceptTextY);
        batch.end();
    }


    private void drawVerticalSeparator(float frameX, float frameWidth, float frameY, float frameHeight) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        float verticalLineX = frameX + frameWidth - 440;
        shapeRenderer.line(verticalLineX, frameY + 50, verticalLineX, frameY + frameHeight - 36);
        shapeRenderer.end();
    }

    public void setShowQuestPlayerFrame(boolean show) {
        this.showQuestPlayerFrame = show;
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }

    // Méthode de mise à jour de la description en fonction de la quête sélectionnée
    private void updateDescription() {
        if (selectedQuestIndex >= 0 && selectedQuestIndex < questList.size()) {
            QuestItem selectedQuest = questList.get(selectedQuestIndex);
            if (selectedQuest.getStatus() > 0) { // Affiche la description pour les quêtes avec un statut supérieur à 0
                descriptionText = selectedQuest.getDescription();
            } else {
                descriptionText = ""; // Pas de description si la progression est 0 ou inférieure
            }
        } else {
            descriptionText = ""; // Réinitialiser la description si aucune quête valide n'est sélectionnée
        }
    }

}
