package com.empire.rpg.entity;

import java.util.UUID;

/**
 * Classe abstraite EntityFactory pour créer des entités.
 */

public abstract class EntityFactory {

    /**
     * Créer une entité avec un UUID bien spécifique.
     *
     * @param id UUID de l'entité à créer
     */

    public abstract void createEntity(UUID id);

}
