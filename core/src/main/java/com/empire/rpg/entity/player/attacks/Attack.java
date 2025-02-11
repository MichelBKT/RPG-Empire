package com.empire.rpg.entity.player.attacks;

import com.empire.rpg.entity.player.animations.AnimationState;
import com.empire.rpg.entity.player.audio.SoundManager;

import java.util.Map;

// Classe représentant une attaque avec ses caractéristiques
public class Attack {
    private String id; // Identifiant unique de l'attaque (ex : "ONE_SLASH1")
    private String name; // Nom de l'attaque (ex : "Fente tranchante")
    private String categoryKey; // Clé de catégorie pour les spritesheets (ex : "ONE3")
    private Map<String, AnimationState> animationStates; // États d'animation associés en fonction de la direction
    private float duration; // Durée de l'attaque
    private float damage; // Dégâts infligés par l'attaque
    private float cooldown; // Temps de recharge de l'attaque
    private float hitboxWidth; // Largeur de la hitbox de l'attaque
    private float hitboxHeight; // Hauteur de la hitbox de l'attaque

    // Constructeur de l'attaque
    public Attack(String id, String name, String categoryKey, Map<String, AnimationState> animationStates, float duration, float damage, float cooldown, float hitboxWidth, float hitboxHeight) {
        this.id = id;
        this.name = name;
        this.categoryKey = categoryKey;
        this.animationStates = animationStates;
        this.duration = duration;
        this.damage = damage;
        this.cooldown = cooldown;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }

    // Getters pour accéder aux attributs de l'attaque
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getCategoryKey() {
        return categoryKey;
    }
    public Map<String, AnimationState> getAnimationStates() {
        return animationStates;
    }
    public float getDuration() {
        return duration;
    }
    public float getDamage() {
        return damage;
    }
    public float getCooldown() {
        return cooldown;
    }
    public float getHitboxWidth() {
        return hitboxWidth;
    }
    public float getHitboxHeight() {
        return hitboxHeight;
    }

    // Méthode pour jouer le son associé à l'attaque
    public void playSound() {
        SoundManager.playSound(this.id);
    }
}
