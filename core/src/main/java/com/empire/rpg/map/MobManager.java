package com.empire.rpg.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import com.empire.rpg.entity.mob.MobFactory;

public class MobManager {
    private final CollisionManager collisionManager;

    public MobManager(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
    }

    public void loadMobsFromMap(TiledMap tiledMap) {
        // Récupérer le calque "mob"
        MapLayer mobLayer = tiledMap.getLayers().get("mob");
        if (mobLayer != null) {
            MapObjects objects = mobLayer.getObjects();

            // Parcourir chaque objet du calque
            for (MapObject mapObject : objects) {
                String mobName = mapObject.getName();
                Float x = null;
                Float y = null;

                // Accéder aux propriétés "x" et "y" de l'objet
                if (mapObject.getProperties().containsKey("x") && mapObject.getProperties().containsKey("y")) {
                    x = ((Float) mapObject.getProperties().get("x"));
                    y = ((Float) mapObject.getProperties().get("y"));
                } else {
                    System.out.println("L'objet " + mobName + " n'a pas de propriétés 'x' et 'y'.");
                    continue;
                }

                if (x != null && y != null) {
                    // Créer le mob
                    MobFactory.createMob(mobName, new Vector2(x, y), collisionManager);
                } else {
                    System.out.println("Impossible de récupérer les coordonnées pour " + mobName);
                }
            }
        } else {
            System.out.println("Le calque 'mob' n'a pas été trouvé dans la carte.");
        }
    }
}
