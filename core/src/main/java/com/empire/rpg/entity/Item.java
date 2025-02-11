package com.empire.rpg.entity;

import com.badlogic.gdx.math.Rectangle;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Représente un item dans le jeu.
 */
public class Item extends Entity {
    private String type;
    private int quantity;
    private String name;
    private String description;
    private int valeur;
    private boolean states;
    private String style;

    /**
     * Constructeur d'un item avec un nom, une quantité et un type.
     *
     * @param name     Le nom de l'item
     * @param quantity La quantité de l'item
     * @param type     La catégorie d'item
     */
    public Item(String name, int quantity, String type, String description, int valeur,boolean states, String style) {
        super(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            CollisionComponent.class, new CollisionComponent(true, new Rectangle(0, 0, 32, 32))
        ), UUID.randomUUID());

        this.name = name;
        this.type= type;
        this.quantity = quantity;
        this.description = description;
        this.valeur = valeur;
        this.states = states;
        this.style = style;
    }

    /**
     * Récupérer le nom de l'item.
     *
     * @return Le nom de l'item
     */
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * Affecter un nouveau nom à l'item.
     *
     * @param name le nouveau nom de l'item.
     */
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    /**
     * Récupérer la catégorie d'item.
     *
     * @return La catégorie de l'item
     */
    public String getType() {
        return type;
    }

    /**
     * Affecter une nouvelle catégorie à l'item.
     *
     * @param type La nouvelle catégorie de l'item.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Récupérer la quantité de l'item.
     *
     * @return La quantité de l'item
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Affecter une nouvelle quantité à l'item.
     *
     * @param quantity La nouvelle quantité de l'item
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Ajouter une entité item dans le jeu.
     *
     * @return null (non implémenté)
     */
    @Override
    public Entity addEntity() {
        // Implémentez cette méthode selon les besoins de votre jeu
        return null;
    }

    /**
     * Supprimer un item dans le jeu par son nom.
     *
     * @param name le nom de l'item à supprimer
     * @return null (non implémenté)
     */
    @Override
    public Entity removeEntity(String name) {
        // Implémentez cette méthode selon les besoins de votre jeu
        return null;
    }

    public boolean getStates() {
        return states;
    }

    public void setStates(boolean states) {
        this.states = states;
    }

    public String getDescription() {
        return description;
    }

    public int getValeur() {
        return valeur;
    }

    public String getStyle() {
        return style;
    }

}
