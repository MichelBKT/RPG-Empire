package com.empire.rpg.entity.player.states;

import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;

public class WalkingState extends State {

    // Constructeur qui initialise l'état de marche avec une référence au joueur
    public WalkingState(PlayerCharacter PlayerCharacter) {
        super(PlayerCharacter);
    }

    // Méthode appelée lorsqu'on entre dans l'état de marche
    @Override
    public void enter() {
        PlayerCharacter.updateAnimationState(); // Met à jour l'animation pour la marche
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (!PlayerCharacter.isMoving()) {
            // Si le joueur s'arrête, passer à l'état de repos
            PlayerCharacter.changeState(new StandingState(PlayerCharacter));
        } else if (PlayerCharacter.isRunning()) {
            // Si le joueur accélère, passer à l'état de course
            PlayerCharacter.changeState(new RunningState(PlayerCharacter));
        } else {
            // Sinon, continuer à bouger et mettre à jour l'animation
            PlayerCharacter.move(deltaTime, collisionManager);
            PlayerCharacter.updateAnimationState();
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de marche
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
