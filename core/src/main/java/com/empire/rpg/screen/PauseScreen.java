package com.empire.rpg.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.empire.rpg.utils.FontUtils;

public class PauseScreen implements Screen {
    private boolean visible;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private Graphics g;
    private final String[] options = {"Reprendre", "Quitter"};
    private int selectedOption = 0;
    private BitmapFont customFont;
    private BitmapFont customFontTitle;


    public PauseScreen() {
        this.visible = false;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.shapeRenderer = new ShapeRenderer();
    }

    public void toggleVisibility() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedOption = (selectedOption + options.length - 1) % options.length; // Navigation circulaire
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedOption = (selectedOption + 1) % options.length; // Navigation circulaire
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            handleSelection();
        }
    }

    private void handleSelection() {
        switch (selectedOption) {
            case 0: // Reprendre
                toggleVisibility();
                break;
            case 1: // Quitter
                Gdx.app.exit();
                break;
        }
    }

    public void render(Graphics g) {
        if (!visible) return;


        // Dessiner le fond noir semi-transparent (pour effet de superposition)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0, 0, 0, 0.5f)); // Noir avec transparence
        shapeRenderer.rect(950, 250, 854, 480); // Fond semi-transparent sur tout l'écran
        shapeRenderer.end();

        // Dessiner les options avec surbrillance
        batch.begin();
        customFontTitle = FontUtils.createCustomFont("SinisterRegular.ttf", 72, Color.RED);
        customFontTitle.draw(batch, "MENU PAUSE", 540,  500);

        customFont = FontUtils.createCustomFont("SinisterRegular.ttf", 48, Color.WHITE);

        for (int i = 0; i < options.length; i++) {
            if (i == selectedOption) {
                customFont.setColor(Color.YELLOW); // Surbrillance pour l'option sélectionnée
            } else {
                customFont.setColor(Color.WHITE);
            }
            customFont.draw(batch, options[i], 540,  300 - i * 80);
        }

        batch.end();

        // Gérer les entrées pour navigation
        handleInput();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
