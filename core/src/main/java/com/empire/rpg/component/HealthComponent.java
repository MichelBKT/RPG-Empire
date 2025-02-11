package com.empire.rpg.component;

/**
 * Composant de santé pour un personnage ou une entité.
 */
public class HealthComponent implements Component {
    private int currentHealthPoints;
    private int maxHealthPoints;

    /**
     * Constructeur pour initialiser les points de santé actuels et maximum.
     *
     * @param currentHealthPoints Points de santé actuels.
     * @param maxHealthPoints     Points de santé maximum.
     */
    public HealthComponent(int currentHealthPoints, int maxHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
        this.maxHealthPoints = maxHealthPoints;
    }

    /**
     * Retourne la réduction de dégâts.
     *
     * @return Réduction de dégâts (toujours 0 dans cette implémentation).
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Retourne les points de santé actuels.
     *
     * @return Points de santé actuels.
     */
    public int getCurrentHealthPoints() {
        return currentHealthPoints;
    }

    /**
     * Définit les points de santé actuels.
     *
     * @param currentHealthPoints Nouveaux points de santé actuels.
     */
    public void setCurrentHealthPoints(int currentHealthPoints) {
        this.currentHealthPoints = currentHealthPoints;
    }

    /**
     * Retourne les points de santé maximum.
     *
     * @return Points de santé maximum.
     */
    public int getMaxHealthPoints() {
        return maxHealthPoints;
    }

    /**
     * Définit les points de santé maximum.
     *
     * @param maxHealthPoints Nouveaux points de santé maximum.
     */
    public void setMaxHealthPoints(int maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    /**
     * Applique des dégâts aux points de santé actuels.
     *
     * @param damage Quantité de dégâts à appliquer.
     * @return Points de santé restants après application des dégâts.
     */
    public int takeDamage(int damage){
        this.currentHealthPoints = Math.max(this.currentHealthPoints - damage, 0);
        return this.currentHealthPoints;
    }

    /**
     * Applique des points de guérison aux points de santé actuels.
     *
     * @param heal Quantité de points de guérison à appliquer.
     * @return Points de santé après guérison.
     */
    public int heal(int heal){
        this.currentHealthPoints = Math.min(this.currentHealthPoints + heal, this.maxHealthPoints);
        return this.currentHealthPoints;
    }
}
