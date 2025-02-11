package com.empire.rpg.entity.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.entity.player.animations.spritesheet.BodySpriteSheet;

// Composant représentant le corps du joueur
public class Body {
    private BodySpriteSheet spriteSheet; // Spritesheet associée au corps
    private TextureRegion currentFrame; // Frame actuelle à afficher

    // Constructeur du composant Body
    public Body() {
        spriteSheet = new BodySpriteSheet();
    }

    // Mettre à jour la frame actuelle du corps
    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    // Rendu du corps sur l'écran
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
    public BodySpriteSheet getSpriteSheet() {
        return spriteSheet;
    }
}
