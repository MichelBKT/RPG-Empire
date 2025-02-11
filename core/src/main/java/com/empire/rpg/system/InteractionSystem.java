package com.empire.rpg.system;


import com.empire.rpg.component.Component;
import com.empire.rpg.entity.Entity;

/**
 * Système d'Interaction (InteractionSystem)
 * Ce système gère l’interaction entre le joueur et les objets ou entités spécifiques, comme les pancartes ou les coffres.
 * Par exemple, il vérifie si le joueur est proche d'un objet et souhaite interagir avec lui, en activant ensuite des actions
 * spécifiques (ouvrir un coffre, lire une pancarte, etc.).
 */

public class InteractionSystem implements GameSystem<Component> {
    private Entity entity;
    private boolean isInteracting;
    private boolean isInteractable;

    public InteractionSystem(Entity entity){
        this.entity = entity;
        this.isInteracting = false;
        this.isInteractable = false;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isInteracting() {
        return isInteracting;
    }

    public void setInteracting(boolean interacting) {
        isInteracting = interacting;
    }

    public boolean isInteractable() {
        return isInteractable;
    }

    public void setInteractable(boolean interactable) {
        isInteractable = interactable;
    }

    public void interact(){
        if (isInteractable()){
            // Actions à effectuer en cas d'interaction
        }
    }

    @Override
    public void update(Component component) {
        // Logique de mise à jour de l'interaction
    }
}
