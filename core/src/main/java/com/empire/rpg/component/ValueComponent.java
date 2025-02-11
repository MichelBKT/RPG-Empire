package com.empire.rpg.component;

/**
 * Représente un composant de valeur dans le jeu.
 */
public class ValueComponent implements Component {
    private final int value;

    /**
     * Constructeur pour initialiser la valeur du composant.
     *
     * @param value la valeur à initialiser
     */
    public ValueComponent(int value) {
        this.value = value;
    }

    /**
     * Obtient la valeur du composant.
     *
     * @return la valeur du composant
     */
    public int getValue() {
        return value;
    }

    /**
     * Obtient la réduction de dégâts du composant.
     *
     * @return toujours 0 car ce composant n'a pas de réduction de dégâts
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels du composant.
     *
     * @param i les points de vie à définir
     */
    @Override
    public void setCurrentHealthPoints(int i) {

    }

    /**
     * Obtient les points de vie actuels du composant.
     *
     * @return toujours 0 car ce composant n'a pas de points de vie
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
