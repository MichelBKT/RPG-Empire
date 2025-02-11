package com.empire.rpg.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import com.empire.rpg.Main;
import com.empire.rpg.utils.FontUtils;

public class MainMenuScreen implements Screen {
    private SpriteBatch batch;
    private int selectedOption = 0;
    private final String[] options = new String[]{"Nouvelle Partie", "Quitter le jeu"};
    private TextField playerNameField = null;
    private static String playerName = "";
    private Stage stage;
    private Skin skin;
    private BitmapFont customFont;
    private BitmapFont customFontTitle;
    private boolean isEnteringName = false; // Indique si le joueur est en train d'entrer son nom

    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Charger une police personnalisée
        try {
            customFont = FontUtils.createCustomFont("SinisterRegular.ttf", 42, Color.WHITE);
            customFont = new BitmapFont();
            customFont.setColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Charger le skin
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Création d'un style personnalisé pour le TextField
        playerNameField.setVisible(false); // Le champ est masqué par défaut
        stage.addActor(playerNameField);

        // Activer le clavier virtuel
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Effacer l'écran
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Initialisation des ressources
        if (batch == null) {
            batch = new SpriteBatch();
        }
        if (customFont == null) {
            customFont = FontUtils.createCustomFont("SinisterRegular.ttf", 48, Color.WHITE);
        }
        if (skin == null) {
            skin = new Skin(Gdx.files.internal("uiskin.json"));
        }
        if (playerNameField == null) {
            TextField.TextFieldStyle customStyle = new TextField.TextFieldStyle();
            customStyle.font = customFont;
            customStyle.fontColor = Color.WHITE;
            customStyle.cursor = skin.getDrawable("cursor");
            customStyle.selection = skin.getDrawable("selection");
            customStyle.background = skin.getDrawable("textfield");

            // Création du champ de texte
            playerNameField = new TextField("", customStyle);
            playerNameField.setMessageText("Entrez votre nom...");
            playerNameField.setAlignment(Align.center);
            playerNameField.setSize(400, 60);
            playerNameField.setPosition(WORLD_WIDTH / 2f + 150, WORLD_HEIGHT / 2f - 70);
            playerNameField.setVisible(false); // Le champ est masqué par défaut
            stage = new Stage(new ScreenViewport());
            stage.addActor(playerNameField);
        }

        // Démarrer le batch pour dessiner le menu
        batch.begin();
        customFont.setColor(Color.WHITE);
        if (customFontTitle == null) {
            customFontTitle = FontUtils.createCustomFont("SinisterRegular.ttf", 72, Color.RED);
        }
        customFontTitle.draw(batch, "MENU PRINCIPAL", WORLD_WIDTH / 2f + 150, WORLD_HEIGHT + 200);

        // Ajouter l'image de fond
        Texture background = new Texture("Background/logo-rpg-empire.png");
        batch.draw(background, 1050, 250, 550, 500);

        // Dessiner les options du menu
        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                customFont.setColor(Color.YELLOW); // Jaune pour l'option sélectionnée
            } else {
                customFont.setColor(Color.WHITE); // Blanc pour les autres
            }
            customFont.draw(batch, options[i], WORLD_WIDTH / 2f + 100, WORLD_HEIGHT + 50 - i * 60);
        }
        batch.end();

        // Gérer la navigation dans le menu
        handleMenuNavigation();

        // Gérer la sélection du menu
        handleMenuSelection();

        // Dessiner la scène (TextField inclus)
        stage.act(delta);
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        customFont.dispose();
        stage.dispose();
    }

    private void handleMenuNavigation() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption = (selectedOption + options.length - 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1) % options.length;
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleMenuSelection();
        }
    }

    private void handleMenuSelection() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedOption) {
                case 0:
                    // Nouvelle Partie
                    playerNameField.setVisible(true);
                    Gdx.input.setInputProcessor(stage); // Activer le clavier virtuel
                    isEnteringName = true;

                    // Garder le nom du joueur dans une variable
                    playerName = playerNameField.getText();
                    // Démarre le jeu si le joueur appuie sur Enter & a entré un nom
                    if (!playerName.isEmpty() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                        // Démarrer le jeu avec le nom du joueur
                        Main.getInstance().setScreen(new GameScreen(playerName));
                    }
                    break;
                case 1:
                    // Quitter le jeu
                    Gdx.app.exit();
                    break;
            }
        }
    }

    // get du nom du joueur static
    public static String getPlayerName() {
        return playerName;
    }
}
