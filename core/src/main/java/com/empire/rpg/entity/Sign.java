package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import com.empire.rpg.component.SignComponent;

import java.util.Map;
import java.util.UUID;

/**
 * Représente une entité de type pancarte dans le jeu.
 */
public class Sign extends Entity {

    /**
     * Constructeur pour créer une nouvelle pancarte.
     *
     * @param name       Le nom de la pancarte.
     * @param components Les composants associés à la pancarte.
     * @param id         L'identifiant unique de la pancarte.
     */
    public Sign(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Interagit avec la pancarte en l'ouvrant ou la fermant.
     * Affiche un message en fonction de l'état de la pancarte.
     */
    public void interact() {
        SignComponent signComponent = (SignComponent) getComponent(SignComponent.class);
        if (signComponent != null) {
            // Ouvre ou ferme la pancarte
            signComponent.toggle();
            if (signComponent.isOpen()) {
                System.out.println("La pancarte affiche : " + signComponent.getMessage());
            } else {
                System.out.println("La pancarte est maintenant fermee.");
            }
        }
    }

    /**
     * Ajoute une entité. Non implémenté pour les pancartes.
     *
     * @return Toujours null.
     */
    @Override
    public Entity addEntity() {
        return null;
    }

    /**
     * Supprime une entité par son nom. Non implémenté pour les pancartes.
     *
     * @param name Le nom de l'entité à supprimer.
     * @return Toujours null.
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }
}
