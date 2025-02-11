package com.empire.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.empire.rpg.quest.QuestPlayer;

import com.badlogic.gdx.utils.Json;
import com.empire.rpg.quest.QuestItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;

public class DialogueManager {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private boolean showDialogueFrame = false;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;  // Déclare une BitmapFont
    private Map<String, List<String>> dialogues;  // Stocke les dialogues pour chaque PNJ
    private String currentPNJ;  // Stocke le nom du PNJ actuel
    private int dialogueIndex = 0; // Index actuel du dialogue affiché
    private String currentSpeaker = ""; // Nom du PNJ qui parle actuellement
    private GlyphLayout glyphLayout = new GlyphLayout();  // Initialiser un GlyphLayout
    private ArrayList<QuestItem> questList;    // Liste des quêtes disponibles
    // Définir les dimensions du cadre
    private static final float DIALOGUE_WIDTH = 450f;
    private static final float DIALOGUE_HEIGHT = 125f;
    private int selectedQuestIndex = -1;
    private QuestPlayer questPlayer;  // Instance de QuestPlayer
    private ArrayList<QuestItem> currentQuestList;  // Liste des quêtes actuelles

    public DialogueManager(OrthographicCamera camera, SpriteBatch batch, QuestPlayer questPlayer) {
        if (questPlayer == null) {
            throw new IllegalArgumentException("QuestPlayer cannot be null");
        }
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.questPlayer = questPlayer;  // Assignation de questPlayer passé en argument
        this.questList = questPlayer.getQuestList();  // Initialisation avec la liste des quêtes du joueur
        this.currentQuestList = new ArrayList<>(questList);  // Initialisation de currentQuestList
        font = new BitmapFont();  // Initialiser la BitmapFont
        loadDialogues("dialogues.json");

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
            questList.clear(); // Vider la liste avant d'ajouter de nouvelles quêtes
            QuestItem[] quests = json.fromJson(QuestItem[].class, file);
            for (QuestItem quest : quests) {
                questList.add(quest);
            }
        }
    }


//    // Exemple d'utilisation de la mise à jour de la liste des quêtes
//    public void addNewQuest(QuestItem newQuest) {
//        // Ajouter un nouveau QuestItem à la liste des quêtes
//        currentQuestList.add(newQuest);
//    }

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


    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            saveQuestStatus(1, 1);  // Par exemple, changer le statut de la quête avec l'ID 1
            saveQuestsToFile("quests.json");
            closeDialogue();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
            closeDialogue();
        }
    }



    public ArrayList<QuestItem> getQuestList() {
        return this.questList;
    }

    // Charger les dialogues depuis un fichier JSON
    private void loadDialogues(String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            dialogues = objectMapper.readValue(new File(filename), new TypeReference<Map<String, List<String>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des dialogues !");
        }
    }

    // Affiche le cadre du dialogue
    private void drawDialogueFrame(Vector2 playerPosition) {
        float frameX = playerPosition.x - DIALOGUE_WIDTH / 2;
        float frameY = playerPosition.y - 200;

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1); // Couleur noire pour le fond
        shapeRenderer.rect(frameX, frameY, DIALOGUE_WIDTH, DIALOGUE_HEIGHT);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1); // Couleur blanche pour les bordures
        shapeRenderer.rect(frameX, frameY, DIALOGUE_WIDTH, DIALOGUE_HEIGHT);
        shapeRenderer.end();
    }

    // Démarrer le dialogue avec un PNJ
    public void startDialogue(String speaker) {
        this.currentSpeaker = speaker;
        this.dialogueIndex = 0;  // Réinitialiser l'index
        this.showDialogueFrame = true;
    }

    // Avancer au dialogue suivant ou fermer le cadre si fini
    public void nextDialogue() {
        if (currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            if (dialogueIndex < dialogues.get(currentSpeaker).size() - 1) {
                dialogueIndex++;
            } else {
                this.showDialogueFrame = false; // Ferme le dialogue lorsque tous les dialogues sont montrés
                this.currentSpeaker = null;
            }
        }
    }

    // Découpe le texte pour qu'il s'adapte à la largeur du cadre
    private List<String> wrapText(String text, float maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        String[] words = text.split(" ");  // Diviser le texte en mots

        for (String word : words) {
            // Tester si ajouter ce mot dépasse la largeur du cadre
            String testLine = currentLine.toString() + (currentLine.length() > 0 ? " " : "") + word;
            glyphLayout.setText(font, testLine);

            // Si la ligne dépasse la largeur maximale, ajouter la ligne actuelle à la liste et commencer une nouvelle ligne
            if (glyphLayout.width > maxWidth) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());  // Ajouter la ligne complète
                    currentLine = new StringBuilder(word);  // Commencer une nouvelle ligne avec le mot actuel
                }
            } else {
                // Si la ligne ne dépasse pas la largeur, ajouter le mot à la ligne
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }

        // Ajouter la dernière ligne si elle existe
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    // Dessiner le texte du dialogue
    private void drawDialogueText(float frameX, float frameY) {
        if (currentSpeaker != null && dialogues.containsKey(currentSpeaker)) {
            List<String> pnjDialogues = dialogues.get(currentSpeaker);
            String currentDialogue = pnjDialogues.get(dialogueIndex);

            // Découper le texte en lignes qui s'adaptent à la largeur du cadre
            List<String> wrappedLines = wrapText(currentDialogue, DIALOGUE_WIDTH - 20); // 20px de marge

            // Dessiner le nom du PNJ en haut à gauche
            batch.begin();
            font.setColor(1, 1, 1, 1); // Couleur blanche pour le texte
            font.draw(batch, currentSpeaker + " :", frameX + 10, frameY + DIALOGUE_HEIGHT - 10); // Affiche le nom du PNJ
            batch.end();

            // Dessiner chaque ligne de dialogue dans l'ordre
            float yOffset = frameY + DIALOGUE_HEIGHT - 40;  // Décalage initial du texte (ajuster la position sous le nom du PNJ)
            batch.begin();
            for (String line : wrappedLines) {
                font.draw(batch, line, frameX + 10, yOffset);  // Dessiner chaque ligne
                yOffset -= 20;  // Espacer les lignes verticalement
            }
            batch.end();
        }
    }

    // Méthode de rendu pour afficher le dialogue
    public void render(SpriteBatch batch, Vector2 playerPosition) {
        if (showDialogueFrame) {
            drawDialogueFrame(playerPosition);  // Dessiner le cadre de dialogue
            drawDialogueText(playerPosition.x - DIALOGUE_WIDTH / 2, playerPosition.y - 200);  // Dessiner le texte du dialogue
            handleInput();  // Gérer les entrées utilisateur
        }
    }

    // Fermer le dialogue
    public void closeDialogue() {
        this.showDialogueFrame = false;
        this.currentSpeaker = null;
    }

    // Vérifie si le cadre de dialogue doit être affiché
    public boolean isShowDialogueFrame() {
        return showDialogueFrame;
    }

    // Permet de définir si le cadre de dialogue doit être affiché
    public void setShowDialogueFrame(boolean showDialogueFrame) {
        this.showDialogueFrame = showDialogueFrame;
    }
}
