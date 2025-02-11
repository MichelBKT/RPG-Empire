package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.Mob;

import java.util.List;

/**
 * Interface pour les stratégies de pathfinding.
 */
public interface PathfindingStrategy {
    /**
     * Calcule le chemin pour le mob vers la cible.
     *
     * @param mob       Le mob pour lequel calculer le chemin.
     * @param target    La position cible.
     * @param deltaTime Le temps écoulé.
     */
    void calculatePath(Mob mob, Vector2 target, float deltaTime);
}
