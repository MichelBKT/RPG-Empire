package com.empire.rpg.entity.player.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Classe représentant une animation personnalisée composée de plusieurs frames pour chaque composant
public class CustomAnimation {
    private TextureRegion[] bodyFrames; // Frames pour le corps
    private TextureRegion[] outfitFrames; // Frames pour la tenue
    private TextureRegion[] hairFrames; // Frames pour les cheveux
    private TextureRegion[] hatFrames; // Frames pour le chapeau
    private TextureRegion[] tool1Frames; // Frames pour l'outil 1
    private TextureRegion[] tool2Frames; // Frames pour l'outil 2
    private float[] frameDurations; // Durée de chaque frame
    private boolean looping; // Indique si l'animation est en boucle
    private float totalDuration; // Durée totale de l'animation

    // Constructeur de l'animation personnalisée
    public CustomAnimation(TextureRegion[] bodyFrames, TextureRegion[] outfitFrames, TextureRegion[] hairFrames,
                           TextureRegion[] hatFrames, TextureRegion[] tool1Frames, TextureRegion[] tool2Frames,
                           float[] frameDurations, boolean looping) {
        this.bodyFrames = bodyFrames;
        this.outfitFrames = outfitFrames;
        this.hairFrames = hairFrames;
        this.hatFrames = hatFrames;
        this.tool1Frames = tool1Frames;
        this.tool2Frames = tool2Frames;
        this.frameDurations = frameDurations;
        this.looping = looping;
        for (float duration : frameDurations) {
            totalDuration += duration; // Calculer la durée totale de l'animation
        }
    }

    // Méthodes pour obtenir la frame actuelle pour chaque composant en fonction du temps écoulé

    public TextureRegion getBodyKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return bodyFrames[frameIndex];
    }

    public TextureRegion getOutfitKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return outfitFrames[frameIndex];
    }

    public TextureRegion getHairKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return hairFrames[frameIndex];
    }

    public TextureRegion getHatKeyFrame(float stateTime) {
        int frameIndex = getFrameIndex(stateTime);
        return hatFrames[frameIndex];
    }

    public TextureRegion getTool1KeyFrame(float stateTime) {
        if (tool1Frames == null) return null; // Si pas d'outil 1, retourner null
        int frameIndex = getFrameIndex(stateTime);
        return tool1Frames[frameIndex];
    }

    public TextureRegion getTool2KeyFrame(float stateTime) {
        if (tool2Frames == null) return null; // Si pas d'outil 2, retourner null
        int frameIndex = getFrameIndex(stateTime);
        return tool2Frames[frameIndex];
    }

    // Calculer l'indice de la frame actuelle en fonction du temps écoulé
    private int getFrameIndex(float stateTime) {
        if (looping) {
            stateTime = stateTime % totalDuration; // Boucler le temps si l'animation est en boucle
        } else if (stateTime >= totalDuration) {
            return frameDurations.length - 1; // Si l'animation est terminée, retourner la dernière frame
        }

        float time = 0f;
        for (int i = 0; i < frameDurations.length; i++) {
            time += frameDurations[i];
            if (stateTime < time) {
                return i;
            }
        }
        return frameDurations.length - 1; // Par défaut, retourner la dernière frame
    }
}
