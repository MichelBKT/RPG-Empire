package com.empire.rpg.entity.player.states;

import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.player.PlayerCharacter;

public class RunningState extends State {

    // Constructeur qui initialise l'état de course avec une référence au joueur
    public RunningState(PlayerCharacter PlayerCharacter) {
        super(PlayerCharacter);
    }

    // Méthode appelée lorsqu'on entre dans l'état de course
    @Override
    public void enter() {
        PlayerCharacter.updateAnimationState(); // Met à jour l'animation pour la course
    }

    // Méthode appelée à chaque mise à jour de la boucle de jeu
    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        if (!PlayerCharacter.isMoving()) {
            // Si le joueur ne bouge plus, on le remet à l'état de repos
            PlayerCharacter.changeState(new StandingState(PlayerCharacter));
        } else if (!PlayerCharacter.isRunning()) {
            // Si le joueur ralentit, on passe à l'état de marche
            PlayerCharacter.changeState(new WalkingState(PlayerCharacter));
        } else {
            // Sinon, le joueur continue à se déplacer et on met à jour l'animation
            PlayerCharacter.move(deltaTime, collisionManager);
            PlayerCharacter.updateAnimationState();
        }
    }

    // Méthode appelée lorsqu'on quitte l'état de course
    @Override
    public void exit() {
        // Rien à nettoyer ici pour l'instant
    }
}
