package com.empire.rpg.entity.player.equipment;

import java.util.List;

// Classe représentant un outil équipé par le joueur
public class Tool {
    private String id; // Identifiant de l'outil (ex : "SW01")
    private String name; // Nom de l'outil (ex : "Épée de base")
    private String spritesheetKey; // Clé pour récupérer la spritesheet correspondante
    private List<String> availableAttacks; // Liste des IDs d'attaques disponibles avec cet outil

    // Constructeur de l'outil
    public Tool(String id, String name, String spritesheetKey, List<String> availableAttacks) {
        this.id = id;
        this.name = name;
        this.spritesheetKey = spritesheetKey;
        this.availableAttacks = availableAttacks;
    }

    // Getters pour accéder aux attributs de l'outil
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpritesheetKey() { return spritesheetKey; }
    public List<String> getAvailableAttacks() { return availableAttacks;}

    // Setters pour modifier les attributs de l'outil
    public void setId(String id) { this.id = id;}
    public void setName(String name) { this.name = name; }
    public void setSpritesheetKey(String spritesheetKey) { this.spritesheetKey = spritesheetKey; }
    public void setAvailableAttacks(List<String> availableAttacks) { this.availableAttacks = availableAttacks; }
}
