package com.empire.rpg.entity.mob.attacks;

/**
 * Classe représentant une attaque effectuée par un Mob.
 */
public class MobAttack {
    private String id;            // Identifiant unique de l'attaque
    private float damage;         // Dégâts infligés par l'attaque
    private float cooldown;       // Temps de recharge de l'attaque en secondes
    private float duration;       // Durée de l'attaque en secondes
    private float range;          // Portée de l'attaque
    private float hitboxWidth;    // Largeur de la hitbox de l'attaque
    private float hitboxHeight;   // Hauteur de la hitbox de l'attaque

    /**
     * Constructeur pour initialiser une attaque de Mob.
     *
     * @param id           Identifiant unique de l'attaque.
     * @param damage       Dégâts infligés par l'attaque.
     * @param cooldown     Temps de recharge de l'attaque en secondes.
     * @param duration     Durée de l'attaque en secondes.
     * @param range        Portée de l'attaque.
     * @param hitboxWidth  Largeur de la hitbox de l'attaque.
     * @param hitboxHeight Hauteur de la hitbox de l'attaque.
     */
    public MobAttack(String id, float damage, float cooldown, float duration, float range, float hitboxWidth, float hitboxHeight) {
        this.id = id;
        this.damage = damage;
        this.cooldown = cooldown;
        this.duration = duration;
        this.range = range;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }

    // Getters pour accéder aux attributs de l'attaque
    public String getId() {
        return id;
    }

    public float getDamage() {
        return damage;
    }

    public float getCooldown() {
        return cooldown;
    }

    public float getDuration() {
        return duration;
    }

    public float getRange() {
        return range;
    }

    public float getHitboxWidth() {
        return hitboxWidth;
    }

    public float getHitboxHeight() {
        return hitboxHeight;
    }

    // Vous pouvez ajouter des méthodes supplémentaires si nécessaire
}
