package com.empire.rpg.quest;

public class Rewards {
    private int gold;

    // Constructeur sans argument requis pour la désérialisation JSON
    public Rewards() {
    }

    // Constructeur avec arguments pour initialiser xp et gold
    public Rewards(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}
