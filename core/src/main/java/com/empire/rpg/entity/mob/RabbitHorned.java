package com.empire.rpg.entity.mob;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Camera;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.component.pathfinding.*;
import com.empire.rpg.entity.player.PlayerCharacter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RabbitHorned extends Mob {

    // Animations pour idle
    private Animation<TextureRegion> idleUp, idleDown, idleLeft, idleRight;
    // Animations pour déplacement
    private Animation<TextureRegion> runUp, runDown, runLeft, runRight;
    // Temps écoulé pour les animations
    private float stateTime;
    // Direction actuelle du Gobelin
    private String currentDirection = "DOWN"; // Direction par défaut
    // Indicateur de mouvement
    private boolean isMoving;

    // Textures chargées
    private Texture idleTexture, runTexture;

    public RabbitHorned(Vector2 position, CollisionManager collisionManager) {
        super(
            "Lapin Cornu",
            UUID.randomUUID(),
            initializeComponents(position),
            350f,
            new FleePathfindingStrategy(MobFactory.getPathfinding()), // Stratégie de suivi du joueur
            new GoToPathfindingStrategy(MobFactory.getPathfinding()), // Stratégie pour retourner au spawn
            collisionManager
        );
        this.scale = 2.0f;      // Zoom factor (2x)
        this.offsetX = -95f;    // Décalage X
        this.offsetY = -80f;    // Décalage Y
    }

    private static Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> initializeComponents(Vector2 position) {
        Map<Class<? extends com.empire.rpg.component.Component>, com.empire.rpg.component.Component> components = new HashMap<>();
        components.put(PositionComponent.class, new PositionComponent(position.x, position.y));
        components.put(HealthComponent.class, new HealthComponent(40, 40));
        components.put(CollisionComponent.class, new CollisionComponent(true, new Rectangle(position.x, position.y, 32, 32)));
        return components;
    }

    @Override
    protected void initializeTextures() {
        // Charger et diviser les textures d'idle
        idleTexture = new Texture("mobs/rabbit-horned/Rabbit_Horned_Idle.png");
        TextureRegion[][] idleSplit = TextureRegion.split(idleTexture, 128, 128);

        idleUp = new Animation<>(0.1f, idleSplit[3][0], idleSplit[3][1], idleSplit[3][2], idleSplit[3][3]);
        idleDown = new Animation<>(0.1f, idleSplit[0][0], idleSplit[0][1], idleSplit[0][2], idleSplit[0][3]);
        idleLeft = new Animation<>(0.1f, idleSplit[1][0], idleSplit[1][1], idleSplit[1][2], idleSplit[1][3]);
        idleRight = new Animation<>(0.1f, idleSplit[2][0], idleSplit[2][1], idleSplit[2][2], idleSplit[2][3]);

        // Charger et diviser les textures de déplacement
        runTexture = new Texture("mobs/rabbit-horned/Rabbit_Horned_Move.png");
        TextureRegion[][] runSplit = TextureRegion.split(runTexture, 128, 128);

        runUp = new Animation<>(0.05f, runSplit[3][0], runSplit[3][1], runSplit[3][2], runSplit[3][3], runSplit[3][4], runSplit[3][5]);
        runDown = new Animation<>(0.05f, runSplit[0][0], runSplit[0][1], runSplit[0][2], runSplit[0][3], runSplit[0][4], runSplit[0][5]);
        runLeft = new Animation<>(0.05f, runSplit[1][0], runSplit[1][1], runSplit[1][2], runSplit[1][3], runSplit[1][4], runSplit[1][5]);
        runRight = new Animation<>(0.05f, runSplit[2][0], runSplit[2][1], runSplit[2][2], runSplit[2][3], runSplit[2][4], runSplit[2][5]);

        // Initialiser la texture courante
        currentTexture = idleDown.getKeyFrame(0, true);
    }

    @Override
    public void update(float deltaTime, PlayerCharacter player, Camera camera) {
        super.update(deltaTime, player, camera); // Appel de la mise à jour de base

        // Déterminer si le mob est en mouvement
        isMoving = !currentPath.isEmpty() && targetPosition != null;

        // Déterminer la direction basée sur le mouvement
        if (isMoving && targetPosition != null) {
            float deltaX = targetPosition.x - getPositionVector().x;
            float deltaY = targetPosition.y - getPositionVector().y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                currentDirection = (deltaX > 0) ? "RIGHT" : "LEFT";
            } else {
                currentDirection = (deltaY > 0) ? "UP" : "DOWN";
            }
        }

        // Mettre à jour le temps d'état pour les animations
        stateTime += deltaTime;

        // Sélectionner l'animation appropriée
        Animation<TextureRegion> currentAnimation;
        if (isMoving) {
            switch (currentDirection) {
                case "UP":
                    currentAnimation = runUp;
                    break;
                case "DOWN":
                    currentAnimation = runDown;
                    break;
                case "LEFT":
                    currentAnimation = runLeft;
                    break;
                case "RIGHT":
                    currentAnimation = runRight;
                    break;
                default:
                    currentAnimation = idleDown;
            }
        } else {
            switch (currentDirection) {
                case "UP":
                    currentAnimation = idleUp;
                    break;
                case "DOWN":
                    currentAnimation = idleDown;
                    break;
                case "LEFT":
                    currentAnimation = idleLeft;
                    break;
                case "RIGHT":
                    currentAnimation = idleRight;
                    break;
                default:
                    currentAnimation = idleDown;
            }
        }

        // Obtenir la frame actuelle de l'animation
        currentTexture = currentAnimation.getKeyFrame(stateTime, true);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (idleTexture != null) {
            idleTexture.dispose();
        }
        if (runTexture != null) {
            runTexture.dispose();
        }
    }

    @Override
    protected Map<String, Object> getDeathInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("Item-Name", "Peau de Lapin Horned");
        info.put("Item-Description", "Peau de lapin avec des cornes, un ingrédient très rare.");
        info.put("Item-Quantity", 1);
        info.put("Item-Type", "divers");
        return info;
    }
}
