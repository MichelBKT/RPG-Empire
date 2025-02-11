package com.empire.rpg.entity.player.states;

import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;

// Classe abstraite de base pour les différents états du joueur
public abstract class State {
    protected PlayerCharacter PlayerCharacter; // Référence au joueur associé à cet état

    // Constructeur qui initialise l'état avec une référence au joueur
    public State(PlayerCharacter player) {
        this.PlayerCharacter = player;
    }

    // Méthode appelée lorsqu'on entre dans un état (doit être implémentée par les sous-classes)
    public abstract void enter();

    // Méthode appelée à chaque mise à jour de la boucle de jeu (doit être implémentée par les sous-classes)
    public abstract void update(float deltaTime, CollisionManager collisionManager);

    // Méthode appelée lorsqu'on quitte un état (doit être implémentée par les sous-classes)
    public abstract void exit();
}
