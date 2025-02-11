package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import java.util.Map;
import java.util.UUID;

/**
 * Classe abstraite représentant une entité dans le jeu.
 * Une entité est identifiée de manière unique par un UUID et contient une collection de composants.
 */
public abstract class MOB extends Entity {
    /**
     * Constructeur de l'entité.
     *
     * @param name       Le nom de l'entité.
     * @param components La map des composants de l'entité.
     * @param id         L'identifiant unique de l'entité.
     */
    public MOB(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Ajouter une entité.
     *
     * @return L'entité ajoutée.
     */
    @Override
    public Entity addEntity() {
        return this;
    }

    /**
     * Supprimer une entité par son nom.
     *
     * @param name Le nom de l'entité à supprimer.
     * @return L'entité supprimée.
     */
    @Override
    public Entity removeEntity(String name) {
        if (this.getName().equals(name)) {
            this.removeComponents();
            return this;
        }
        return null;
    }
}
