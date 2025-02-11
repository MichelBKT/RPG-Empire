package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.Mob;

/**
 * Stratégie de pathfinding où le mob suit la cible (généralement le joueur).
 */
public class FollowPathfindingStrategy implements PathfindingStrategy {
    private final Pathfinding pathfinding;

    public FollowPathfindingStrategy(Pathfinding pathfinding) {
        this.pathfinding = pathfinding;
    }

    /**
     * Calcule un chemin pour suivre la cible.
     * @param mob Mob utilisant cette stratégie
     * @param target Position cible (généralement celle du joueur)
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (non utilisé ici)
     */
    @Override
    public void calculatePath(Mob mob, Vector2 target, float deltaTime) {
        if (target != null) {
            pathfinding.calculatePath(mob, target);
        }
    }
}
