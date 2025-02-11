package com.empire.rpg.entity.player.states;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.attacks.Attack;
import com.empire.rpg.entity.player.equipment.Tool;
import com.empire.rpg.entity.player.animations.AnimationState;
import com.empire.rpg.entity.player.animations.CustomAnimation;
import com.empire.rpg.component.HealthComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * État du joueur lorsqu'il effectue une attaque.
 */
public class AttackingState extends State {
    private Attack attack; // Attaque en cours
    private Tool tool; // Outil utilisé pour l'attaque
    private float elapsedTime; // Temps écoulé depuis le début de l'attaque
    private Polygon attackHitbox; // Hitbox de l'attaque
    private Set<Mob> alreadyHitMobs; // Mobs déjà touchés par cette attaque
    private List<Mob> mobsToRemove; // Liste des mobs à supprimer après l'itération

    // Constructeur de l'état d'attaque
    public AttackingState(PlayerCharacter playerCharacter, Attack attack, Tool tool) {
        super(playerCharacter);
        this.attack = attack;
        this.tool = tool;
        this.elapsedTime = 0f;
        this.alreadyHitMobs = new HashSet<>();
        this.mobsToRemove = new ArrayList<>();
    }

    @Override
    public void enter() {
        // Déterminer la direction du joueur pour sélectionner l'animation appropriée
        String direction = PlayerCharacter.getFacingDirection();
        AnimationState animationState = attack.getAnimationStates().get(direction);

        if (animationState != null) {
            // Récupérer les clés de spritesheet pour les outils équipés
            String tool1SpritesheetKey = PlayerCharacter.getCurrentTool1() != null ? PlayerCharacter.getCurrentTool1().getSpritesheetKey() : null;
            String tool2SpritesheetKey = PlayerCharacter.getCurrentTool2() != null ? PlayerCharacter.getCurrentTool2().getSpritesheetKey() : null;

            // Créer l'animation d'attaque personnalisée
            CustomAnimation attackAnimation = PlayerCharacter.getAnimationController().createAttackAnimation(
                attack.getCategoryKey(), tool1SpritesheetKey, tool2SpritesheetKey, animationState);
            PlayerCharacter.getAnimationController().setCustomAnimation(attackAnimation);
        }

        // Jouer le son de l'attaque
        attack.playSound();

        // Calculer la hitbox de l'attaque
        calculateAttackHitbox();
    }

    @Override
    public void update(float deltaTime, CollisionManager collisionManager) {
        elapsedTime += deltaTime;
        if (elapsedTime >= attack.getDuration()) {
            // Si l'attaque est terminée
            if (!PlayerCharacter.getAttackQueue().isEmpty()) {
                // Démarrer l'attaque suivante dans la file d'attente
                PlayerCharacter.startNextAttack();
            } else {
                // Retourner à l'état debout et réinitialiser le combo
                PlayerCharacter.changeState(new StandingState(PlayerCharacter));
                PlayerCharacter.resetCombo();
            }
        }

        // Détecter les collisions entre la hitbox de l'attaque et les mobs
        detectAndApplyDamage(collisionManager);

        // Supprimer les mobs morts après l'itération
        removeDeadMobs();
    }

    @Override
    public void exit() {
        // Actions à effectuer lors de la sortie de l'état d'attaque
        PlayerCharacter.getAnimationController().setCustomAnimation(null);
        attackHitbox = null; // Réinitialiser la hitbox
        alreadyHitMobs.clear(); // Réinitialiser les mobs déjà touchés
        mobsToRemove.clear(); // Réinitialiser la liste des mobs à supprimer
    }

