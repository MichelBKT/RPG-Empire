package com.empire.rpg.system;

import com.empire.rpg.component.Component;
import com.empire.rpg.component.DefenseComponent;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.component.WeaponComponent;
import com.empire.rpg.entity.Entity;
import com.empire.rpg.entity.MOB;

/**
 * Système de Combat (FightSystem)
 * Utilisé pour gérer les interactions de combat, ce système s'assure que les attaques et les réductions de points de vie
 * sont correctement appliquées. Il s'appuie sur HealthComponent, DefenseComponent, et WeaponComponent pour
 * calculer les dégâts infligés.
 */

public class FightSystem implements GameSystem<Component> {
    private MOB entityTarget;
    private HealthComponent healthComponent;
    private DefenseComponent defenseComponent;
    private WeaponComponent weaponComponent;
    private int damage;
    private int health;

    public FightSystem(MOB entityTarget) {
        this.entityTarget = entityTarget;
        this.healthComponent = (HealthComponent) entityTarget.getComponent(HealthComponent.class);
        this.defenseComponent = (DefenseComponent) entityTarget.getComponent(DefenseComponent.class);
        this.weaponComponent = (WeaponComponent) entityTarget.getComponent(WeaponComponent.class);
        this.damage = 0;
        this.health = 0;
    }

    public Entity getEntityTarget() {
        return entityTarget;
    }

    public void setEntityTarget(MOB entityTarget) {
        this.entityTarget = entityTarget;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    public void setHealthComponent(HealthComponent healthComponent) {
        this.healthComponent = healthComponent;
    }

    public DefenseComponent getDefenseComponent() {
        return defenseComponent;
    }

    public void setDefenseComponent(DefenseComponent defenseComponent) {
        this.defenseComponent = defenseComponent;
    }

    public WeaponComponent getWeaponComponent() {
        return weaponComponent;
    }

    public void setWeaponComponent(WeaponComponent weaponComponent) {
        this.weaponComponent = weaponComponent;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;

    }
    public void attack(Entity target, int damage) {
        // Vérification de la validité de la cible
        if (target == null) {
            System.out.println("Cible invalide");
            return;
        }

        // Vérification de la validité de l'arme
        if (weaponComponent == null) {
            System.out.println("Arme invalide");
            return;
        }

        // Calcul des dégâts

        if (target.getComponent(DefenseComponent.class) != null) {
            setDamage(weaponComponent.getDamage() - target.getComponent(DefenseComponent.class).getDamageReduction());
            healthComponent.setCurrentHealthPoints(healthComponent.getCurrentHealthPoints() - damage);
        } else {
            setDamage(weaponComponent.getDamage());
            healthComponent.setCurrentHealthPoints(healthComponent.getCurrentHealthPoints() - damage);
        }

        // Application des dégâts
        target.getComponent(HealthComponent.class).setCurrentHealthPoints(target.getComponent(HealthComponent.class).getCurrentHealthPoints() - getDamage());

        // Affichage des dégâts infligés
        System.out.println("Dégâts infligés: " + getDamage());

        // Affichage des points de vie restants
        System.out.println("Points de vie restants: " + target.getComponent(HealthComponent.class).getCurrentHealthPoints());
    }

    @Override
    public void update(Component component) {
        attack(entityTarget, damage);

        // Logique de mise à jour du combat

        HealthComponent health = (HealthComponent) entityTarget.getComponent(HealthComponent.class);
        DefenseComponent defense = (DefenseComponent) entityTarget.getComponent(DefenseComponent.class);
        WeaponComponent weapon = (WeaponComponent) entityTarget.getComponent(WeaponComponent.class);

        // Vérifier si les composants sont présents

        if (health != null && defense != null && weapon != null) {
            // Calculer les dégâts infligés
            int damage = weapon.getDamage() - defense.getDamageReduction();

            // Appliquer les dégâts
            health.setCurrentHealthPoints(health.getCurrentHealthPoints() - damage);

            // Afficher les dégâts infligés
            System.out.println("Dégâts infligés: " + damage);

            // Afficher les points de vie restants
            System.out.println("Points de vie restants: " + health.getCurrentHealthPoints());
        }

        // Mettre à jour les dégâts infligés
        setDamage(damage);

        // Mettre à jour les points de vie restants
        assert health != null;
        setHealth(health.getCurrentHealthPoints());

        // Mettre à jour l'entité cible
        setEntityTarget(entityTarget);

        // Mettre à jour les composants
        setHealthComponent(health);
        setDefenseComponent(defense);

    }
}
