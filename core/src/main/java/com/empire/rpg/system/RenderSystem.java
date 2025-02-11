package com.empire.rpg.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.Component;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.component.TextureComponent;
import com.empire.rpg.entity.Entity;


/**
 * Système de Rendu (RenderSystem)
 * Bien que moins directement lié aux interactions, un système de rendu est nécessaire pour afficher graphiquement les
 * entités et leurs états. Ce système s'intègre avec une bibliothèque graphique comme LibGDX.
 */

public class RenderSystem implements GameSystem<Component> {

    private SpriteBatch batch; // Batch qui gère le rendu de plusieurs textures en une fois

    public RenderSystem() {
        this.batch = new SpriteBatch();
    }

    public void render(Entity entity) {
        TextureComponent textureComponent = (TextureComponent) entity.getComponent(TextureComponent.class);
        PositionComponent positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);

        if (textureComponent != null && positionComponent != null) {
            batch.begin();
            batch.draw(textureComponent.getTexture(), positionComponent.getX(), positionComponent.getY());
            batch.end();
        }
    }

    @Override
    public void update(Component component) {
        // Logique de mise à jour du rendu

    }
}
