package com.empire.rpg.entity.player.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.empire.rpg.entity.player.equipment.Tool;
import com.empire.rpg.entity.player.attacks.Attack;
import com.empire.rpg.entity.player.animations.AnimationState;

public class Constants {
    // Variables pour le joueur
    public static final float PLAYER_WALKING_SPEED = 150f; // Vitesse de marche du joueur
    public static final float PLAYER_RUNNING_SPEED = 300f; // Vitesse de course du joueur
    public static final float PLAYER_SCALE = 2.0f; // Facteur d'échelle par défaut du joueur

    // Variables pour la collision du joueur
    public static final float PLAYER_COLLISION_WIDTH = 15f; // Largeur de la boîte de collision
    public static final float PLAYER_COLLISION_HEIGHT = 10f; // Hauteur de la boîte de collision
    public static final float PLAYER_COLLISION_OFFSET_X = -(PLAYER_COLLISION_WIDTH / 2); // Décalage en X de la boîte de collision
    public static final float PLAYER_COLLISION_OFFSET_Y = -12f; // Décalage en Y de la boîte de collision

    // Variables pour les sprites du joueur
    public static final int SPRITE_WIDTH = 64;
    public static final int SPRITE_HEIGHT = 64;

    // Chemins des sprites du joueur
    public static final Map<String, String> BODY_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> OUTFIT_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> HAIR_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, String> HAT_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, Map<String, String>> TOOL1_SPRITESHEET_PATHS = new HashMap<>();
    public static final Map<String, Map<String, String>> TOOL2_SPRITESHEET_PATHS = new HashMap<>();

    // Indices des frames et des timings pour les animations du joueur
    public static final Map<String, int[][]> FRAME_INDICES = new HashMap<>();
    public static final Map<String, float[]> FRAME_TIMINGS = new HashMap<>();

    // Initialisation les outils et les attaques
    public static final Map<String, Attack> ATTACKS = new HashMap<>();
    public static final Map<String, Tool> TOOLS = new HashMap<>();

    // Initialisation des chemins des sprites du joueur, des outils et des animations
    static {
        // Lecture du fichier JSON du joueur
        FileHandle filePlayer = Gdx.files.internal("Player/Player.json");
        JsonReader jsonReaderPlayer = new JsonReader();
        JsonValue basePlayer = jsonReaderPlayer.parse(filePlayer);

        // Lecture du fichier JSON des constantes
        FileHandle fileConstants = Gdx.files.internal("BDD/constants.json");
        JsonReader jsonReaderConstants = new JsonReader();
        JsonValue baseConstants = jsonReaderConstants.parse(fileConstants);

        // Accès à la section spritesheets dans constants.json
        JsonValue spritesheets = baseConstants.get("spritesheets");

        // Remplissage des maps en itérant sur chaque catégorie de spritesheets
        populateMap(basePlayer, spritesheets.get("body"), BODY_SPRITESHEET_PATHS, "body");
        populateMap(basePlayer, spritesheets.get("outfit"), OUTFIT_SPRITESHEET_PATHS, "outfit");
        populateMap(basePlayer, spritesheets.get("hair"), HAIR_SPRITESHEET_PATHS, "hair");
        populateMap(basePlayer, spritesheets.get("hat"), HAT_SPRITESHEET_PATHS, "hat");

        // Remplissage des maps pour les outils (catégories imbriquées)
        populateNestedMap(basePlayer, spritesheets.get("tool1"), TOOL1_SPRITESHEET_PATHS, "tool1");
        populateNestedMap(basePlayer, spritesheets.get("tool2"), TOOL2_SPRITESHEET_PATHS, "tool2");

        // Remplissage des animations
        JsonValue animations = baseConstants.get("animations");
        populateAnimations(animations);

        // Remplissage des attaques
        JsonValue attacks = baseConstants.get("attacks");
        populateAttacks(attacks);

        // Remplissage des outils
        JsonValue tools = baseConstants.get("tools");
        populateTools(tools);
    }

