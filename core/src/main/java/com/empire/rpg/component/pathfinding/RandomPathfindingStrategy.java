package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.Mob;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Stratégie de pathfinding aléatoire pour les mobs en mode passif.
 */
public class RandomPathfindingStrategy implements PathfindingStrategy {
    private static final int MAX_RANDOM_DISTANCE = 5; // 5 cellules
    private static final int TILE_SIZE = 48; // Taille d'une cellule en pixels
    private static final float WAIT_TIME_SECONDS = 2.0f; // Temps d'attente entre les déplacements

    private final Pathfinding pathfinding;
    private final Random random;
    private float waitTime = WAIT_TIME_SECONDS; // Temps d'attente initial

    public RandomPathfindingStrategy(Pathfinding pathfinding) {
        this.pathfinding = pathfinding;
        this.random = new Random();
    }

    /**
     * Calcule un chemin aléatoire vers une destination sans collision.
     * @param mob Mob utilisant cette stratégie
     * @param ignored Non utilisé dans cette stratégie
     * @param deltaTime Temps écoulé depuis la dernière mise à jour
     */
    @Override
    public void calculatePath(Mob mob, Vector2 ignored, float deltaTime) {
        if (waitTime > 0) {
            waitTime -= deltaTime;
            return;
        }

        waitTime = WAIT_TIME_SECONDS; // Réinitialise le temps d'attente
        Vector2 startPosition = mob.getPosition();
        Vector2 randomTarget = generateRandomTarget(startPosition);
        pathfinding.calculatePath(mob, randomTarget);
    }

    private Vector2 generateRandomTarget(Vector2 startPosition) {
        int attempts = 0;

        while (attempts < 10) {
            int offsetX = random.nextInt(2 * MAX_RANDOM_DISTANCE + 1) - MAX_RANDOM_DISTANCE;
            int offsetY = random.nextInt(2 * MAX_RANDOM_DISTANCE + 1) - MAX_RANDOM_DISTANCE;
            Vector2 candidatePosition = new Vector2(startPosition.x + offsetX * TILE_SIZE, startPosition.y + offsetY * TILE_SIZE);

            if (!pathfinding.isTileBlocked(pathfinding.pixelToTile(candidatePosition)) && startPosition.dst(candidatePosition) <= MAX_RANDOM_DISTANCE * TILE_SIZE) {
                return candidatePosition;
            }
            attempts++;
        }

        return startPosition; // Retourne à la position initiale si aucun point n'est trouvé
    }
}
