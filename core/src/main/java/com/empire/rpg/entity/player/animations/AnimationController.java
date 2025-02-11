package com.empire.rpg.entity.player.animations;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.empire.rpg.entity.player.PlayerCharacter;
import com.empire.rpg.entity.player.components.*;
import com.empire.rpg.entity.player.utils.Constants;
import com.empire.rpg.entity.player.animations.spritesheet.SpriteSheet;
import com.empire.rpg.entity.player.attacks.Attack;

import java.util.HashMap;
import java.util.Map;

// Contrôleur d'animation du joueur, gère les animations en fonction de l'état du joueur
public class AnimationController {
    private AnimationState currentAnimationState; // État d'animation actuel
    private CustomAnimation currentCustomAnimation; // Animation personnalisée actuelle (pour les attaques)
    private Body body; // Composant du corps du joueur
    private Outfit outfit; // Composant de la tenue du joueur
    private Hair hair; // Composant des cheveux du joueur
    private Hat hat; // Composant du chapeau du joueur
    private Tool1 tool1; // Composant de l'outil 1 (main gauche)
    private Tool2 tool2; // Composant de l'outil 2 (main droite)
    private PlayerCharacter PlayerCharacter; // Référence au joueur

    private Map<AnimationState, CustomAnimation> animations; // Map des animations standard (marche, repos, etc.)
    private float stateTime; // Temps écoulé depuis le début de l'animation

    // Maps pour stocker les spritesheets des différents composants
    private Map<String, SpriteSheet> bodySpriteSheets;
    private Map<String, SpriteSheet> outfitSpriteSheets;
    private Map<String, SpriteSheet> hairSpriteSheets;
    private Map<String, SpriteSheet> hatSpriteSheets;

    // Maps pour les spritesheets des outils en fonction de la catégorie et de la clé de spritesheet
    private Map<String, Map<String, SpriteSheet>> tool1SpriteSheets;
    private Map<String, Map<String, SpriteSheet>> tool2SpriteSheets;

    // Constructeur du contrôleur d'animation
    public AnimationController(PlayerCharacter PlayerCharacter, Body body, Outfit outfit, Hair hair, Hat hat, Tool1 tool1, Tool2 tool2) {
        this.PlayerCharacter = PlayerCharacter;
        this.body = body;
        this.outfit = outfit;
        this.hair = hair;
        this.hat = hat;
        this.tool1 = tool1;
        this.tool2 = tool2;
        animations = new HashMap<>();
        loadSpriteSheets(); // Charge les spritesheets nécessaires
        createAnimations(); // Crée les animations standard
        currentAnimationState = AnimationState.STANDING_DOWN; // Par défaut, le joueur regarde vers le bas
    }

