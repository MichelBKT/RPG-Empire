package com.empire.rpg.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.entity.player.PlayerCharacter;

import com.empire.rpg.entity.player.equipment.Tool;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsable de l'affichage de l'interface utilisateur du joueur,
 * notamment la barre de santé et l'icône de l'arme équipée.
 */
public class PlayerUI {
    // Textures pour les éléments UI
    private Texture playerStatusTexture;
    private Texture playerHealthBarTexture;

    // Variables de position et de taille pour player_status.png
    private float statusX;
    private float statusY;
    private float statusWidth;
    private float statusHeight;

    // Variables de position et de taille pour player_health_bar.png
    private float healthBarX;
    private float healthBarY;
    private float healthBarFullWidth;
    private float healthBarHeight;

    // Variables de position et de taille pour l'icône de l'arme
    private float weaponIconX;
    private float weaponIconY;
    private float weaponIconWidth;
    private float weaponIconHeight;

    // Référence au joueur
    private final PlayerCharacter player;

    // Dimensions virtuelles de l'UI
    private float uiWidth;
    private float uiHeight;

    // Cache pour les textures des icônes d'armes
    private Map<String, Texture> toolIcons;

    /**
     * Constructeur pour initialiser l'UI du joueur.
     *
     * @param playerInstance Instance du joueur pour accéder à sa santé et à ses outils.
     */
    public PlayerUI(PlayerCharacter playerInstance, float uiWidth, float uiHeight) {
        this.player = playerInstance;
        this.uiWidth = uiWidth;
        this.uiHeight = uiHeight;

        // Initialiser le cache des icônes d'armes
        this.toolIcons = new HashMap<>();

        // Charger les textures
        try {
            this.playerStatusTexture = new Texture(Gdx.files.internal("UI/player_status.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de player_status.png: " + e.getMessage());
            this.playerStatusTexture = null;
        }

        try {
            this.playerHealthBarTexture = new Texture(Gdx.files.internal("UI/player_health_bar.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de player_health_bar.png: " + e.getMessage());
            this.playerHealthBarTexture = null;
        }

        // Initialisation des variables de position et de taille
        initializePositions();
    }

    private void initializePositions() {
        // Dimensions des textures originales
        float originalStatusWidth = 83f;
        float originalStatusHeight = 35f;
        float originalHealthBarWidth = 45f;
        float originalHealthBarHeight = 5f;
        float originalWeaponIconSize = 16f;

        // Facteur d'échelle pour agrandir les textures
        float scaleFactor = 4f;

        // Taille des éléments UI après mise à l'échelle
        statusWidth = originalStatusWidth * scaleFactor;
        statusHeight = originalStatusHeight * scaleFactor;
        healthBarFullWidth = originalHealthBarWidth * scaleFactor;
        healthBarHeight = originalHealthBarHeight * scaleFactor;
        weaponIconWidth = originalWeaponIconSize * scaleFactor * 0.75f; // Ajuster si nécessaire
        weaponIconHeight = originalWeaponIconSize * scaleFactor * 0.75f;

        // Positions des éléments UI
        statusX = 0f; // Position X fixe à gauche
        statusY = uiHeight - statusHeight; // Position Y en haut

        healthBarX = statusX + statusWidth / 2f - 34f; // Ajuster en fonction du design
        healthBarY = statusY + statusHeight / 2f + 2f; // Ajuster en fonction du design

        weaponIconX = statusX + 46f; // Ajuster en fonction du design
        weaponIconY = statusY + 56f; // Ajuster en fonction du design
    }

    /**
     * Méthode pour dessiner l'UI du joueur.
     *
     * @param batch SpriteBatch utilisé pour le rendu.
     */
    public void render(SpriteBatch batch) {
        // Obtenir la santé actuelle et maximale du joueur
        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
        if (health == null) {
            System.out.println("HealthComponent missing");
            return;
        }
        int currentHealth = health.getCurrentHealthPoints();
        int maxHealth = health.getMaxHealthPoints();

        // Calculer le pourcentage de santé
        float healthPercentage = (float) currentHealth / maxHealth;

        // Calculer la largeur de la barre de santé en fonction de la santé actuelle
        float currentHealthBarWidth = healthBarFullWidth * healthPercentage;

        // Dessiner la barre de santé
        if (playerHealthBarTexture != null) {
            batch.draw(playerHealthBarTexture, healthBarX, healthBarY, currentHealthBarWidth, healthBarHeight);
        }

        // Dessiner le cadre de la barre de santé
        if (playerStatusTexture != null) {
            batch.draw(playerStatusTexture, statusX, statusY, statusWidth, statusHeight);
        }

        // Déterminer quelle icône d'arme afficher
        String toolIdToDisplay = null;

        // Récupérer les outils équipés
        Tool currentTool1 = player.getCurrentTool1();
        Tool currentTool2 = player.getCurrentTool2();

        if (currentTool1 != null) {
            toolIdToDisplay = currentTool1.getId();
        } else if (currentTool2 != null) {
            toolIdToDisplay = currentTool2.getId();
        }

        // Charger et récupérer la texture de l'icône de l'arme
        if (toolIdToDisplay != null && !toolIdToDisplay.isEmpty()) {
            Texture weaponTexture = toolIcons.get(toolIdToDisplay);
            if (weaponTexture == null) {
                // Charger la texture si elle n'est pas déjà chargée
                String weaponIconPath = "UI/icon/weapon/" + toolIdToDisplay + "-icon.png";
                try {
                    weaponTexture = new Texture(Gdx.files.internal(weaponIconPath));
                    toolIcons.put(toolIdToDisplay, weaponTexture);
                } catch (Exception e) {
                    System.err.println("Erreur lors du chargement de " + weaponIconPath + ": " + e.getMessage());
                    weaponTexture = null;
                }
            }

            // Dessiner l'icône de l'arme si la texture est chargée
            if (weaponTexture != null) {
                batch.draw(weaponTexture, weaponIconX, weaponIconY, weaponIconWidth, weaponIconHeight);
            }
        }
    }

    /**
     * Méthode pour libérer les ressources utilisées par l'UI.
     */
    public void dispose() {
        if (playerStatusTexture != null) {
            playerStatusTexture.dispose();
        }
        if (playerHealthBarTexture != null) {
            playerHealthBarTexture.dispose();
        }
        // Disposer toutes les textures d'icônes d'armes
        for (Texture texture : toolIcons.values()) {
            if (texture != null) {
                texture.dispose();
            }
        }
        toolIcons.clear();
    }
}
