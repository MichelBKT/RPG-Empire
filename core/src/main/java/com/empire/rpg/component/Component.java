package com.empire.rpg.component;

/**
 * Interface qui represente un composant.
 */
public interface Component {

    /**
     * Retourne la réduction de dégâts.
     *
     * @return Réduction de dégâts (toujours 0 dans cette implémentation).
     */
    int getDamageReduction();

    /**
     * Définit les points de vie actuels.
     *
     * @param i Les nouveaux points de vie.
     */
    void setCurrentHealthPoints(int i);

    /**
     * Retourne les points de vie actuels.
     *
     * @return Points de vie actuels.
     */
    int getCurrentHealthPoints();
}
