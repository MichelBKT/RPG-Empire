package com.empire.rpg.entity.mob;

public class MobItem {
    private String name;
    private int quantity;
    private String description;
    private String type;

    public MobItem() {
        // Constructeur par défaut
    }

    // Constructeur
    public MobItem(String name, int quantity, String type, String description, int valeur) {
        this.name = name;
        this.quantity = quantity;
        this.description = description;
        this.type = type;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

