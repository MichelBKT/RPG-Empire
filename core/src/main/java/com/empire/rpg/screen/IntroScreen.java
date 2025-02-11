package com.empire.rpg.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.empire.rpg.Main;

public class IntroScreen implements Screen {
    private  SpriteBatch batch;
    private float timer = 5f;
    private  Array<Texture> frames;
    private float elapsedTime = 0f;
    private float frameDuration = 1 / 30f; // 30 FPS
    private Sound introMusic = Gdx.audio.newSound(Gdx.files.internal("Audio/introScreen.mp3"));
    private boolean soundPlayed = false;
    private Runnable onFinish;
    private boolean animationFinished = false;

    public IntroScreen(Runnable onFinish) {
        this.onFinish = onFinish;
        this.batch = new SpriteBatch();
        this.frames = new Array<>();
        show();
    }

    @Override
    public void show() {
        int numberOfFrames = 152; // Ajuste selon ton animation
        for (int i = 1; i <= numberOfFrames; i++) {
            String filePath = "Anim/RPG_Empire_launching_animation/frame_" + String.format("%04d", i) + ".png";
            if (Gdx.files.internal(filePath).exists()) {
                frames.add(new Texture(filePath));
            } else {
                System.err.println("Frame non trouvée : " + filePath);
            }
        }

    }

    @Override
    public void render(float delta) {
        // Effacer l'écran
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += delta;
        int currentFrameIndex = (int) (elapsedTime / frameDuration);

        // Jouer le son une seule fois au début
        if (!soundPlayed && elapsedTime >= 0.1f) {
            introMusic.play();
            soundPlayed = true;
        }
        if (currentFrameIndex == frames.size) {
            if (!animationFinished) {
                animationFinished = true;
                onFinish.run(); // Appeler le Runnable pour passer à l'écran suivant
            }
            timer -= delta;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        Texture currentFrame = frames.get(Math.min(currentFrameIndex, frames.size - 1));

        float x = (Gdx.graphics.getWidth() - currentFrame.getWidth()) / 2f;
        float y = (Gdx.graphics.getHeight() - currentFrame.getHeight()) / 2f;
        batch.draw(currentFrame, x, y);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture frame : frames) {
            frame.dispose();
        }
        introMusic.dispose();
    }
}
