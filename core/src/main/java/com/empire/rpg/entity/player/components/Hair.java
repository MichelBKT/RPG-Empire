package com.empire.rpg.entity.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.entity.player.animations.spritesheet.HairSpriteSheet;

// Composant représentant les cheveux du joueur
public class Hair {
    private HairSpriteSheet spriteSheet; // Spritesheet associée aux cheveux
    private TextureRegion currentFrame; // Frame actuelle à afficher

    // Constructeur du composant Hair
    public Hair() {
        spriteSheet = new HairSpriteSheet();
    }

    // Mettre à jour la frame actuelle des cheveux
    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    // Rendu des cheveux sur l'écran
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
    public HairSpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