    /**
     * Méthode utilitaire pour remplir une map de spritesheet paths pour les catégories simples.
     *
     * @param basePlayer    JsonValue contenant les données du joueur.
     * @param categoryPaths JsonValue contenant les chemins pour une catégorie spécifique.
     * @param targetMap     Map cible à remplir avec les chemins des spritesheets.
     * @param categoryName  Nom de la catégorie (pour les messages d'erreur).
     */
    private static void populateMap(JsonValue basePlayer, JsonValue categoryPaths, Map<String, String> targetMap, String categoryName) {
        for (JsonValue entry = categoryPaths.child; entry != null; entry = entry.next) {
            String key = entry.name; // Par exemple "P1", "BOW1", etc.
            JsonValue pathArray = entry; // Tableau des segments de chemin

            // Naviguer dans basePlayer en suivant le chemin spécifié dans pathArray
            String path = navigatePath(basePlayer, pathArray);
            if (path != null) {
                targetMap.put(key, path);
            } else {
                // Gérer les erreurs ou les chemins manquants si nécessaire
                System.err.println("Chemin invalide ou valeur non trouvée pour la clé : " + key + " dans la catégorie : " + categoryName);
            }
        }
    }

    /**
     * Méthode utilitaire pour remplir une map de spritesheet paths pour les catégories imbriquées.
     *
     * @param basePlayer    JsonValue contenant les données du joueur.
     * @param categoryPaths JsonValue contenant les chemins pour une catégorie spécifique.
     * @param targetMap     Map cible à remplir avec les chemins des spritesheets imbriqués.
     * @param categoryName  Nom de la catégorie (pour les messages d'erreur).
     */
    private static void populateNestedMap(JsonValue basePlayer, JsonValue categoryPaths, Map<String, Map<String, String>> targetMap, String categoryName) {
        for (JsonValue firstLevel = categoryPaths.child; firstLevel != null; firstLevel = firstLevel.next) {
            String firstKey = firstLevel.name; // Par exemple "P2", "BOW1", etc.
            JsonValue secondLevel = firstLevel;

            if (secondLevel == null) continue;

            // Initialiser la sous-map si elle n'existe pas
            Map<String, String> subMap = targetMap.computeIfAbsent(firstKey, k -> new HashMap<>());

            for (JsonValue entry = secondLevel.child; entry != null; entry = entry.next) {
                String secondKey = entry.name; // Par exemple "BNET", "FARM", etc.
                JsonValue pathArray = entry; // Tableau des segments de chemin

                // Naviguer dans basePlayer en suivant le chemin spécifié dans pathArray
                String path = navigatePath(basePlayer, pathArray);
                if (path != null) {
                    subMap.put(secondKey, path);
                } else {
                    // Gérer les erreurs ou les chemins manquants si nécessaire
                    System.err.println("Chemin invalide ou valeur non trouvée pour la clé : " + secondKey + " dans la sous-catégorie : " + firstKey + " de la catégorie : " + categoryName);
                }
            }
        }
    }

    /**
     * Méthode utilitaire pour naviguer dans le JsonValue en suivant un chemin spécifié par un tableau.
     *
     * @param basePlayer JsonValue de base (Player.json).
     * @param pathArray  JsonValue représentant le tableau des segments de chemin.
     * @return La chaîne de caractères finale si le chemin est valide, sinon null.
     */
    private static String navigatePath(JsonValue basePlayer, JsonValue pathArray) {
        JsonValue current = basePlayer;
        for (JsonValue pathElement = pathArray.child; pathElement != null; pathElement = pathElement.next) {
            current = current.get(pathElement.asString());
            if (current == null) {
                return null; // Chemin invalide
            }
        }
        return current.isString() ? current.asString() : null;
    }