    /**
     * Calculer la hitbox de l'attaque en fonction de la direction du joueur.
     */
    private void calculateAttackHitbox() {
        float hitboxWidth = attack.getHitboxWidth();
        float hitboxHeight = attack.getHitboxHeight();

        // Points de la hitbox avant transformation
        float[] vertices = new float[]{
            -hitboxWidth / 2, 0,                  // Point inférieur gauche
            hitboxWidth / 2, 0,                   // Point inférieur droit
            hitboxWidth / 2, hitboxHeight,        // Point supérieur droit
            -hitboxWidth / 2, hitboxHeight        // Point supérieur gauche
        };

        attackHitbox = new Polygon(vertices);

        // Positionner le polygone devant le joueur
        float playerCenterX = PlayerCharacter.getCollisionBounds().x + PlayerCharacter.getCollisionBounds().width / 2;
        float playerCenterY = PlayerCharacter.getCollisionBounds().y + PlayerCharacter.getCollisionBounds().height / 2;

        // Déplacer la hitbox au centre du joueur
        attackHitbox.setPosition(playerCenterX, playerCenterY);

        // Tourner et déplacer la hitbox en fonction de la direction
        String direction = PlayerCharacter.getFacingDirection();
        float rotation = 0f;
        float offsetX = 0f;
        float offsetY = 0f;

        switch (direction) {
            case "UP":
                rotation = 0f;
                offsetY = PlayerCharacter.getCollisionBounds().height / 2;
                break;
            case "DOWN":
                rotation = 180f;
                offsetY = -PlayerCharacter.getCollisionBounds().height / 2;
                break;
            case "LEFT":
                rotation = 90f;
                offsetX = -PlayerCharacter.getCollisionBounds().width / 2;
                break;
            case "RIGHT":
                rotation = -90f;
                offsetX = PlayerCharacter.getCollisionBounds().width / 2;
                break;
        }

        // Appliquer la rotation
        attackHitbox.setRotation(rotation);

        // Déplacer la hitbox devant le joueur
        attackHitbox.translate(offsetX, offsetY);
    }

    /**
     * Détecter les collisions entre la hitbox de l'attaque et les mobs, et appliquer les dégâts.
     *
     * @param collisionManager Le gestionnaire de collisions.
     */
    private void detectAndApplyDamage(CollisionManager collisionManager) {
        if (attackHitbox == null) {
            return;
        }

        for (Mob mob : Mob.allMobs) {
            // Éviter de traiter le même mob plusieurs fois
            if (alreadyHitMobs.contains(mob)) {
                continue;
            }

            Rectangle mobBounds = mob.getCollisionBounds();
            Polygon mobPolygon = rectangleToPolygon(mobBounds);

            // Utiliser Intersector pour détecter la collision entre deux polygones
            if (Intersector.overlapConvexPolygons(attackHitbox, mobPolygon)) {
                // Appliquer les dégâts au mob
                mob.takeDamage(attack.getDamage());

                // Marquer le mob comme déjà touché pour cette attaque
                alreadyHitMobs.add(mob);

                // Vérifier si le mob est mort pour le supprimer ultérieurement
                HealthComponent health = (HealthComponent) mob.getComponent(HealthComponent.class);
                if (health != null && health.getCurrentHealthPoints() <= 0) {
                    mobsToRemove.add(mob);
                }
            }
        }
    }

    /**
     * Convertit un Rectangle en Polygon.
     *
     * @param rect Le rectangle à convertir.
     * @return Le polygone équivalent.
     */
    private Polygon rectangleToPolygon(Rectangle rect) {
        float[] vertices = new float[]{
            rect.x, rect.y,
            rect.x + rect.width, rect.y,
            rect.x + rect.width, rect.y + rect.height,
            rect.x, rect.y + rect.height
        };
        return new Polygon(vertices);
    }

    /**
     * Supprime les mobs morts de la liste `Mob.allMobs`.
     */
    private void removeDeadMobs() {
        for (Mob mob : mobsToRemove) {
            // L'appel à printDeathInfo() est déjà fait dans onDeath(), donc pas besoin de le rappeler ici
            Mob.allMobs.remove(mob);
            // Optionnel : Ajouter des effets de mort, des animations, etc.
            mob.dispose();
            // System.out.println(mob.getName() + " a été supprimé du jeu.");
        }
        mobsToRemove.clear();
    }

    // Getter pour la hitbox de l'attaque
    public Polygon getAttackHitbox() {
        return attackHitbox;
    }

    // Getter pour l'attaque en cours
    public Attack getAttack() {
        return attack;
    }
}
