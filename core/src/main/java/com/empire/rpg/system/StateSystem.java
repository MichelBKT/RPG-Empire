package com.empire.rpg.system;

import com.empire.rpg.component.Component;
import com.empire.rpg.entity.Entity;

/**
 * Système de Mise à Jour de l'État (StateSystem)
 * Ce système peut être utilisé pour gérer les changements d’état dans le jeu, comme les ouvertures/fermetures de coffre
 * ou les états de la pancarte (ouverte/fermée).
 */

public class StateSystem implements GameSystem<Component> {
    private Entity entity;
    private boolean isOpen;
    private boolean isClosed;

    public StateSystem(Entity entity) {
        this.entity = entity;
        this.isOpen = false;
        this.isClosed = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
    public void collect(Entity entity){
        // Actions à effectuer en cas de collecte
        //entity.putInInventory();
    }
    public void open(Entity entity){
        if (isClosed()) {
            setOpen(true);
            setClosed(false);
            // Actions à effectuer en cas d'ouverture
            collect(entity);
        } else if (isOpen()) {
            System.out.println("Le coffre est deja ouvert.");
        } else {
            System.out.println("La pancarte est fermee.");
        }
    }
    public void close(Entity entity){
        if (isOpen()) {
            setOpen(false);
            setClosed(true);
            // Actions à effectuer en cas de fermeture
        } else if (isClosed()) {
            System.out.println("Le coffre est deja ferme.");
        } else {
            System.out.println("La pancarte est ouverte.");
        }
    }
    private void display(Entity entity) {
        // Actions à effectuer en cas d'affichage
        System.out.println("Affichage de l'entité : " + entity);
    };

    @Override
    public void update(Component component) {
        // Logique de mise à jour de l'état

    }
}


