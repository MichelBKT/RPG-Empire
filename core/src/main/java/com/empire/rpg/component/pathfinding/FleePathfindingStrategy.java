package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.*;

import java.util.ArrayList;

/**
 * Stratégie de pathfinding où le mob fuit la cible (généralement le joueur).
 */
public class FleePathfindingStrategy implements PathfindingStrategy {
    private final Pathfinding pathfinding;
    private static final float MAX_FLEE_DISTANCE = 6 * 48; // 6 cellules de 48 px
    private static final int MAX_ATTEMPTS = 8; // Limite de tentatives pour trouver un point de fuite

    public FleePathfindingStrategy(Pathfinding pathfinding) {
        this.pathfinding = pathfinding;
    }

    /**
     * Calcule un chemin de fuite à partir de la cible.
     * @param mob Mob utilisant cette stratégie
     * @param target Position de la cible à fuir
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (non utilisé ici)
     */
    @Override
    public void calculatePath(Mob mob, Vector2 target, float deltaTime) {
        if (target == null) return;

        Vector2 fleeDirection = new Vector2(mob.getPosition()).sub(target).nor().scl(MAX_FLEE_DISTANCE);
        Vector2 fleeTarget = mob.getPosition().cpy().add(fleeDirection);

        int attempts = 0;
        while (attempts < MAX_ATTEMPTS && (pathfinding.isTileBlocked(fleeTarget) || mob.getPosition().dst(fleeTarget) > MAX_FLEE_DISTANCE)) {
            fleeDirection.rotateDeg(45); // Essaye une nouvelle direction
            fleeTarget = mob.getPosition().cpy().add(fleeDirection);
            attempts++;
        }

        if (!pathfinding.isTileBlocked(fleeTarget) && mob.getPosition().dst(fleeTarget) <= MAX_FLEE_DISTANCE) {
            pathfinding.calculatePath(mob, fleeTarget);
        } else {
            mob.setPath(new ArrayList<>()); // Définit un chemin vide pour éviter le blocage
        }
    }
}
