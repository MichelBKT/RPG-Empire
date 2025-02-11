package com.empire.rpg.component;

import com.badlogic.gdx.math.Rectangle;

/**
 * La classe CollisionComponent représente un composant qui gère la détection de collision
 * et l'état de collision pour une entité dans le jeu.
 */

public class CollisionComponent implements Component {
    private boolean isCollidable;
    private Rectangle boundingBox;

    /**
     * Constructeur d'un CollisionComponent avec un état de collision spécifié.
     *
     * @param isCollidable l'état de collision initial
     * @param boundingBox la boîte de collision de l'entité
     */

    public CollisionComponent(boolean isCollidable, Rectangle boundingBox) {
        this.isCollidable = isCollidable;
        this.boundingBox = boundingBox;
    }

    /**
     * Retourne si le composant est collidable.
     *
     * @return true si le composant est collidable, false sinon
     */
    public boolean isCollidable() {
        return isCollidable;
    }

    /**
     * Affecte l'état de collision du composant.
     *
     * @param collidable le nouvel état de collision
     */

    public void setCollidable(boolean collidable) {
        isCollidable = collidable;
    }

    /**
     * Retourne la bounding box de l'entité.
     *
     * @return la bounding box
     */
    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    /**
     * Définit une nouvelle bounding box.
     *
     * @param boundingBox la nouvelle bounding box
     */
    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Detecte une collision et affiche un message dans la console.
     *
     * @return true si une collision est détectée
     */

    public boolean detectCollision() {
        System.out.println("Collision detected");
        return true;
    }

    /**
     * Inverse l'état de collision du composant.
     *
     * @return le nouvel état de collision
     */

    public boolean toggleCollision() {
        isCollidable = !isCollidable;
        return isCollidable;
    }

    /**
     * Retourne le type du composant.
     *
     * @return le type du composant
     */

    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Affecte le type du composant.
     *
     * @param i le nouveau type du composant
     */

    @Override
    public void setCurrentHealthPoints(int i) {
        // No implementation needed
    }

    /**
     * Retourne les points de vie actuels du composant.
     *
     * @return les points de vie actuels
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
