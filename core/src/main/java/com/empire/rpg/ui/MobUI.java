package com.empire.rpg.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.component.HealthComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Classe responsable de l'affichage de l'UI des mobs,
 * notamment la barre de vie au-dessus de leur tête.
 */
public class MobUI {
    // Textures pour les éléments UI
    private Texture healthBarBackgroundTexture;
    private Texture healthBarTexture;

    // Variables pour la taille et la position du fond de la barre de vie
    private float backgroundWidth;      // Largeur du fond de la barre de vie
    private float backgroundHeight;     // Hauteur du fond de la barre de vie
    private float backgroundOffsetX;    // Décalage en X par rapport à la position du mob
    private float backgroundOffsetY;    // Décalage en Y par rapport à la position du mob

    // Variables pour la taille et la position de la barre de vie
    private float barWidth;      // Largeur maximale de la barre de vie
    private float barHeight;     // Hauteur de la barre de vie
    private float barOffsetX;    // Décalage en X par rapport à la position du mob
    private float barOffsetY;    // Décalage en Y par rapport à la position du mob

    // Cache pour les textures (pour éviter de les recharger pour chaque mob)
    private static Texture sharedHealthBarBackgroundTexture;
    private static Texture sharedHealthBarTexture;

    /**
     * Constructeur pour initialiser l'UI des mobs avec des valeurs par défaut.
     */
    public MobUI() {
        // Initialiser avec des valeurs par défaut
        // Taille du fond de la barre de vie
        this.backgroundWidth = 35f * 1.5f;     // Largeur du fond de la barre de vie en pixels
        this.backgroundHeight = 11f * 1.5f;    // Hauteur du fond de la barre de vie en pixels
        this.backgroundOffsetX = backgroundWidth / 2f - 18f; // Centrer sur le mob
        this.backgroundOffsetY = 80f;   // Hauteur au-dessus de la tête du mob

        // Taille de la barre de vie
        this.barWidth = 21f * 1.5f;     // Largeur maximale de la barre de vie en pixels
        this.barHeight = 4f * 1.5f;     // Hauteur de la barre de vie en pixels
        this.barOffsetX = backgroundOffsetX / 2f + 20f;    // Décalage par rapport au fond
        this.barOffsetY = 85f;  // Décalage par rapport au fond

        // Charger les textures si elles ne sont pas déjà chargées
        if (sharedHealthBarBackgroundTexture == null) {
            try {
                sharedHealthBarBackgroundTexture = new Texture(Gdx.files.internal("UI/enemy_health.png"));
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de UI/enemy_health.png: " + e.getMessage());
                sharedHealthBarBackgroundTexture = null;
            }
        }

        if (sharedHealthBarTexture == null) {
            try {
                sharedHealthBarTexture = new Texture(Gdx.files.internal("UI/enemy_health_bar.png"));
            } catch (Exception e) {
                System.err.println("Erreur lors du chargement de UI/enemy_health_bar.png: " + e.getMessage());
                sharedHealthBarTexture = null;
            }
        }
    }

    /**
     * Méthode pour dessiner la barre de vie d'un mob.
     *
     * @param batch  SpriteBatch utilisé pour le rendu.
     * @param mob    Le mob pour lequel afficher la barre de vie.
     */
    public void render(SpriteBatch batch, Mob mob) {
        if (sharedHealthBarBackgroundTexture == null || sharedHealthBarTexture == null) {
            // Les textures n'ont pas été chargées correctement
            return;
        }

        // Obtenir la position du mob
        Vector2 mobPosition = mob.getPositionVector();

        // Calculer la position du fond de la barre de vie
        float backgroundX = mobPosition.x + backgroundOffsetX;
        float backgroundY = mobPosition.y + backgroundOffsetY;

        // Calculer la position de la barre de vie
        float barX = mobPosition.x + barOffsetX;
        float barY = mobPosition.y + barOffsetY;

        // Obtenir la santé actuelle et maximale du mob
        HealthComponent health = (HealthComponent) mob.getComponent(HealthComponent.class);
        if (health == null) {
            return;
        }
        int currentHealth = health.getCurrentHealthPoints();
        int maxHealth = health.getMaxHealthPoints();

        // Ne pas afficher la barre de vie si le mob est mort
        if (currentHealth <= 0) {
            return;
        }

        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;

        // Calculer la largeur actuelle de la barre de vie
        float currentBarWidth = barWidth * healthPercentage;

        // Dessiner le fond de la barre de vie
        batch.draw(sharedHealthBarBackgroundTexture, backgroundX, backgroundY, backgroundWidth, backgroundHeight);

        // Dessiner la barre de vie (étirée selon le pourcentage de santé)
        batch.draw(sharedHealthBarTexture, barX, barY, currentBarWidth, barHeight);
    }

    /**
     * Méthode pour libérer les ressources utilisées par l'UI des mobs.
     */
    public void dispose() {
        if (sharedHealthBarBackgroundTexture != null) {
            sharedHealthBarBackgroundTexture.dispose();
            sharedHealthBarBackgroundTexture = null;
        }
        if (sharedHealthBarTexture != null) {
            sharedHealthBarTexture.dispose();
            sharedHealthBarTexture = null;
        }
    }

    // Méthodes pour définir la taille et la position du fond de la barre de vie
    public void setBackgroundSize(float width, float height) {
        this.backgroundWidth = width;
        this.backgroundHeight = height;
    }

    public void setBackgroundOffset(float offsetX, float offsetY) {
        this.backgroundOffsetX = offsetX;
        this.backgroundOffsetY = offsetY;
    }

    // Méthodes pour définir la taille et la position de la barre de vie
    public void setBarSize(float width, float height) {
        this.barWidth = width;
        this.barHeight = height;
    }

    public void setBarOffset(float offsetX, float offsetY) {
        this.barOffsetX = offsetX;
        this.barOffsetY = offsetY;
    }
}
