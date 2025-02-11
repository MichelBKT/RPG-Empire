package com.empire.rpg.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    private Texture texture;
    private TextureRegion currentFrame;
    private TextureRegion[] frames;
    private float stateTime;
    private float scale;

    /**
     * Constructeur pour initialiser la texture, les frames, et définir la frame initiale.
     *
     * @param texture la texture à utiliser
     * @param frameWidth largeur de chaque frame dans la texture
     * @param frameHeight hauteur de chaque frame dans la texture
     * @param row index de la ligne de la frame initiale
     * @param col index de la colonne de la frame initiale
     * @param scale facteur d'échelle pour redimensionner la texture
     */
    public TextureComponent(Texture texture, int frameWidth, int frameHeight, int row, int col, float scale) {
        this.texture = texture;
        this.scale = scale;

        // Découpe de la texture en frames
        TextureRegion[][] tmp = TextureRegion.split(texture, frameWidth, frameHeight);
        frames = new TextureRegion[tmp.length * tmp[0].length];

        int index = 0;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        // Sélection de la frame initiale selon les indices row et col
        if (row < tmp.length && col < tmp[0].length) {
            currentFrame = tmp[row][col];
        } else {
            currentFrame = frames[0];  // Si les indices sont hors limites, utilise la première frame
        }

        stateTime = 0f;
    }

    public void updateStateTime(float deltaTime) {
        stateTime += deltaTime;
        int frameIndex = (int) (stateTime / 0.1f) % frames.length;
        currentFrame = frames[frameIndex];
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public int getDamageReduction() {
        return 0;
    }

    @Override
    public void setCurrentHealthPoints(int i) {}

    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }
}