    // Charger les spritesheets pour chaque composant
    private void loadSpriteSheets() {
        // Initialiser les maps pour les spritesheets
        bodySpriteSheets = new HashMap<>();
        outfitSpriteSheets = new HashMap<>();
        hairSpriteSheets = new HashMap<>();
        hatSpriteSheets = new HashMap<>();
        tool1SpriteSheets = new HashMap<>();
        tool2SpriteSheets = new HashMap<>();

        // Charger les spritesheets pour le corps
        for (String key : Constants.BODY_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.BODY_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            bodySpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour la tenue
        for (String key : Constants.OUTFIT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.OUTFIT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            outfitSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour les cheveux
        for (String key : Constants.HAIR_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAIR_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hairSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour le chapeau
        for (String key : Constants.HAT_SPRITESHEET_PATHS.keySet()) {
            String path = Constants.HAT_SPRITESHEET_PATHS.get(key);
            SpriteSheet spriteSheet = new SpriteSheet(path);
            hatSpriteSheets.put(key, spriteSheet);
        }

        // Charger les spritesheets pour les outils (tool1 et tool2) en fonction des attaques disponibles
        for (Attack attack : Constants.ATTACKS.values()) {
            String categoryKey = attack.getCategoryKey();

            // Charger les spritesheets pour tool1
            Map<String, String> toolsForCategory1 = Constants.TOOL1_SPRITESHEET_PATHS.get(categoryKey);
            if (toolsForCategory1 != null) {
                Map<String, SpriteSheet> toolSheets1 = new HashMap<>();
                for (String spritesheetKey : toolsForCategory1.keySet()) {
                    String path = toolsForCategory1.get(spritesheetKey);
                    SpriteSheet spriteSheet = new SpriteSheet(path);
                    toolSheets1.put(spritesheetKey, spriteSheet);
                }
                tool1SpriteSheets.put(categoryKey, toolSheets1);
            }

            // Charger les spritesheets pour tool2
            Map<String, String> toolsForCategory2 = Constants.TOOL2_SPRITESHEET_PATHS.get(categoryKey);
            if (toolsForCategory2 != null) {
                Map<String, SpriteSheet> toolSheets2 = new HashMap<>();
                for (String spritesheetKey : toolsForCategory2.keySet()) {
                    String path = toolsForCategory2.get(spritesheetKey);
                    SpriteSheet spriteSheet = new SpriteSheet(path);
                    toolSheets2.put(spritesheetKey, spriteSheet);
                }
                tool2SpriteSheets.put(categoryKey, toolSheets2);
            }
        }
    }

    // Créer les animations pour chaque état (marche, repos, etc.)
    private void createAnimations() {
        for (AnimationState state : AnimationState.values()) {
            String stateName = state.name();

            // Ignorer les états d'attaque ici (ils seront gérés séparément)
            if (!(stateName.startsWith("RUNNING") || stateName.startsWith("WALKING") || stateName.startsWith("STANDING"))) {
                continue;
            }

            int[][] frameIndices = Constants.FRAME_INDICES.get(stateName);
            float[] durations = Constants.FRAME_TIMINGS.get(stateName);

            if (frameIndices != null && durations != null) {
                // Déterminer la clé de spritesheet appropriée en fonction de l'état d'animation
                String spritesheetKey = getSpritesheetKeyForState(state);

                // Récupérer les SpriteSheets pour chaque composant
                SpriteSheet bodySheet = bodySpriteSheets.get(spritesheetKey);
                SpriteSheet outfitSheet = outfitSpriteSheets.get(spritesheetKey);
                SpriteSheet hairSheet = hairSpriteSheets.get(spritesheetKey);
                SpriteSheet hatSheet = hatSpriteSheets.get(spritesheetKey);

                // Créer les frames pour chaque composant
                TextureRegion[] bodyFrames = getFrames(bodySheet, frameIndices);
                TextureRegion[] outfitFrames = getFrames(outfitSheet, frameIndices);
                TextureRegion[] hairFrames = getFrames(hairSheet, frameIndices);
                TextureRegion[] hatFrames = getFrames(hatSheet, frameIndices);

                // Créer l'animation personnalisée sans outils (tool1 et tool2 sont null)
                CustomAnimation animation = new CustomAnimation(
                    bodyFrames, outfitFrames, hairFrames, hatFrames, null, null, durations, true
                );
                animations.put(state, animation);
            }
        }
    }

    // Déterminer la clé de spritesheet à utiliser en fonction de l'état d'animation
    private String getSpritesheetKeyForState(AnimationState state) {
        String stateName = state.name();
        if (stateName.startsWith("RUNNING") || stateName.startsWith("WALKING") || stateName.startsWith("STANDING")) {
            return "P1"; // Utilise la clé "P1" pour les animations standard
        } else {
            // Pour les autres états (attaques), la clé sera déterminée ailleurs
            return null;
        }
    }

    // Obtenir les frames à partir d'une spritesheet donnée et des indices de frames
    private TextureRegion[] getFrames(SpriteSheet spriteSheet, int[][] frameIndices) {
        if (spriteSheet == null) {
            return null;
        }
        TextureRegion[] frames = new TextureRegion[frameIndices.length];
        for (int i = 0; i < frameIndices.length; i++) {
            int row = frameIndices[i][0];
            int col = frameIndices[i][1];
            frames[i] = spriteSheet.getFrame(row, col);
        }
        return frames;
    }

    // Créer une animation d'attaque personnalisée
    public CustomAnimation createAttackAnimation(String categoryKey, String tool1SpritesheetKey, String tool2SpritesheetKey, AnimationState state) {
        int[][] frameIndices = Constants.FRAME_INDICES.get(state.name());
        float[] durations = Constants.FRAME_TIMINGS.get(state.name());

        if (frameIndices == null || durations == null) {
            // Si les indices de frames ou les timings sont manquants, retourner null
            return null;
        }

        // Récupérer les SpriteSheets pour chaque composant en fonction de la catégorie
        SpriteSheet bodySheet = bodySpriteSheets.get(categoryKey);
        SpriteSheet outfitSheet = outfitSpriteSheets.get(categoryKey);
        SpriteSheet hairSheet = hairSpriteSheets.get(categoryKey);
        SpriteSheet hatSheet = hatSpriteSheets.get(categoryKey);

        // Récupérer les SpriteSheets pour les outils
        Map<String, SpriteSheet> tool1Sheets = tool1SpriteSheets.get(categoryKey);
        SpriteSheet tool1Sheet = null;
        if (tool1Sheets != null && tool1SpritesheetKey != null) {
            tool1Sheet = tool1Sheets.get(tool1SpritesheetKey);
        }

        Map<String, SpriteSheet> tool2Sheets = tool2SpriteSheets.get(categoryKey);
        SpriteSheet tool2Sheet = null;
        if (tool2Sheets != null && tool2SpritesheetKey != null) {
            tool2Sheet = tool2Sheets.get(tool2SpritesheetKey);
        }

        // Obtenir les frames pour chaque composant
        TextureRegion[] bodyFrames = getFrames(bodySheet, frameIndices);
        TextureRegion[] outfitFrames = getFrames(outfitSheet, frameIndices);
        TextureRegion[] hairFrames = getFrames(hairSheet, frameIndices);
        TextureRegion[] hatFrames = getFrames(hatSheet, frameIndices);
        TextureRegion[] tool1Frames = getFrames(tool1Sheet, frameIndices);
        TextureRegion[] tool2Frames = getFrames(tool2Sheet, frameIndices);

        // Créer l'animation personnalisée avec les outils
        return new CustomAnimation(bodyFrames, outfitFrames, hairFrames, hatFrames, tool1Frames, tool2Frames, durations, false);
    }

    // Définir une animation personnalisée (par exemple lors d'une attaque)
    public void setCustomAnimation(CustomAnimation animation) {
        this.currentCustomAnimation = animation;
        this.stateTime = 0f; // Réinitialiser le temps d'animation
    }

    // Changer l'état d'animation actuel
    public void setAnimationState(AnimationState state) {
        if (currentAnimationState != state) {
            currentAnimationState = state;
            stateTime = 0f; // Réinitialiser le temps d'animation
        }
    }

    // Mettre à jour l'animation en fonction du temps écoulé
    public void update(float deltaTime) {
        stateTime += deltaTime;

        // Utiliser l'animation personnalisée si elle est définie, sinon l'animation standard
        CustomAnimation currentAnimation = (currentCustomAnimation != null) ? currentCustomAnimation : animations.get(currentAnimationState);

        if (currentAnimation != null) {
            // Récupérer les frames actuelles pour chaque composant
            TextureRegion currentBodyFrame = currentAnimation.getBodyKeyFrame(stateTime);
            TextureRegion currentOutfitFrame = currentAnimation.getOutfitKeyFrame(stateTime);
            TextureRegion currentHairFrame = currentAnimation.getHairKeyFrame(stateTime);
            TextureRegion currentHatFrame = currentAnimation.getHatKeyFrame(stateTime);
            TextureRegion currentTool1Frame = currentAnimation.getTool1KeyFrame(stateTime);
            TextureRegion currentTool2Frame = currentAnimation.getTool2KeyFrame(stateTime);

            // Mettre à jour les composants avec les frames actuelles
            body.update(currentBodyFrame);
            outfit.update(currentOutfitFrame);
            hair.update(currentHairFrame);
            hat.update(currentHatFrame);
            tool1.update(currentTool1Frame);
            tool2.update(currentTool2Frame);
        }
    }

    // Libérer les ressources associées aux spritesheets
    public void dispose() {
        for (SpriteSheet sheet : bodySpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : outfitSpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : hairSpriteSheets.values()) {
            sheet.dispose();
        }
        for (SpriteSheet sheet : hatSpriteSheets.values()) {
            sheet.dispose();
        }
    }
}
