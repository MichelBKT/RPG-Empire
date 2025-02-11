package com.empire.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class InteractionImageManager {
    private Rectangle interactionZone; // La zone du carré
    private Texture[] images; // Les images à afficher
    private int currentImageIndex; // Index de l'image actuellement affichée
    private static boolean isImageVisible; // Indique si une image est affichée

    public InteractionImageManager(float x, float y, float width, float height, String[] imagePaths) {
        this.interactionZone = new Rectangle(x, y, width, height);
        this.images = new Texture[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = new Texture(Gdx.files.internal(imagePaths[i]));
        }
        this.currentImageIndex = 0;
        this.isImageVisible = false;
    }

    public void update(Vector2 playerPosition) {
        // Vérifie si le joueur est dans la zone et appuie sur F
        if (interactionZone.contains(playerPosition)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                System.out.println("F pressed");
                isImageVisible = !isImageVisible; // Afficher ou cacher l'image
                currentImageIndex = 0; // Réinitialiser à la première image
            }
        }
        // Passer à l'image suivante si une image est visible et Espace est pressé
        if (isImageVisible && Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            if (currentImageIndex > 0 ){
                currentImageIndex--;
            }
        }

        // Passer à l'image suivante si une image est visible et Espace est pressé
        if (isImageVisible && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            currentImageIndex++;
            if (currentImageIndex >= images.length) {
                isImageVisible = false; // Cacher toutes les images si on dépasse la dernière
            }
        }
    }


    public static boolean getIsImageVisible() {
        return isImageVisible;
    }


    public void render(SpriteBatch batch) {
        if (isImageVisible) {
            // Dessine l'image actuellement visible sur toute la fenêtre
            batch.draw(images[currentImageIndex], 0, 0, 1280, 720);
        }
    }

    public void dispose() {
        for (Texture image : images) {
            image.dispose();
        }
    }
}