    /**
     * Méthode utilitaire pour remplir les animations.
     *
     * @param animations JsonValue contenant les animations.
     */
    private static void populateAnimations(JsonValue animations) {
        for (JsonValue animEntry = animations.child; animEntry != null; animEntry = animEntry.next) {
            String animName = animEntry.name; // Par exemple "STANDING_UP", "WALKING_UP", etc.

            // Récupérer les frames et les timings
            JsonValue frames = animEntry.get("frames");
            JsonValue timings = animEntry.get("timings");

            // Convertir les frames en int[][]
            int[][] frameIndices = new int[frames.size][2];
            for (int i = 0; i < frames.size; i++) {
                JsonValue frame = frames.get(i);
                frameIndices[i][0] = frame.get(0).asInt();
                frameIndices[i][1] = frame.get(1).asInt();
            }

            // Convertir les timings en float[]
            float[] frameTimings = new float[timings.size];
            for (int i = 0; i < timings.size; i++) {
                frameTimings[i] = timings.get(i).asFloat();
            }

            // Remplir les maps FRAME_INDICES et FRAME_TIMINGS
            FRAME_INDICES.put(animName, frameIndices);
            FRAME_TIMINGS.put(animName, frameTimings);
        }
    }

    /**
     * Méthode utilitaire pour remplir les attaques.
     *
     * @param attacks JsonValue contenant les attaques.
     */
    private static void populateAttacks(JsonValue attacks) {
        for (JsonValue attackEntry = attacks.child; attackEntry != null; attackEntry = attackEntry.next) {
            String attackKey = attackEntry.name; // Par exemple "ONE_SLASH1", "POL_SLASH1", etc.

            String id = attackEntry.get("id").asString();
            String name = attackEntry.get("name").asString();
            String toolId = attackEntry.get("toolId").asString();

            // Récupérer les états
            JsonValue statesJson = attackEntry.get("states");
            Map<String, AnimationState> states = new HashMap<>();
            for (JsonValue stateEntry = statesJson.child; stateEntry != null; stateEntry = stateEntry.next) {
                String direction = stateEntry.name.toUpperCase(); // "up" -> "UP"
                String stateName = stateEntry.asString();
                AnimationState state = AnimationState.valueOf(stateName);
                states.put(direction, state);
            }

            float duration = attackEntry.get("duration").asFloat();
            float damage = attackEntry.get("damage").asFloat();
            float cooldown = attackEntry.get("cooldown").asFloat();

            // Récupérer les hitbox
            JsonValue hitboxJson = attackEntry.get("hitbox");
            float hitboxWidth = hitboxJson.get("width").asFloat();
            float hitboxHeight = hitboxJson.get("height").asFloat();

            // Créer et ajouter l'attaque
            Attack attack = new Attack(id, name, toolId, states, duration, damage, cooldown, hitboxWidth, hitboxHeight);
            ATTACKS.put(id, attack);
        }
    }

    /**
     * Méthode utilitaire pour remplir les outils.
     *
     * @param tools JsonValue contenant les outils.
     */
    private static void populateTools(JsonValue tools) {
        for (JsonValue toolEntry = tools.child; toolEntry != null; toolEntry = toolEntry.next) {
            String toolKey = toolEntry.name; // Par exemple "AX01", "MC01", etc.

            String id = toolEntry.get("id").asString();
            String name = toolEntry.get("name").asString();
            String spritesheetKey = toolEntry.get("spritesheetKey").asString();

            // Récupérer les attaques associées
            JsonValue attackIdsJson = toolEntry.get("attackId");
            List<String> attackIds = new ArrayList<>();
            for (JsonValue attackIdEntry = attackIdsJson.child; attackIdEntry != null; attackIdEntry = attackIdEntry.next) {
                attackIds.add(attackIdEntry.asString());
            }

            // Créer et ajouter l'outil
            Tool tool = new Tool(id, name, spritesheetKey, attackIds);
            TOOLS.put(id, tool);
        }
    }
}
