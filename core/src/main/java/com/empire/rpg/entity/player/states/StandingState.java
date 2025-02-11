package com.empire.rpg.entity.player.states;

import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;

public class StandingState extends State {

    // Constructeur qui initialise l'état de repos avec une référence au joueur
    public StandingState(PlayerCharacter PlayerCharacter) {
        super(PlayerCharacter);
    }

    // Méthode appelée lorsqu'on entre dans l'état de repos
    @Override
    public void enter() {
        PlayerCharacter.updateAnimationState(); // Met à jour l'animation pour le repos
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (PlayerCharacter.isMoving()) {
            // Si le joueur commence à bouger
            if (PlayerCharacter.isRunning()) {
                // Passer à l'état de course
                PlayerCharacter.changeState(new RunningState(PlayerCharacter));
            } else {
                // Passer à l'état de marche
                PlayerCharacter.changeState(new WalkingState(PlayerCharacter));
            }
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de repos
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
