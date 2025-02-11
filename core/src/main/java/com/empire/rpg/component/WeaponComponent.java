package com.empire.rpg.component;

/**
 * Représente un composant d'arme dans le jeu.
 */
public class WeaponComponent implements Component {
    private String name;
    private int damage;

    /**
     * Constructeur pour créer un composant d'arme.
     *
     * @param name le nom de l'arme
     * @param attackPoints les points d'attaque de l'arme
     */
    public WeaponComponent(String name, int attackPoints) {
        this.name = name;
        this.damage = attackPoints;
    }

    /**
     * Obtient le nom de l'arme.
     *
     * @return le nom de l'arme
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom de l'arme.
     *
     * @param name le nouveau nom de l'arme
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtient les points de dégâts de l'arme.
     *
     * @return les points de dégâts de l'arme
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Définit les points de dégâts de l'arme.
     *
     * @param damage les nouveaux points de dégâts de l'arme
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * Obtient la réduction des dégâts.
     *
     * @return toujours 0 car une arme n'a pas de réduction de dégâts
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     * Non implémenté pour le composant d'arme.
     *
     * @param i les nouveaux points de vie
     */
    @Override
    public void setCurrentHealthPoints(int i) {
        // Non implémenté
    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return toujours 0 car une arme n'a pas de points de vie
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
