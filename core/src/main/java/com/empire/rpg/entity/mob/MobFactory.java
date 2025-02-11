package com.empire.rpg.entity.mob;

import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.component.pathfinding.Pathfinding;

/**
 * Factory pour créer des mobs en tant qu'entités avec des composants.
 */
public class MobFactory {
    private static Pathfinding pathfinding;

    public static void setPathfinding(Pathfinding pathfindingInstance) {
        pathfinding = pathfindingInstance;
    }

    public static Pathfinding getPathfinding() {
        return pathfinding;
    }

    public static void createMob(String type, Vector2 position, CollisionManager collisionManager) {
        Mob mob = null;
        switch (type.toLowerCase()) {
            case "goblin":
                mob = new Goblin(position, collisionManager);
                break;
            case "ogre":
                mob = new Ogre(position, collisionManager);
                break;
            case "orc":
                mob = new Orc(position, collisionManager);
                break;
            case "rabbit":
                mob = new Rabbit(position, collisionManager);
                break;
            case "rabbit-horned":
                mob = new RabbitHorned(position, collisionManager);
                break;
            case "lucas":
                mob = new Lucas(position, collisionManager);
                break;
            default:
                System.out.println("Type de mob inconnu: " + type);
        }
    }
}
