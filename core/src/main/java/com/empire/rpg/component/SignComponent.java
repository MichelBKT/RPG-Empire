package com.empire.rpg.component;

/**
 * Représente un composant de panneau dans le jeu.
 */
public class SignComponent implements Component {
    private boolean isOpen;
    private final String message;

    /**
     * Constructeur pour créer un SignComponent avec un message.
     * Par défaut, la pancarte est fermée.
     *
     * @param message Le message affiché sur la pancarte.
     */
    public SignComponent(String message) {
        this.isOpen = false; // Par défaut, la pancarte est fermée
        this.message = message;
    }

    /**
     * Vérifie si la pancarte est ouverte.
     *
     * @return true si la pancarte est ouverte, sinon false.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Bascule l'état de la pancarte entre ouverte et fermée.
     */
    public void toggle() {
        isOpen = !isOpen;
    }

    /**
     * Obtient le message affiché sur la pancarte.
     *
     * @return Le message de la pancarte.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtient la réduction des dégâts pour ce composant.
     *
     * @return La réduction des dégâts, toujours 0 pour ce composant.
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels pour ce composant.
     * Non implémenté pour ce composant.
     *
     * @param i Les points de vie à définir.
     */
    @Override
    public void setCurrentHealthPoints(int i) {
        // Non implémenté
    }

    /**
     * Obtient les points de vie actuels pour ce composant.
     *
     * @return Les points de vie actuels, toujours 0 pour ce composant.
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
