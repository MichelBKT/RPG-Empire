package com.empire.rpg.component;

/**
 * Composant de mouvement pour les entités du jeu.
 */
public class MovementComponent implements Component {
    private float speed;
    private String direction;

    /**
     * Constructeur pour MovementComponent.
     *
     * @param speed La vitesse de déplacement.
     * @param direction La direction de déplacement.
     */
    public MovementComponent(float speed, String direction) {
        this.speed = speed;
        this.direction = direction;
    }

    /**
     * Obtient la direction actuelle.
     *
     * @return La direction actuelle.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Définit la direction.
     *
     * @param direction La nouvelle direction.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Obtient la vitesse actuelle.
     *
     * @return La vitesse actuelle.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Définit la vitesse.
     *
     * @param speed La nouvelle vitesse.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Déplace l'entité dans une nouvelle direction.
     *
     * @param direction La nouvelle direction.
     */
    public void move(String direction) {
        this.direction = direction;
    }

    /**
     * Arrête le mouvement de l'entité.
     */
    public void stop() {
        this.direction = "stop";
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return La réduction des dégâts (toujours 0 pour ce composant).
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i Les nouveaux points de vie.
     */
    @Override
    public void setCurrentHealthPoints(int i) {
        // Implémentation vide
    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return Les points de vie actuels (toujours 0 pour ce composant).
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
