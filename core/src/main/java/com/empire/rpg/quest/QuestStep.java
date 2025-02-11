package com.empire.rpg.quest;

public class QuestStep {
    private int stepNumber;
    private String name;
    private Rewards reward;

    // Constructeur sans argument requis pour la désérialisation
    public QuestStep() {
        // Initialisation par défaut, vous pouvez personnaliser si nécessaire
    }

    // Constructeur avec paramètres
    public QuestStep(int stepNumber, String name, Rewards reward) {
        this.stepNumber = stepNumber;
        this.name = name;
        this.reward = reward;
    }

    // Getters et setters
    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rewards getReward() {
        return reward;
    }

    public void setReward(Rewards reward) {
        this.reward = reward;
    }
}
