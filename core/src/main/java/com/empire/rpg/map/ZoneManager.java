package com.empire.rpg.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ZoneManager {
    public static class Zone {
        public String name;
        public Polygon polygon;

        public Zone(String name, Polygon polygon) {
            this.name = name;
            this.polygon = polygon;
        }
    }

    private List<Zone> zones;

    public ZoneManager() {
        zones = new ArrayList<>();
    }

    public void loadZonesFromMap(TiledMap tiledMap) {
        MapLayer zoneLayer = tiledMap.getLayers().get("zone");
        if (zoneLayer != null) {
            MapObjects objects = zoneLayer.getObjects();

            for (MapObject mapObject : objects) {
                if (mapObject instanceof PolygonMapObject) {
                    String zoneName = mapObject.getName();
                    PolygonMapObject polygonObject = (PolygonMapObject) mapObject;

                    // Obtenir le polygone
                    Polygon polygon = polygonObject.getPolygon();

                    // Récupérer les coordonnées x et y de l'objet
                    Float x = getFloatProperty(mapObject, "x");
                    Float y = getFloatProperty(mapObject, "y");

                    if (x != null && y != null) {
                        // Ajuster la position du polygone en fonction de l'objet
                        polygon.setPosition(x, y);

                        // Créer et ajouter la zone à la liste
                        zones.add(new Zone(zoneName, polygon));
                    } else {
                        System.out.println("Impossible de récupérer les coordonnées pour la zone " + zoneName);
                    }
                } else {
                    System.out.println("L'objet " + mapObject.getName() + " n'est pas un PolygonMapObject.");
                }
            }
        } else {
            System.out.println("Le calque 'zone' n'a pas été trouvé dans la carte.");
        }
    }

    public String getZoneNameAt(float x, float y) {
        for (Zone zone : zones) {
            if (zone.polygon.contains(x, y)) {
                return zone.name;
            }
        }
        return null; // Aucune zone correspondante trouvée
    }

    // Méthode utilitaire pour obtenir une propriété float à partir d'un MapObject
    private Float getFloatProperty(MapObject mapObject, String propertyName) {
        Object value = mapObject.getProperties().get(propertyName);
        if (value != null) {
            return Float.parseFloat(value.toString());
        }
        return null;
    }
}
