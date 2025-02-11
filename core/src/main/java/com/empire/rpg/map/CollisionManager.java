package com.empire.rpg.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionManager {
    private final Array<Rectangle> collisionRectangles = new Array<>();
    private final Array<Ellipse> collisionEllipses = new Array<>();
    private final Array<Polygon> collisionPolygons = new Array<>();

    public CollisionManager(TiledMap tiledMap) {
        // Charger la couche de collision
        MapLayer collisionLayer = tiledMap.getLayers().get("collision");
        if (collisionLayer != null) {
            MapObjects objects = collisionLayer.getObjects();

            // Traiter chaque type d'objet
            for (RectangleMapObject rectangleObject : objects.getByType(RectangleMapObject.class)) {
                collisionRectangles.add(rectangleObject.getRectangle());
            }
            for (EllipseMapObject ellipseObject : objects.getByType(EllipseMapObject.class)) {
                collisionEllipses.add(ellipseObject.getEllipse());
            }
            for (PolygonMapObject polygonObject : objects.getByType(PolygonMapObject.class)) {
                collisionPolygons.add(polygonObject.getPolygon());
            }
        }
    }

    public boolean isColliding(Rectangle playerRect) {
        // Vérifier les collisions avec les rectangles
        for (Rectangle rect : collisionRectangles) {
            if (playerRect.overlaps(rect)) {
                return true;
            }
        }

        // Vérifier les collisions avec les ellipses (approximation par cercle)
        for (Ellipse ellipse : collisionEllipses) {
            float ellipseCenterX = ellipse.x + ellipse.width / 2;
            float ellipseCenterY = ellipse.y + ellipse.height / 2;
            float radius = Math.min(ellipse.width, ellipse.height) / 2; // Approximation

            if (overlapsCircleRectangle(ellipseCenterX, ellipseCenterY, radius, playerRect)) {
                return true;
            }
        }

        // Vérifier les collisions avec les polygones
        for (Polygon polygon : collisionPolygons) {
            if (Intersector.overlapConvexPolygons(new Polygon(new float[]{
                playerRect.x, playerRect.y,
                playerRect.x + playerRect.width, playerRect.y,
                playerRect.x + playerRect.width, playerRect.y + playerRect.height,
                playerRect.x, playerRect.y + playerRect.height
            }), polygon)) {
                return true;
            }
        }

        return false;
    }

    public boolean isColliding(Rectangle rect1, Rectangle rect2) {
        return rect1.overlaps(rect2);
    }

    // Fonction pour vérifier la collision entre un cercle et un rectangle
    private boolean overlapsCircleRectangle(float circleX, float circleY, float radius, Rectangle rectangle) {
        float closestX = Math.max(rectangle.x, Math.min(circleX, rectangle.x + rectangle.width));
        float closestY = Math.max(rectangle.y, Math.min(circleY, rectangle.y + rectangle.height));

        float distanceX = circleX - closestX;
        float distanceY = circleY - closestY;

        return (distanceX * distanceX + distanceY * distanceY) < (radius * radius);
    }

    // Getters pour les objets de collision
    public Array<Rectangle> getCollisionRectangles() {
        return collisionRectangles;
    }

    public Array<Ellipse> getCollisionEllipses() {
        return collisionEllipses;
    }

    public Array<Polygon> getCollisionPolygons() {
        return collisionPolygons;
    }
}
