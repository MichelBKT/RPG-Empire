package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import java.util.Map;
import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.UUID;

/**
 * La classe Player représente une entité joueur dans le jeu RPG.
 * Elle étend la classe Entity et inclut des fonctionnalités supplémentaires
 * spécifiques aux joueurs.
 */
public abstract class Player extends Entity {

    /**
     * Constructeur de l'entité joueur.
     *
     * @param name       le nom du joueur.
     * @param components une map de composants associés au joueur.
     * @param id         l'identifiant unique du joueur.
     */
    public Player(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    /**
     * Méthode abstraite pour ajouter une entité.
     *
     * @return L'entité ajoutée.
     */
    @Override
    public abstract Entity addEntity();

    /**
     * Méthode abstraite pour supprimer une entité par son nom.
     *
     * @param name Le nom de l'entité à supprimer.
     * @return L'entité supprimée.
     */
    @Override
    public abstract Entity removeEntity(String name);

    /**
     * Méthode abstraite pour rendre le joueur.
     *
     * @param batch Le batch pour le rendu.
     */
    public abstract void render(Batch batch);
}
