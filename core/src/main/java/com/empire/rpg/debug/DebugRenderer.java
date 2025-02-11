package com.empire.rpg.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.component.HealthComponent;
import com.empire.rpg.entity.mob.Mob;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.states.AttackingState;
import com.empire.rpg.entity.player.attacks.Attack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class DebugRenderer {
    // Définition des constantes de couleur
    private static final Color COLLISION_COLOR = Color.RED;
    private static final Color DIRECTION_COLOR = Color.YELLOW;
    private static final Color ATTACK_ZONE_COLOR = Color.ORANGE;
    private static final Color TEXT_COLOR = Color.WHITE;

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final SpriteBatch spriteBatch;
    private final GlyphLayout glyphLayout;
    private static final float DIRECTION_LINE_LENGTH = 50f;

    public DebugRenderer() {
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.spriteBatch = new SpriteBatch();
        this.font.setColor(TEXT_COLOR); // Définir la couleur par défaut du texte en blanc
        this.glyphLayout = new GlyphLayout();
    }

    /**
     * Méthode principale pour rendre les éléments de débogage.
     *
     * @param camera           La caméra du jeu.
     * @param player           Le joueur dont les informations de débogage sont affichées.
     * @param collisionManager Le gestionnaire de collisions contenant les objets de collision de la carte.
     */
    public void renderDebugBounds(Camera camera, PlayerCharacter player, CollisionManager collisionManager) {
        // Gestion des entrées pour ajouter/soustraire des points de vie
        handleHealthModification(player);

        // Configurer ShapeRenderer pour le dessin des formes
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Dessiner la boîte de collision du joueur
        drawCollisionBounds(player.getCollisionBounds());

        // Dessiner la ligne de direction du joueur
        Rectangle playerBounds = player.getCollisionBounds();
        float playerCenterX = playerBounds.x + playerBounds.width / 2;
        float playerCenterY = playerBounds.y + playerBounds.height / 2;
        drawDirectionVector(playerCenterX, playerCenterY, player.getDirection());

        // Dessiner les objets de collision de la carte
        drawMapCollisionObjects(collisionManager);

        // Dessiner la zone d'effet de l'attaque si le joueur est en train d'attaquer
        drawPlayerAttackHitbox(player);

        // Dessiner les collisions et directions des mobs
        drawMobsCollisionAndDirection();

        shapeRenderer.end();

        // Dessiner les textes d'informations
        drawInfoTexts(camera, player);

        // Dessiner les attaques des mobs
        for (Mob mob : Mob.allMobs) {
            renderAttackHitbox(mob, camera);
        }
    }

    /**
     * Dessine la boîte de collision d'une entité.
     *
     * @param bounds Les dimensions de la boîte de collision.
     */
    private void drawCollisionBounds(Rectangle bounds) {
        shapeRenderer.setColor(COLLISION_COLOR);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    /**
     * Dessine le vecteur de direction d'une entité.
     *
     * @param centerX La position centrale X de l'entité.
     * @param centerY La position centrale Y de l'entité.
     * @param direction Le vecteur de direction à dessiner.
     */
    private void drawDirectionVector(float centerX, float centerY, Vector2 direction) {
        shapeRenderer.setColor(DIRECTION_COLOR);
        shapeRenderer.line(
            centerX, centerY,
            centerX + direction.x * DIRECTION_LINE_LENGTH,
            centerY + direction.y * DIRECTION_LINE_LENGTH
        );
    }

    /**
     * Dessine la zone d'effet de l'attaque du joueur s'il est en train d'attaquer.
     *
     * @param player Le joueur dont la zone d'attaque est dessinée.
     */
    private void drawPlayerAttackHitbox(PlayerCharacter player) {
        if (player.getCurrentState() instanceof AttackingState) {
            AttackingState attackingState = (AttackingState) player.getCurrentState();
            Polygon attackHitbox = attackingState.getAttackHitbox();
            if (attackHitbox != null) {
                shapeRenderer.setColor(ATTACK_ZONE_COLOR);
                shapeRenderer.polygon(attackHitbox.getTransformedVertices());
            }
        }
    }

    /**
     * Dessine les objets de collision de la carte.
     *
     * @param collisionManager Le gestionnaire de collisions contenant les objets de collision.
     */
    private void drawMapCollisionObjects(CollisionManager collisionManager) {
        // Dessiner les rectangles de collision
        Array<Rectangle> collisionRectangles = collisionManager.getCollisionRectangles();
        for (Rectangle rect : collisionRectangles) {
            drawCollisionBounds(rect);
        }

        // Dessiner les ellipses de collision
        Array<Ellipse> collisionEllipses = collisionManager.getCollisionEllipses();
        for (Ellipse ellipse : collisionEllipses) {
            shapeRenderer.setColor(COLLISION_COLOR);
            shapeRenderer.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height);
        }

        // Dessiner les polygones de collision
        Array<Polygon> collisionPolygons = collisionManager.getCollisionPolygons();
        for (Polygon polygon : collisionPolygons) {
            shapeRenderer.setColor(COLLISION_COLOR);
            shapeRenderer.polygon(polygon.getTransformedVertices());
        }
    }

    /**
     * Dessine les boîtes de collision et les vecteurs de direction de tous les mobs.
     */
    private void drawMobsCollisionAndDirection() {
        // Parcourir tous les mobs existants
        for (Mob mob : Mob.allMobs) {
            // Dessiner la boîte de collision du mob
            Rectangle mobBounds = mob.getCollisionBounds();
            drawCollisionBounds(mobBounds);

            // Dessiner la ligne de direction du mob
            float mobCenterX = mobBounds.x + mobBounds.width / 2;
            float mobCenterY = mobBounds.y + mobBounds.height / 2;
            drawDirectionVector(mobCenterX, mobCenterY, mob.getDirection());
        }
    }

    /**
     * Dessine les textes d'informations tels que les coordonnées, le nom, la santé du joueur,
     * et les informations sur l'attaque en cours.
     *
     * @param camera La caméra du jeu.
     * @param player Le joueur dont les informations sont affichées.
     */
    private void drawInfoTexts(Camera camera, PlayerCharacter player) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // Dessiner les coordonnées X et Y du joueur
        Rectangle playerBounds = player.getCollisionBounds();
        float playerTextX = playerBounds.x + playerBounds.width / 2;
        float playerTextY = playerBounds.y + playerBounds.height + 15; // Position légèrement au-dessus du joueur
        drawCenteredText("X: " + (int) playerBounds.x + " Y: " + (int) playerBounds.y, playerTextX, playerTextY);

        // Dessiner le nom du joueur
        String playerName = player.getName();
        drawCenteredText("Name: " + playerName, playerTextX, playerTextY + 15 + glyphLayout.height);

        // Dessiner la santé du joueur
        int currentHealth = player.getHealth();
        int maxHealth = player.getMaxHealth();
        String healthText = "Health: " + currentHealth + "/" + maxHealth;
        drawCenteredText(healthText, playerTextX, playerTextY + 30 + glyphLayout.height);

        // Dessiner le texte sur la hitbox de l'attaque
        if (player.getCurrentState() instanceof AttackingState) {
            AttackingState attackingState = (AttackingState) player.getCurrentState();
            Polygon attackHitbox = attackingState.getAttackHitbox();
            if (attackHitbox != null) {
                Attack attack = attackingState.getAttack();
                if (attack != null) {
                    // Calculer le centre du polygone
                    Vector2 centroid = getPolygonCentroid(attackHitbox);

                    // Préparer le texte
                    String attackInfo = "ID: " + attack.getId() + " Damage: " + attack.getDamage();

                    // Dessiner le texte au centre de la hitbox
                    drawCenteredText(attackInfo, centroid.x, centroid.y);
                }
            }
        }

        // Dessiner la santé des mobs
        for (Mob mob : Mob.allMobs) {
            HealthComponent mobHealth = (HealthComponent) mob.getComponent(HealthComponent.class);
            if (mobHealth != null) {
                // Obtenir la position du mob
                Rectangle mobBounds = mob.getCollisionBounds();
                float mobTextX = mobBounds.x + mobBounds.width / 2;
                float mobTextY = mobBounds.y + mobBounds.height + 20; // Position légèrement au-dessus du mob

                // Préparer le texte
                String mobHealthText = mob.getName() + " Health: " + mobHealth.getCurrentHealthPoints() + "/" + mobHealth.getMaxHealthPoints();

                // Dessiner le texte au-dessus du mob
                drawCenteredText(mobHealthText, mobTextX, mobTextY);
            }
        }

        spriteBatch.end();
    }

    /**
     * Dessine un texte centré à une position donnée.
     *
     * @param text Le texte à dessiner.
     * @param x La position X centrale.
     * @param y La position Y centrale.
     */
    private void drawCenteredText(String text, float x, float y) {
        glyphLayout.setText(font, text);
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;
        font.setColor(TEXT_COLOR);
        font.draw(spriteBatch, text, x - textWidth / 2, y + textHeight / 2);
    }

    /**
     * Calcule le centroïde d'un polygone.
     *
     * @param polygon Le polygone dont le centroïde est calculé.
     * @return Le centroïde du polygone.
     */
    private Vector2 getPolygonCentroid(Polygon polygon) {
        float[] vertices = polygon.getTransformedVertices();
        float centroidX = 0, centroidY = 0;
        int numVertices = vertices.length / 2;
        for (int i = 0; i < vertices.length; i += 2) {
            centroidX += vertices[i];
            centroidY += vertices[i + 1];
        }
        centroidX /= numVertices;
        centroidY /= numVertices;
        return new Vector2(centroidX, centroidY);
    }

    /**
     * Gère l'ajout et la soustraction de points de vie via les touches "P" et "O".
     *
     * @param player Le joueur dont les points de vie sont modifiés.
     */
    private void handleHealthModification(PlayerCharacter player) {
        HealthComponent health = (HealthComponent) player.getComponent(HealthComponent.class);
        if (health == null) {
            System.out.println("HealthComponent missing");
            return;
        }

        // Ajouter 5 HP lorsque la touche "P" est pressée
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            int healedHealth = health.heal(5);
            System.out.println("Health increased to " + healedHealth + "/" + health.getMaxHealthPoints());
        }

        // Soustraire 5 HP lorsque la touche "O" est pressée
        if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            int damagedHealth = health.takeDamage(5);
            System.out.println("Health decreased to " + damagedHealth + "/" + health.getMaxHealthPoints());
        }
    }

    public void renderAttackHitbox(Mob mob, Camera camera) {
        Polygon attackHitbox = mob.getAttackHitbox();
        if (attackHitbox == null) {
            return; // Aucun polygone à dessiner
        }
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.ORANGE);
        shapeRenderer.polygon(attackHitbox.getTransformedVertices());
        shapeRenderer.end();
        shapeRenderer.dispose();
    }

    /**
     * Nettoie et libère les ressources utilisées par le DebugRenderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
        spriteBatch.dispose();
    }
}
