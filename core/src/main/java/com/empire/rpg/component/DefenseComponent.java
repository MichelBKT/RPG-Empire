package com.empire.rpg.component;

/**
 * Classe représentant un composant de défense.
 */
public class DefenseComponent implements Component {
    private int armorPoints;
    private int damageReduction;

    /**
     * Constructeur pour créer un composant de défense.
     *
     * @param armorPoints      Points d'armure initiaux.
     * @param damageReduction  Réduction des dégâts initiale.
     */
    public DefenseComponent(int armorPoints, int damageReduction) {
        this.armorPoints = armorPoints;
        this.damageReduction = damageReduction;
    }

    /**
     * Obtient les points d'armure.
     *
     * @return Points d'armure.
     */
    public int getArmorPoints() {
        return armorPoints;
    }

    /**
     * Définit les points d'armure.
     *
     * @param armorPoints Points d'armure à définir.
     */
    public void setArmorPoints(int armorPoints) {
        this.armorPoints = armorPoints;
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return Réduction des dégâts.
     */
    public int getDamageReduction() {
        return damageReduction;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i Points de vie actuels à définir.
     */
    @Override
    public void setCurrentHealthPoints(int i) {
    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return Points de vie actuels.
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }

    /**
     * Définit la réduction des dégâts.
     *
     * @param damageReduction Réduction des dégâts à définir.
     */
    public void setDamageReduction(int damageReduction) {
        this.damageReduction = damageReduction;
    }

    /**
     * Applique des dégâts aux points d'armure.
     *
     * @param damage Dégâts à appliquer.
     */
    public void takeDamage(int damage) {
        this.armorPoints -= damage;
    }

    /**
     * Augmente les points d'armure.
     *
     * @param armorPoints Points d'armure à ajouter.
     */
    public void increaseArmorPoints(int armorPoints) {
        this.armorPoints += armorPoints;
    }
}
