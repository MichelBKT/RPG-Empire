package com.empire.rpg.entity.player.components;

import com.badlogic.gdx.math.Rectangle;

// Composant de collision pour le joueur
public class CollisionComponent {
    private Rectangle boundingBox; // Boîte englobante pour la collision
    private float offsetX; // Décalage en X par rapport à la position du joueur
    private float offsetY; // Décalage en Y par rapport à la position du joueur

    // Constructeur du composant de collision
    public CollisionComponent(float x, float y, float width, float height, float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        // Initialiser la boîte englobante en tenant compte du décalage
        boundingBox = new Rectangle(x + offsetX, y + offsetY, width, height);
    }

    // Getter pour la boîte englobante
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    // Mettre à jour la position de la boîte de collision
    public void setPosition(float x, float y) {
        boundingBox.setPosition(x + offsetX, y + offsetY);
    }

    // Mettre à jour la taille de la boîte de collision
    public void setSize(float width, float height) {
        boundingBox.setSize(width, height);
    }

    // Mettre à jour le décalage de la boîte de collision
    public void setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    // Getters pour les décalages
    public float getOffsetX() {
        return offsetX;
    }
    public float getOffsetY() {
        return offsetY;
    }
}
