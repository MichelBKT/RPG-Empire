package com.empire.rpg.entity;

import java.util.UUID;

/**
 * Classe de gestion des entités dans le jeu.
 * Elle contient le nom de l'entité et peut être étendue par d'autres classes.
 */
public abstract class EntityManager {
    protected String name;

    /**
     * Constructeur de EntityManager avec un nom.
     *
     * @param name Le nom de l'entité.
     */
    public EntityManager(String name) {
        this.name = name;
    }

    /**
     * Récupère le nom de l'entité.
     *
     * @return Le nom de l'entité.
     */
    public String getName() {
        return name;
    }

    /**
     * Définit un nouveau nom pour l'entité.
     *
     * @param name Le nouveau nom de l'entité.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Crée une entité avec un identifiant donné.
     *
     * @param id L'identifiant de l'entité à créer.
     */
    public abstract void createEntity(UUID id);
}
