package com.empire.rpg.entity.player.animations;

// Enumération des différents états d'animation possibles pour le joueur
public enum AnimationState {
    // États de repos
    STANDING_UP,
    STANDING_DOWN,
    STANDING_LEFT,
    STANDING_RIGHT,
    // États de marche
    WALKING_UP,
    WALKING_DOWN,
    WALKING_LEFT,
    WALKING_RIGHT,
    // États de course
    RUNNING_UP,
    RUNNING_DOWN,
    RUNNING_LEFT,
    RUNNING_RIGHT,

    // États d'attaque - Attaque à une main (ONE_SLASH1)
    ONE_SLASH1_UP,
    ONE_SLASH1_DOWN,
    ONE_SLASH1_LEFT,
    ONE_SLASH1_RIGHT,
    // États d'attaque - Deuxième attaque à une main (ONE_SLASH2)
    ONE_SLASH2_UP,
    ONE_SLASH2_DOWN,
    ONE_SLASH2_LEFT,
    ONE_SLASH2_RIGHT,
    // États de défense - Esquive à une main (ONE_DODGE)
    ONE_DODGE_UP,
    ONE_DODGE_DOWN,
    ONE_DODGE_LEFT,
    ONE_DODGE_RIGHT,

    // États d'attaque - Attaque à la lance (POL_SLASH1)
    POL_SLASH1_UP,
    POL_SLASH1_DOWN,
    POL_SLASH1_LEFT,
    POL_SLASH1_RIGHT,

    // États d'attaque - Tir à l'arc (BOW_SHOOT1)
    BOW_SHOOT1_UP,
    BOW_SHOOT1_DOWN,
    BOW_SHOOT1_LEFT,
    BOW_SHOOT1_RIGHT,
}
