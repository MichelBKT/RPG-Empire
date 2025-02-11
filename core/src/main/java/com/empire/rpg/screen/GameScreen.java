package com.empire.rpg.screen;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Input;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.map.MapManager;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.WeaponComponent;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.component.Component;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.screen.IntroScreen;
import com.empire.rpg.screen.MainMenuScreen;
import com.empire.rpg.screen.Screen;
import com.empire.rpg.ui.PlayerUI;

public class GameScreen extends ApplicationAdapter implements Screen{
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private PlayerCharacter player;
    private SoundManager soundManager;
    private DebugRenderer debugRenderer;
    private PlayerUI playerUI;
    private Screen currentScreen;
    private String playerName;
    private boolean animationFinished = false;
    private boolean debugMode = false;
    private PauseScreen pauseScreen;


    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;

    public GameScreen(String playerName) {
        // Charger l'écran d'introduction avec un Runnable pour afficher le menu principal
        IntroScreen introScreen = new IntroScreen(() -> setScreen(new MainMenuScreen()));
        setScreen(introScreen);
        this.playerName = playerName;
    }


    @Override
    public void create() {

        // Charger les ressources
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        // Création d'une map de composants avec PositionComponent et HealthComponent
        Map<Class<? extends Component>, Component> components = Map.of(
            HealthComponent.class, new HealthComponent(90, 100),
            PositionComponent.class, new PositionComponent(4800f, 4800f)
        );

        // Création et initialisation de l'instance de PlayerCharacter
        player = new PlayerCharacter(2.0f, UUID.randomUUID(), "Hero", components);

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

//        // Initialiser l'UI du joueur
//        playerUI = new PlayerUI(player);

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Initialiser le menu pause
        pauseScreen = new PauseScreen();

    }

    private void setScreen(Screen screen) {
        currentScreen = screen;
        if (!(currentScreen instanceof IntroScreen)) {
            return;
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Rendre les couches inférieures (en-dessous du joueur)
        mapManager.renderLowerLayers(camera);

        // Mettre à jour le joueur
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime, collisionManager);

        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapManager.renderUpperLayers(camera);

        // Activer/Désactiver le mode de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (debugMode) {
            debugRenderer.renderDebugBounds(camera, player, collisionManager);
        }

        // Rendre l'UI du joueur (après le rendu des éléments du jeu)
        playerUI.render(batch);

        // Mettre à jour la caméra pour suivre le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Mettre à jour la screen actuelle
        if (currentScreen != null) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (currentScreen instanceof PauseScreen) {
                ((PauseScreen) currentScreen).toggleVisibility();
            } else {
                setScreen(new PauseScreen());
            }
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (pauseScreen.isVisible()) {
            pauseScreen.render(null); // Passer `null` pour simplifier.
            return; // Interrompt le rendu du reste.
        }

        // Rendu normal du jeu.
        mapManager.renderLowerLayers(camera);
        deltaTime = Gdx.graphics.getDeltaTime();
        player.update(deltaTime, collisionManager);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        player.render(batch);
        batch.end();
        mapManager.renderUpperLayers(camera);
        playerUI.render(batch);
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize ( int width, int height){
        viewport.update(width, height);
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    @Override
    public void dispose () {
        player.dispose();
        if (soundManager != null) {
            soundManager.dispose();
        }
        debugRenderer.dispose();
        playerUI.dispose();

        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}


