package com.empire.rpg.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.map.ZoneManager;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class ZoneUI {
    private Texture bannerTexture;
    private BitmapFont font;

    // Variables pour la taille et la position de la bannière
    private float bannerWidth;
    private float bannerHeight;
    private float bannerX;
    private float bannerY;

    // Dimensions virtuelles de l'UI
    private float uiWidth;
    private float uiHeight;

    // Joueur et ZoneManager pour obtenir la zone actuelle
    private PlayerCharacter player;
    private ZoneManager zoneManager;

    // Nom de la zone actuelle
    private String currentZoneName;

    // Gestion du temps d'affichage
    private float displayTime = 0f;
    private float maxDisplayTime = 3f; // Afficher pendant 3 secondes

    // Facteur de mise à l'échelle du texte
    private float textScale;

    public ZoneUI(PlayerCharacter player, ZoneManager zoneManager, float uiWidth, float uiHeight) {
        this.player = player;
        this.zoneManager = zoneManager;
        this.uiWidth = uiWidth;
        this.uiHeight = uiHeight;

        // Charger la texture de la bannière
        try {
            bannerTexture = new Texture(Gdx.files.internal("UI/banner_hud.png"));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de UI/banner_hud.png: " + e.getMessage());
            bannerTexture = null;
        }

        // Initialiser le BitmapFont pour le texte
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        // Initialiser le facteur de mise à l'échelle du texte
        textScale = 2.0f; // Échelle par défaut

        // Définir les dimensions et la position de la bannière
        initializeBannerPosition();
    }

    private void initializeBannerPosition() {
        // Dimensions de la texture originale
        float originalBannerWidth = 192f;
        float originalBannerHeight = 32f;

        // Facteur d'échelle
        float scaleFactor = 3.5f;

        // Taille de la bannière après mise à l'échelle
        bannerWidth = originalBannerWidth * scaleFactor;
        bannerHeight = originalBannerHeight * scaleFactor;

        // Position de la bannière
        bannerX = uiWidth - bannerWidth - 80f; // Ajuster si nécessaire
        bannerY = uiHeight - bannerHeight;     // Ajuster si nécessaire
    }

    public void update() {
        // Obtenir la position du joueur
        float playerX = player.getX();
        float playerY = player.getY();

        // Obtenir le nom de la zone actuelle
        String zoneName = zoneManager.getZoneNameAt(playerX, playerY);

        // Mettre à jour le nom de la zone si nécessaire
        if (zoneName != null && !zoneName.equals(currentZoneName)) {
            currentZoneName = zoneName;
            displayTime = 0f; // Réinitialiser le timer
        } else if (zoneName == null) {
            currentZoneName = "";
            displayTime = 0f;
        }

        // Mettre à jour le timer
        if (!currentZoneName.isEmpty()) {
            displayTime += Gdx.graphics.getDeltaTime();
            if (displayTime > maxDisplayTime) {
                currentZoneName = "";
                displayTime = 0f;
            }
        }
    }

    public void render(SpriteBatch batch) {
        if (currentZoneName.isEmpty() || bannerTexture == null) {
            return; // Ne rien afficher si aucun nom de zone ou texture non chargée
        }

        // Dessiner la bannière
        batch.draw(bannerTexture, bannerX, bannerY, bannerWidth, bannerHeight);

        // Définir l'échelle de la police
        font.getData().setScale(textScale);

        // Utiliser GlyphLayout pour mesurer le texte
        GlyphLayout layout = new GlyphLayout(font, currentZoneName);

        // Calculer la position du texte pour le centrer dans la bannière
        float textX = bannerX + (bannerWidth - layout.width) / 2f;
        float textY = bannerY + (bannerHeight + layout.height) / 2f;

        font.draw(batch, layout, textX, textY);
    }

    public void dispose() {
        if (bannerTexture != null) {
            bannerTexture.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }

    // Méthodes pour ajuster la taille et la position de la bannière
    public void setBannerSize(float width, float height) {
        this.bannerWidth = width;
        this.bannerHeight = height;
        initializeBannerPosition();
    }

    public void setBannerPosition(float x, float y) {
        this.bannerX = x;
        this.bannerY = y;
    }

    // Méthodes pour ajuster le style du texte
    public void setFont(BitmapFont font) {
        if (this.font != null) {
            this.font.dispose();
        }
        this.font = font;
    }

    public void setFontColor(Color color) {
        font.setColor(color);
    }

    // Méthode pour définir le facteur de mise à l'échelle du texte
    public void setTextScale(float scale) {
        this.textScale = scale;
        font.getData().setScale(scale);
    }

    // Méthode pour ajuster le temps d'affichage de la bannière
    public void setMaxDisplayTime(float seconds) {
        this.maxDisplayTime = seconds;
    }
}
