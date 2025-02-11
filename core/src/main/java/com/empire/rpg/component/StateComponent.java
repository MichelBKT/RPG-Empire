package com.empire.rpg.component;

/**
 * Représente un composant d'état dans le jeu.
 */

public class StateComponent implements Component {
    private boolean currentState;
    private String nameState;

    /**
     * Constructeur pour créer un StateComponent.
     *
     * @param currentState l'état actuel du composant
     * @param nameState le nom de l'état
     */
    public StateComponent(boolean currentState, String nameState) {
        this.currentState = currentState;
        this.nameState = nameState;
    }

    /**
     * Vérifie si l'état actuel est actif.
     *
     * @return true si l'état actuel est actif, sinon false
     */
    public boolean isCurrentState() {
        return currentState;
    }

    /**
     * Définit l'état actuel.
     *
     * @param currentState le nouvel état actuel
     */
    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
    }

    /**
     * Obtient le nom de l'état.
     *
     * @return le nom de l'état
     */
    public String getNameState() {
        return nameState;
    }

    /**
     * Définit le nom de l'état.
     *
     * @param nameState le nouveau nom de l'état
     */
    public void setNameState(String nameState) {
        this.nameState = nameState;
    }

    /**
     * Bascule l'état actuel.
     */
    public void toggleState(){
        this.currentState = !this.currentState;
    }

    /**
     * Obtient la réduction de dégâts.
     *
     * @return la réduction de dégâts (toujours 0)
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i les nouveaux points de vie actuels
     */
    @Override
    public void setCurrentHealthPoints(int i) {

    }

    /**
     * Obtient les points de vie actuels.
     *
     * @return les points de vie actuels (toujours 0)
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
