package com.empire.rpg.entity.player.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

// Gestionnaire de sons pour les attaques
public class SoundManager {
    private static Map<String, Sound> soundCache = new HashMap<>(); // Cache pour les sons déjà chargés

    // Méthode pour jouer le son associé à une attaque
    public static void playSound(String attackId) {
        Sound sound = soundCache.get(attackId);
        if (sound == null) {
            // Si le son n'est pas encore chargé, le charger depuis le fichier correspondant
            String filePath = "Audio/SFX/Weapon/" + attackId + ".mp3";
            sound = Gdx.audio.newSound(Gdx.files.internal(filePath));
            soundCache.put(attackId, sound);
        }
        sound.play(); // Jouer le son
    }

    // Méthode pour libérer les ressources associées aux sons
    public static void dispose() {
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}
