package com.empire.rpg.component;

import com.badlogic.gdx.math.Vector2;

/**
 * Représente un composant de position dans le jeu.
 */
public class PositionComponent implements Component {
    private float x;
    private float y;

    /**
     * Constructeur pour initialiser la position.
     *
     * @param x La coordonnée x initiale.
     * @param y La coordonnée y initiale.
     */
    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Obtient la coordonnée y.
     *
     * @return La coordonnée y.
     */
    public float getY() {
        return y;
    }

    /**
     * Obtient la coordonnée x.
     *
     * @return La coordonnée x.
     */
    public float getX() {
        return x;
    }

    /**
     * Définit une nouvelle position.
     *
     * @param x La nouvelle coordonnée x.
     * @param y La nouvelle coordonnée y.
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Affiche la position actuelle.
     */
    public void getPosition() {
        System.out.println("X: " + x + " Y: " + y);
    }

    /**
     * Retourne la position actuelle sous forme d'un Vector2.
     *
     * @return La position actuelle sous forme d'un Vector2.
     */
    public Vector2 getPlayerPosition() {
        return new Vector2(x, y);  // Renvoie la position en tant que Vector2
    }

    /**
     * Déplace la position actuelle par les valeurs spécifiées.
     *
     * @param x La valeur de déplacement en x.
     * @param y La valeur de déplacement en y.
     */
    public void move(float x, float y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return Toujours 0 pour ce composant.
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i Les points de vie actuels.
     */
    @Override
    public void setCurrentHealthPoints(int i) {
        // Implémentation vide
    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return Toujours 0 pour ce composant.
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
