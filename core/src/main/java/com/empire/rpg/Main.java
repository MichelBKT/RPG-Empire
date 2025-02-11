package com.empire.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.component.*;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import com.empire.rpg.map.*;
import com.empire.rpg.entity.PNJ;
import com.empire.rpg.quest.Quest;
import com.empire.rpg.quest.QuestPlayer;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.audio.SoundManager;
import com.empire.rpg.debug.DebugRenderer;
import com.empire.rpg.entity.mob.MobFactory;
import com.empire.rpg.component.pathfinding.Pathfinding;
import com.empire.rpg.CollisionHandler;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.ui.PlayerUI;
import com.empire.rpg.ui.MobUI;
import com.empire.rpg.ui.ZoneUI;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.empire.rpg.screen.*;
import com.empire.rpg.entity.player.Inventory.Inventory;
import com.empire.rpg.shop.Shop;
import com.empire.rpg.shop.Vente;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private SpriteBatch batch2;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private MapManager mapManager;
    private CollisionManager collisionManager;
    private CollisionHandler collisionHandler;
    private PlayerCharacter player;
    private PositionComponent positionComponent;
    private SoundManager soundManager;
    private DebugRenderer debugRenderer;
    private PlayerUI playerUI;
    private Texture F_Key_Texture;
    private Texture questBoardTexture;
    private boolean debugMode = false;
    private static final float WORLD_WIDTH = 854f;
    private static final float WORLD_HEIGHT = 480f;
    private static final Vector2 squarePosition = new Vector2(52 * 48, 45 * 49);  // Position du tableau de quête dans le monde
    private boolean showInteractionFrame = false;  // Booléen pour savoir si le cadre d'interaction est affiché
    private boolean showQuestPlayer = false;
    private boolean showDialogueFrame = false;
    private Inventory inventaire; // Instance de l'inventaire
    private ShapeRenderer shapeRenderer; // Déclaration de ShapeRenderer
    private BitmapFont font;
    private Quest quest;  // Objet Quest qui gère les quêtes dans le jeu
    private QuestPlayer questPlayer;
    private DialogueManager dialogue; // Objet qui gère les dialogues avec les PNJ
    private PNJ pnj_duc, pnj_shop, pnj_damien, pnj_morgane, pnj_fostine, pnj_pierre, pnj_marie, pnj_jean, pnj_luc, pnj_paul, pnj_claude, pnj_gaspard, pnj_grog, pnj_ethan, pnj_gork;
    private static final float INTERACTION_DISTANCE = 70;  // Distance d'interaction avec un objet
    private static final float DISPLAY_DISTANCE = 500;
    private static final float SQUARE_SIZE = 64;
    private static final float INTERACTION_DISTANCE_PNJ = 70;
    private Pathfinding pathfinding;
    private MobUI mobUI;
    private ZoneUI zoneUI;
    private OrthographicCamera uiCamera;
    private Viewport uiViewport;
    private ZoneManager zoneManager;
    private MobManager mobManager;
    private Screen currentScreen;
    private boolean animationFinished = false;
    private PauseScreen pauseScreen;
    private static Main instance;

    // Ajoutez cette liste à votre classe Main
    private List<PNJ> pnjList = new ArrayList<>();

    // Nom du joueur
    private String playerName;

    // Dimensions virtuelles de l'UI
    private static final float UI_WIDTH = 1280f;
    private static final float UI_HEIGHT = 720f;

    //PPT
    private InteractionImageManager interactionImageManager;

    //Shop
    private Shop shop; // Instance du shop
    private boolean showShopFrame = false;

    //Shop
    private Vente vente; // Instance du shop
    private boolean showVenteFrame = false;

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public void setScreen(Screen screen) {
        currentScreen = screen;
        if (!(currentScreen instanceof IntroScreen)) {
            return;
        }
    }

    @Override
    public void create() {
        // Charger l'écran d'introduction avec un Runnable pour afficher le menu principal
        IntroScreen introScreen = new IntroScreen(() -> setScreen(new MainMenuScreen()));
        setScreen(introScreen);

        // Récupérer le nom du joueur depuis le menu principal
        playerName = MainMenuScreen.getPlayerName();

        instance = this;
        // Gdx.input.setCursorCatched(true);
        batch = new SpriteBatch();
        batch2 = new SpriteBatch();
        camera = new OrthographicCamera();
        pauseScreen = new PauseScreen();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer(); // Initialisation de ShapeRenderer
        font = new BitmapFont();

        // Charger la carte et les collisions
        mapManager = new MapManager("rpg-map.tmx", camera);
        collisionManager = new CollisionManager(mapManager.getTiledMap());

        // Initialiser collisionHandler
        collisionHandler = new CollisionHandler(collisionManager);

        // Initialisation des PNJ
        initializePNJ("Duc_Michel", 177 * 48 + 24, 67 * 48 + 24, "PNJ/michel.png");
        initializePNJ("Jean-Benoit", 1265f, 1831f, "PNJ/jean-benoit.png");
        initializePNJ("Damien", 750f, 2550f, "PNJ/damien.png");
        initializePNJ("Morgane", 1200f, 3145f, "PNJ/morgane.png");
        initializePNJ("Fostine", 900f, 580f, "PNJ/fostine.png");
        initializePNJ("Pierre", 5300f, 4600f, "PNJ/PNJ01.png");
        initializePNJ("Marie", 5700f, 4250f, "PNJ/PNJ06.png");
        initializePNJ("Jean", 8250f, 2800f, "PNJ/PNJ15.png");
        initializePNJ("Luc", 8400f, 2350f, "PNJ/PNJ10.png");
        initializePNJ("Paul", 7950f, 2600f, "PNJ/PNJ04.png");
        initializePNJ("Claude", 5350f, 2400f, "PNJ/PNJ02.png");
        initializePNJ("Gaspard", 6350f, 3000f, "PNJ/PNJ13.png");

        // Initialisation des objets liés aux quêtes
        quest = new Quest(camera, batch);  // Création de l'objet Quest pour gérer les quêtes
        questPlayer = new QuestPlayer(camera, batch, quest.getQuestList());  // Création de l'objet QuestPlayer pour gérer l'affichage des quêtes
        dialogue = new DialogueManager(camera, batch, questPlayer); // Initialisation du gestionnaire de dialogues
        // Chargement de la texture de l'icône du tableau de quête
        questBoardTexture = new Texture(Gdx.files.internal("exclamation.png"));  // Chemin vers l'image du tableau de quêtes
        F_Key_Texture = new Texture(Gdx.files.internal("Images/F_Key.png"));  // Chemin vers l'image de la touche F

        Map<Class<? extends Component>, Component> components = new HashMap<>();
        components.put(HealthComponent.class, new HealthComponent(90, 100));
        //components.put(PositionComponent.class, new PositionComponent(180 * 48 + 24, 55 * 48 + 24)); // Spawn au port de la ville
        //components.put(PositionComponent.class, new PositionComponent(51 * 48 + 24, 42 * 48 + 24)); // Spawn au tableau des guêtes
        components.put(PositionComponent.class, new PositionComponent(8300, 2530));

        this.player = new PlayerCharacter(2.0f, UUID.randomUUID(), playerName, components);

        inventaire = new Inventory(camera, batch);  // Initialisation de l'inventaire

        shop = new Shop(camera, batch);  // Initialisation du shop

        vente = new Vente(camera, batch);  // Initialisation de vente

        // Initialiser l'InteractionImageManager
        interactionImageManager = new InteractionImageManager(
            8300, 2530, 48, 48, // Coordonnées et taille du carré
            // Chemins des images
            new String[]{
                "PPT/1.png", "PPT/2.png", "PPT/3.png", "PPT/4.png", "PPT/5.png", "PPT/6.png"
            }
        );

        // Initialiser la caméra et le Viewport de l'UI
        uiCamera = new OrthographicCamera();
        uiViewport = new FitViewport(UI_WIDTH, UI_HEIGHT, uiCamera);

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Initialiser le pathfinding
        pathfinding = new Pathfinding(collisionManager);
        // Définit le pathfinding global pour tous les mobs
        MobFactory.setPathfinding(pathfinding);

        // Initialiser le MobManager
        mobManager = new MobManager(collisionManager);
        // Initialiser le MobUI
        mobUI = new MobUI();

        // Initialiser le ZoneManager et charger les zones
        zoneManager = new ZoneManager();
        zoneManager.loadZonesFromMap(mapManager.getTiledMap());

        // Initialiser le débogueur
        debugRenderer = new DebugRenderer();

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        // Appeler initializeGame() pour initialiser le joueur et l'UI
        initializeGame();

        // Mettre à jour la caméra sur le joueur
        if (player != null) {
            camera.position.set(player.getX(), player.getY(), 0);
            camera.update();
        } else {
            System.err.println("Erreur : 'player' n'a pas été initialisé correctement dans initializeGame().");
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gestion de la touche Echap
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseScreen.toggleVisibility(); // Basculer la visibilité du menu Pause
        }

        // Si le menu Pause est visible, ne pas rendre le reste du jeu
        if (pauseScreen.isVisible()) {
            pauseScreen.render(null); // Passer `null` ou une instance de `Graphics` si nécessaire
            return;
        }

        // Rendre les couches inférieures (en-dessous du joueur)
        mapManager.renderLowerLayers(camera);

        if (canMove()) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            player.update(deltaTime, collisionManager);
        }

        // Vérifier si le joueur est mort
        if (player.isDead()) {
            resetGame();
            return; // Arrêter le rendu pour cette frame
        }

        // Mettre à jour les mobs
        for (Mob mob : Mob.allMobs) {
            mob.update(Gdx.graphics.getDeltaTime(), player, camera);
        }
        collisionHandler.handleCollisions(player, Mob.allMobs);
        // Démarrer le batch pour dessiner le joueur
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        for (Mob mob : Mob.allMobs) {
            // Appliquer le facteur de zoom lors du rendu
            batch.draw(
                mob.getCurrentTexture(),
                mob.getPosition().x + mob.getOffsetX(),
                mob.getPosition().y + mob.getOffsetY(),
                mob.getCurrentTexture().getRegionWidth() * mob.getScale(),
                mob.getCurrentTexture().getRegionHeight() * mob.getScale()
            );
        }

        pnj_duc.render(batch);
        pnj_shop.render(batch);
        pnj_damien.render(batch);
        pnj_morgane.render(batch);
        pnj_fostine.render(batch);
        pnj_pierre.render(batch);
        pnj_marie.render(batch);
        pnj_jean.render(batch);
        pnj_luc.render(batch);
        pnj_paul.render(batch);
        pnj_claude.render(batch);
        pnj_gaspard.render(batch);
        player.render(batch);

        for (Mob mob : Mob.allMobs) {
            // Rendu de la barre de vie du mob
            mobUI.render(batch, mob);
        }

        batch.end();

        // Rendre les couches supérieures (au-dessus du joueur)
        mapManager.renderUpperLayers(camera);

        // Mettre à jour la ZoneUI
        zoneUI.update();

        if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), pnj_shop.getPosition(), INTERACTION_DISTANCE)) {
            batch.begin();
            // Dessinez la texture de la touche "F"
            batch.draw(F_Key_Texture, pnj_shop.getPosition().x + 52, pnj_shop.getPosition().y + 78, 24, 24);
            batch.end();
        }

        // Dans la méthode create(), après avoir initialisé tous les PNJ, ajoutez-les à la liste
        pnjList.add(pnj_duc);
        pnjList.add(pnj_damien);
        pnjList.add(pnj_morgane);
        pnjList.add(pnj_fostine);
        pnjList.add(pnj_pierre);
        pnjList.add(pnj_marie);
        pnjList.add(pnj_jean);
        pnjList.add(pnj_luc);
        pnjList.add(pnj_paul);
        pnjList.add(pnj_claude);
        pnjList.add(pnj_gaspard);

        // Dans la méthode render(), remplacez le code spécifique par une boucle for
        for (PNJ pnj : pnjList) {
            if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), pnj.getPosition(), INTERACTION_DISTANCE)) {
                batch.begin();
                // Dessinez la texture de la touche "F"
                batch.draw(F_Key_Texture, pnj.getPosition().x + 52, pnj.getPosition().y + 78, 24, 24);
                batch.end();
                // Détecter l'appui sur la touche "F"
                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    dialogue.startDialogue(pnj.getName());
                }
            }
        }

        // Rendre l'UI du joueur et de la zone
        batch2.setProjectionMatrix(uiCamera.combined);
        batch2.begin();
        playerUI.render(batch2);
        zoneUI.render(batch2);
        // Mise à jour et rendu de l'interaction avec les images
        interactionImageManager.update(new Vector2(player.getX(), player.getY()));
        interactionImageManager.render(batch2);
        batch2.end();

        dialogue.render(batch, player.getPlayerPosition());

        // Activer/Désactiver le mode de débogage
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }
        if (debugMode) {
            debugRenderer.renderDebugBounds(camera, player, collisionManager);
        }

        // Mettre à jour l'inventaire pour gérer les entrées
        if (!showInteractionFrame) {
            inventaire.update();  // Appel de update() pour gérer la navigation dans l'inventaire
            inventaire.render(new Vector2(player.getX(),player.getY()));
        }

        if (!showShopFrame) {
            shop.update();  // Appel de update() pour gérer la navigation dans le shop
            shop.render(new Vector2(player.getX(), player.getY()));
        }

        if (!showShopFrame) {
            vente.update();  // Appel de update() pour gérer la navigation dans le vente
            vente.render(new Vector2(player.getX(), player.getY()));
        }

        // Affichage de l'icône de quête si le joueur est proche du tableau
        if (isPlayerNearSquare(player.getPlayerPosition(), squarePosition, DISPLAY_DISTANCE)) {
            batch.begin();
            batch.draw(questBoardTexture, squarePosition.x + 19, squarePosition.y + SQUARE_SIZE);  // Affiche l'icône du tableau de quête
            batch.end();
        }

        // Afficher le cadre de dialogue si activé
        if (dialogue.isShowDialogueFrame()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                dialogue.nextDialogue(); // Avance au dialogue suivant
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                dialogue.closeDialogue(); // Ferme le cadre
            }
            dialogue.render(batch, player.getPlayerPosition());
        }

        // Cacher le cadre d'interaction avec la touche ESCAPE
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showDialogueFrame = false;  // Désactive le cadre d'interaction
            dialogue.setShowDialogueFrame(false);  // Désactive le cadre d'interaction dans Quest
        }

        // Bascule d'affichage du cadre d'interaction avec la touche F
        if (isPlayerWithinInteractionDistance(player.getPlayerPosition(), squarePosition, INTERACTION_DISTANCE) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            showInteractionFrame = !showInteractionFrame;  // Inverse l'état d'affichage du cadre d'interaction
            quest.setShowInteractionFrame(showInteractionFrame);  // Applique l'état à l'objet Quest
        }

        // Afficher/masquer le cadre de quête avec la touche G
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            showQuestPlayer = !showQuestPlayer;  // Inverse l'état d'affichage du cadre de quête
            questPlayer.setShowQuestPlayerFrame(showQuestPlayer);  // Applique l'état à l'objet QuestPlayer
        }

        // Cacher le cadre d'interaction avec la touche ESCAPE
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showInteractionFrame = false;  // Désactive le cadre d'interaction
            quest.setShowInteractionFrame(false);  // Désactive le cadre d'interaction dans Quest
        }

        // Si le cadre de quête doit être affiché, appelle le rendu de QuestPlayer
        if (showQuestPlayer) {
            questPlayer.render();  // Affiche le cadre de quête
        }

        // Si le cadre d'interaction doit être affiché, appelle le rendu de Quest
        if (showInteractionFrame) {
            quest.render(batch, player.getPlayerPosition());  // Affiche l'interaction avec le tableau de quête
        }

        // Mettre à jour la caméra pour suivre le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();

        if (currentScreen != null) {
            currentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    // Méthode pour déterminer si le joueur peut se déplacer
    private boolean canMove() {
        return !showQuestPlayer && !showInteractionFrame && !showDialogueFrame;  // Le joueur ne peut se déplacer que si les cadres de quête et d'interaction sont fermés
    }

    // Méthode pour vérifier si le joueur est proche du tableau d'interaction (50 unités)
    private boolean isPlayerWithinInteractionDistance(Vector2 playerPosition, Vector2 squarePosition, float distanceThreshold) {
        return playerPosition.dst(squarePosition) < distanceThreshold;  // Vérifie si le joueur est à une distance inférieure au seuil
    }

    // Méthode pour vérifier si le joueur est proche du tableau de quête (500 unités)
    private boolean isPlayerNearSquare(Vector2 playerPosition, Vector2 squarePosition, float distanceThreshold) {
        return playerPosition.dst(squarePosition) < distanceThreshold;  // Vérifie si le joueur est à une distance inférieure au seuil
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiViewport.update(width, height, true);
    }

    private void resetGame() {
        System.out.println("Redémarrage du jeu...");

        // Dispose des ressources existantes
        if (player != null) {
            player.dispose();
            player = null;
        }
        if (playerUI != null) {
            playerUI.dispose();
            playerUI = null;
        }
        if (zoneUI != null) {
            zoneUI.dispose();
            zoneUI = null;
        }
        // Disposer les mobs
        for (Mob mob : Mob.allMobs) {
            mob.dispose();
        }
        Mob.allMobs.clear();

        // Appeler initializeGame() pour recréer le joueur et l'UI
        initializeGame();
    }

    private void initializeGame() {
        // Récupérer le nom du joueur depuis le menu principal
        playerName = MainMenuScreen.getPlayerName();

        // Création d'une map de composants avec PositionComponent et HealthComponent
        Map<Class<? extends Component>, Component> components = new HashMap<>();
        components.put(HealthComponent.class, new HealthComponent(100, 100));
        components.put(PositionComponent.class, new PositionComponent(8300f, 2530f));

        // Recharger les mobs depuis la carte
        mobManager.loadMobsFromMap(mapManager.getTiledMap());

        // Création et initialisation de l'instance de PlayerCharacter
        player = new PlayerCharacter(2.0f, UUID.randomUUID(), playerName, components);

        // Initialiser l'UI du joueur
        playerUI = new PlayerUI(player, UI_WIDTH, UI_HEIGHT);

        // Initialiser le ZoneUI
        zoneUI = new ZoneUI(player, zoneManager, UI_WIDTH, UI_HEIGHT);

        // Mettre à jour la caméra sur le joueur
        camera.position.set(player.getX(), player.getY(), 0);
        camera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapManager.dispose();
        player.dispose();
        if (player != null) {
            player.dispose();
        }
        if (playerUI != null) {
            playerUI.dispose();
        }
        if (zoneUI != null) {
            zoneUI.dispose();
        }
        if (pathfinding != null) {
            pathfinding.dispose();
        }
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }
        if (mobUI != null) {
            mobUI.dispose();
        }
        if (soundManager != null) {
            soundManager.dispose();
        }
        for (Mob mob : Mob.allMobs) {
            mob.dispose();
        }
        Mob.allMobs.clear();
        F_Key_Texture.dispose();
        shapeRenderer.dispose(); // Libère ShapeRenderer
        font.dispose();
        inventaire.dispose();  // Libère les ressources utilisées dans inventaire
        quest.dispose();
        questBoardTexture.dispose();
        pathfinding.dispose();
        interactionImageManager.dispose();
        shop.dispose();  // Libère les ressources utilisées dans shop
        vente.dispose();  // Libère les ressources utilisées dans vente
    }

    private void initializePNJ(String name, float x, float y, String texturePath) {
        Map<Class<? extends Component>, Component> components = Map.of(
            PositionComponent.class, new PositionComponent(x, y),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            TextureComponent.class, new TextureComponent(new Texture(texturePath), 48, 48, 0, 0, 2.0f)
        );
        PNJ pnj = new PNJ(name, components, UUID.randomUUID());
        pnj.setName(name);
        switch (name) {
            case "Duc_Michel":
                pnj_duc = pnj;
                break;
            case "Jean-Benoit":
                pnj_shop = pnj;
                break;
            case "Damien":
                pnj_damien = pnj;
                break;
            case "Morgane":
                pnj_morgane = pnj;
                break;
            case "Fostine":
                pnj_fostine = pnj;
                break;
            case "Pierre":
                pnj_pierre = pnj;
                break;
            case "Marie":
                pnj_marie = pnj;
                break;
            case "Jean":
                pnj_jean = pnj;
                break;
            case "Luc":
                pnj_luc = pnj;
                break;
            case "Paul":
                pnj_paul = pnj;
                break;
            case "Claude":
                pnj_claude = pnj;
                break;
            case "Gaspard":
                pnj_gaspard = pnj;
                break;
        }
    }
}
