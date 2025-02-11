package com.empire.rpg.entity.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.entity.player.animations.spritesheet.OutfitSpriteSheet;

// Composant représentant la tenue (vêtement) du joueur
public class Outfit {
    private OutfitSpriteSheet spriteSheet; // Spritesheet associée à la tenue
    private TextureRegion currentFrame; // Frame actuelle à afficher

    // Constructeur du composant Outfit
    public Outfit() {
        spriteSheet = new OutfitSpriteSheet();
    }

    // Mettre à jour la frame actuelle de la tenue
    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    // Rendu de la tenue sur l'écran
    public void render(Batch batch, float x, float y, float scale) {
        if (currentFrame != null) {
            float width = currentFrame.getRegionWidth() * scale;
            float height = currentFrame.getRegionHeight() * scale;

            // Ajuster x et y pour centrer le sprite sur la position du joueur
            float adjustedX = x - (width / 2);
            float adjustedY = y - (height / 2);

            batch.draw(currentFrame, adjustedX, adjustedY, width, height);
        }
    }

    // Libérer les ressources associées au spriteSheet
    public void dispose() {
        spriteSheet.dispose();
    }

    // Getter pour le spriteSheet
    public OutfitSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
