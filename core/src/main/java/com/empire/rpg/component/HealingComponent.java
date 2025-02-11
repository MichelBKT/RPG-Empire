package com.empire.rpg.component;

/**
 * Composant de guérison pour les entités du jeu.
 */
public class HealingComponent implements Component {
    private final int healingPoints;

    /**
     * Constructeur pour créer un composant de guérison.
     *
     * @param healingPoints le nombre de points de guérison
     */
    public HealingComponent(int healingPoints) {
        this.healingPoints = healingPoints;
    }

    /**
     * Obtient le nombre de points de guérison.
     *
     * @return le nombre de points de guérison
     */
    public int getHealingPoints() {
        return healingPoints;
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return toujours 0 car ce composant ne réduit pas les dégâts
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i les points de vie actuels
     */
    @Override
    public void setCurrentHealthPoints(int i) {

    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return toujours 0 car ce composant ne gère pas les points de vie
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
