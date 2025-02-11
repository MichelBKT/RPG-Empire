package com.empire.rpg.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.empire.rpg.component.TextureComponent;
import com.empire.rpg.component.CollisionComponent;
import com.empire.rpg.component.Component;
import com.empire.rpg.component.MovementComponent;
import com.empire.rpg.component.PositionComponent;
import java.util.Map;
import com.badlogic.gdx.math.Rectangle;
import java.util.UUID;
import com.badlogic.gdx.math.Vector2;

/**
 * PNJ (Personnage Non Joueur) est une classe représentant un personnage non joueur dans le jeu.
 */
public class PNJ extends Entity {

    public Vector2 getPosition() {
        PositionComponent positionComponent = (PositionComponent) this.getComponent(PositionComponent.class);
        return new Vector2(positionComponent.getX(), positionComponent.getY());
    }

    /**
     * Ajoute une nouvelle entité PNJ.
     *
     * @return une nouvelle instance de PNJ
     */
    @Override
    public PNJ addEntity() {
        return new PNJ(name, Map.of(
            PositionComponent.class, new PositionComponent(0, 0),
            MovementComponent.class, new MovementComponent(1.5f, "north"),
            CollisionComponent.class, new CollisionComponent(true, new Rectangle(0, 0, 32, 32))
        ), UUID.randomUUID());
    }

    /**
     * Supprime une entité par son nom.
     *
     * @param name le nom de l'entité à supprimer
     * @return l'entité supprimée ou null si non trouvée
     */
    @Override
    public Entity removeEntity(String name) {
        return null;
    }

    /**
     * Constructeur de la classe PNJ.
     *
     * @param name le nom du PNJ
     * @param components les composants associés au PNJ
     * @param id l'identifiant unique du PNJ
     */
    public PNJ(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name, components, id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Crée une entité avec un identifiant donné.
     *
     * @param id l'identifiant unique de l'entité
     */
    @Override
    public void createEntity(UUID id) {

    }

    public void render(SpriteBatch batch) {
        TextureComponent textureComponent = (TextureComponent) components.get(TextureComponent.class);
        PositionComponent positionComponent = (PositionComponent) components.get(PositionComponent.class);

        if (textureComponent != null && positionComponent != null) {
            float scale = textureComponent.getScale();

            batch.draw(
                textureComponent.getCurrentFrame(),
                positionComponent.getX(),
                positionComponent.getY(),
                textureComponent.getCurrentFrame().getRegionWidth() * scale,  // Largeur avec échelle
                textureComponent.getCurrentFrame().getRegionHeight() * scale  // Hauteur avec échelle
            );
        }
    }


}
