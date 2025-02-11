package com.empire.rpg;

import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.map.CollisionManager;

public class CollisionHandler {
    private final CollisionManager collisionManager;

    public CollisionHandler(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    /**
     * Gère la collision entre le joueur et un mob.
     * Si une collision est détectée, retourne le joueur à sa position précédente.
     *
     * @param player L'instance du joueur
     * @param mob    L'instance du mob
     */
    public void handlePlayerMobCollision(PlayerCharacter player, Mob mob) {
        if (collisionManager.isColliding(player.getCollisionBounds(), mob.getCollisionBounds())) {
            // Si une collision est détectée, annuler le mouvement du joueur en le ramenant à sa position précédente
            player.restorePreviousPosition();
        }
    }

    /**
     * Vérifie et gère les collisions entre le joueur et tous les mobs.
     *
     * @param player L'instance du joueur
     * @param mobs   La liste de tous les mobs dans le jeu
     */
    public void handleCollisions(PlayerCharacter player, Iterable<Mob> mobs) {
        for (Mob mob : mobs) {
            handlePlayerMobCollision(player, mob);
        }
    }
}
