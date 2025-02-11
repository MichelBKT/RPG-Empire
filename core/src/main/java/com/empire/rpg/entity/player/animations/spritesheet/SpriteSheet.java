package com.empire.rpg.entity.player.animations.spritesheet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.entity.player.utils.Constants;

// Classe de base pour gérer les spritesheets
public class SpriteSheet {
    private Texture texture; // Texture contenant l'image complète de la spritesheet
    private TextureRegion[][] frames; // Tableau 2D des frames découpées de la spritesheet

    // Constructeur par défaut sans paramètres
    public SpriteSheet() {
        // Rien à initialiser ici
    }

    // Constructeur avec le chemin vers la texture de la spritesheet
    public SpriteSheet(String texturePath) {
        // Charge la texture depuis le chemin fourni
        texture = new Texture(Gdx.files.internal(texturePath));
        // Divise la texture en régions (frames) de taille constante définie dans les constantes
        frames = TextureRegion.split(texture, Constants.SPRITE_WIDTH, Constants.SPRITE_HEIGHT);
    }

    // Définit la texture de la spritesheet en utilisant un nouveau chemin
    public void setTexture(String texturePath) {
        if (texture != null) {
            // Libère les ressources de l'ancienne texture si elle existe
            texture.dispose();
        }
        // Charge la nouvelle texture
        texture = new Texture(Gdx.files.internal(texturePath));
        // Divise la nouvelle texture en frames
        frames = TextureRegion.split(texture, Constants.SPRITE_WIDTH, Constants.SPRITE_HEIGHT);
    }

    // Récupère une frame spécifique de la spritesheet en fonction de la ligne et de la colonne
    public TextureRegion getFrame(int row, int col) {
        return frames[row][col];
    }

    // Libère les ressources associées à la texture
    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
