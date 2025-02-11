package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.entity.mob.Mob;

import java.util.ArrayList;
import java.util.List;

/**
 * Stratégie de pathfinding pour retourner au spawn.
 */
public class GoToPathfindingStrategy implements PathfindingStrategy {
    private final Pathfinding pathfinding;
    private static final int MAX_STEP_DISTANCE = 6 * 48; // 6 cellules de 48 px

    /**
     * Constructeur de la stratégie.
     * @param pathfinding Instance de Pathfinding utilisée pour calculer le chemin
     */
    public GoToPathfindingStrategy(Pathfinding pathfinding) {
        this.pathfinding = pathfinding;
    }

    /**
     * Calcule un chemin vers la position cible, en ajoutant des points intermédiaires si la distance dépasse MAX_STEP_DISTANCE.
     * @param mob Mob utilisant cette stratégie
     * @param target Position cible (point B)
     * @param deltaTime Temps écoulé depuis la dernière mise à jour (non utilisé ici)
     */
    @Override
    public void calculatePath(Mob mob, Vector2 target, float deltaTime) {
        if (target == null) return;

        Vector2 start = mob.getPosition();
        List<Vector2> waypoints = generateIntermediatePoints(start, target);

        List<Vector2> fullPath = new ArrayList<>();
        for (int i = 0; i < waypoints.size() - 1; i++) {
            List<Vector2> segmentPath = pathfinding.calculateSegmentPath(waypoints.get(i), waypoints.get(i + 1));
            fullPath.addAll(segmentPath);
        }

        mob.setPath(fullPath); // Définit le chemin complet avec les points intermédiaires
    }

    /**
     * Génère des points intermédiaires entre le point de départ et la cible si la distance est trop grande.
     * @param start Position de départ (point A)
     * @param target Position cible (point B)
     * @return Liste de points intermédiaires
     */
    private List<Vector2> generateIntermediatePoints(Vector2 start, Vector2 target) {
        List<Vector2> points = new ArrayList<>();
        points.add(start);

        Vector2 direction = new Vector2(target).sub(start).nor().scl(MAX_STEP_DISTANCE);
        Vector2 currentPoint = start.cpy();

        while (currentPoint.dst(target) > MAX_STEP_DISTANCE) {
            currentPoint.add(direction);
            points.add(currentPoint.cpy()); // Ajouter le point intermédiaire
        }

        points.add(target); // Ajouter le point cible final
        return points;
    }
}
