package com.empire.rpg.entity.player.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Composant représentant l'outil tenu dans la main droite (Tool2)
public class Tool2 {
    private TextureRegion currentFrame; // Frame actuelle à afficher pour l'outil

    // Mettre à jour la frame actuelle de l'outil
    public void update(TextureRegion frame) {
        currentFrame = frame;
    }

    // Rendu de l'outil sur l'écran
    public void render(Batch batch, float x, float y, float scale) {
        if (currentFrame != null) {
            float width = currentFrame.getRegionWidth() * scale;
            float height = currentFrame.getRegionHeight() * scale;
            float adjustedX = x - (width / 2);
            float adjustedY = y - (height / 2);
            batch.draw(currentFrame, adjustedX, adjustedY, width, height);
        }
    }

    // Méthode pour libérer les ressources si nécessaire
    public void dispose() {
        // Rien à libérer ici car nous n'avons pas de ressources propres
    }
}
