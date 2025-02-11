package com.empire.rpg.quest;

import java.util.List;
import com.empire.rpg.quest.Rewards;


public class QuestItem {
    private String title;
    private String description;
    private int id;
    private boolean unique;
    private int status = 0;
    private String author;
    private Rewards reward;
    private int type;
    private List<QuestStep> steps; // Liste des étapes

    // Constructeur par défaut
    public QuestItem() {
        this.status = 0; // Définir explicitement le status à 0
    }

    // Constructeur avec tous les paramètres
    public QuestItem(String title, String description, int id, boolean unique, int status, String author, Rewards reward, int type, List<QuestStep> steps) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.unique = unique;
        this.status = status;
        this.author = author;
        this.reward = reward;
        this.type = type;
        this.steps = steps; // Initialisation de la liste des étapes
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public boolean isUnique() {
        return unique;
    }

    public int getStatus() {
        return status;
    }

    public String getAuthor() {
        return author;
    }

    public Rewards getReward() {
        return reward;
    }

    public int getType() {
        return type;
    }

    public List<QuestStep> getSteps() {
        return steps; // Retourne la liste des étapes
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReward(Rewards reward) {
        this.reward = reward;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setSteps(List<QuestStep> steps) {
        this.steps = steps; // Permet de définir la liste des étapes
    }
}
