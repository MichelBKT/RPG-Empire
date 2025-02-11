package com.empire.rpg.entity.player.Inventory;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.empire.rpg.entity.Item;


public class Inventory {

    //partie ouverture de l'inventaire
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private static boolean showInteractionFrame = false;

    //partie organisation de l'inventaire
    private final String[] categories = {"Armes", "Tenue", "Consommable", "Outil",  "Objet de quête", "Divers"};
    private int selectedCategoryIndex = 0;
    private int selectedObjectIndex = 0;
    private boolean inCategorySelection = true;
    private static List<Item> items;  // Liste des objets chargés à partir du JSON

    //scroll des objet
    private static final int MAX_VISIBLE_ITEMS = 11; // Nombre maximum d'objets affichés en même temps
    private int scrollOffset = 0; // Décalage de défilement pour l'affichage des objets


    private static Item equippedArmor;
    private static Item equippedConsommable;
    private static Item equippedLance;
    private static Item equippedEpee;
    private static Item equippedBouclier;
    private static Item equippedArc;
    private static Item equippedCarquois;


    public Inventory(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.items = new ArrayList<>();
        loadInventoryFromJson();
    }

    //partie trie de la liste du fichier JSON
    public static void loadInventoryFromJson() {

        // Vider la liste existante
        items.clear();

        Json json = new Json();
        JsonValue base = new JsonReader().parse(Gdx.files.internal("BDD/inventory.json"));

        for (JsonValue itemJson : base.get("inventory")) {
            String name = itemJson.getString("name");
            int quantity = itemJson.getInt("quantity");
            String type = itemJson.getString("type");
            String description = itemJson.getString("description");
            int valeur = itemJson.getInt("valeur", 0);  // Valeur par défaut 0 si absent
            boolean states = itemJson.getBoolean("states", false);
            String style = itemJson.getString("style", "null");

            Item item = new Item(name, quantity, type, description, valeur, states, style);
            items.add(item);

            // Restaurer les équipements
            if (states) {
                if (type.equalsIgnoreCase("Tenue")) {
                    equippedArmor = item;
                } else if (type.equalsIgnoreCase("Consommable")) {
                    equippedConsommable = item;
                } else if (type.equalsIgnoreCase("Armes")) {
                    switch (style.toLowerCase()) {
                        case "lance":
                            equippedLance = item;
                            break;
                        case "épée":
                            equippedEpee = item;
                            break;
                        case "arc":
                            equippedArc = item;
                            break;
                        case "bouclier":
                            equippedBouclier = item;
                            break;
                        case "carquois":
                            equippedCarquois = item;
                            break;
                    }
                }
            }
        }
    }

    private static void saveInventoryToJson() {
        Json json = new Json();

        // Filtrer les objets avec une quantité > 0
        List<Item> itemsToSave = items.stream()
            .filter(item -> item.getQuantity() > 0)
            .collect(Collectors.toList());

        String inventoryJson = json.prettyPrint(itemsToSave); // Convertit la liste des objets en JSON
        Gdx.files.local("BDD/inventory.json").writeString("{\"inventory\":" + inventoryJson + "}", false);
    }

    public void render(Vector2 playerPosition) {
        if (showInteractionFrame) {
            drawInteractionFrame(playerPosition);

        }
    }

    // partie création de l'interface graphique de l'inventaire
    private void drawInteractionFrame(Vector2 playerPosition) {
        float frameWidth = 700;
        float frameHeight = 400;
        float frameX = playerPosition.x - frameWidth / 2;
        float frameY = playerPosition.y - frameHeight / 2;
        float padding = 15;
        float lineWidth = 2f; // Épaisseur des lignes

        // Fond noir transparent
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.rect(frameX, frameY, frameWidth, frameHeight);
        shapeRenderer.end();

        // Contour blanc épaissi
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(frameX, frameY + frameHeight - lineWidth, frameWidth, lineWidth); // Haut
        shapeRenderer.rect(frameX, frameY, frameWidth, lineWidth);                          // Bas
        shapeRenderer.rect(frameX, frameY, lineWidth, frameHeight);                        // Gauche
        shapeRenderer.rect(frameX + frameWidth - lineWidth, frameY, lineWidth, frameHeight); // Droite
        shapeRenderer.end();

        // Dessiner le titre "Inventaire" avec une taille de police plus grande
        batch.begin();
        String messageTop = "Inventaire";
        GlyphLayout layout = new GlyphLayout(font, messageTop);

        // Augmenter temporairement la taille de la police
        float originalScaleX = font.getData().scaleX;
        float originalScaleY = font.getData().scaleY;
        font.getData().setScale(1.5f); // Augmenter la taille du texte uniquement pour le titre

        font.setColor(1, 1, 1, 1);
        float textTopX = frameX + (frameWidth - layout.width * font.getScaleX()) / 2;
        float textTopY = frameY + frameHeight - padding;
        font.draw(batch, messageTop, textTopX, textTopY);

        // Réinitialiser la taille de la police pour le reste des textes
        font.getData().setScale(originalScaleX, originalScaleY);
        batch.end();

        // Barre de séparation sous "Inventaire"
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float separatorY = textTopY - layout.height * 2f - 10; // Adapter à la taille du texte
        shapeRenderer.rect(frameX, separatorY - lineWidth / 2, frameWidth, lineWidth); // Toute la largeur du cadre
        shapeRenderer.end();

        // Colonnes de séparation verticales ajustées
        float verticalLineEndY = separatorY - lineWidth / 2; // Arrêter les lignes verticales ici
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float categoryObjectSeparatorX = frameX - 80 + frameWidth / 3;
        float objectDetailSeparatorX = frameX - 80 + (2 * frameWidth / 3);

        shapeRenderer.rect(categoryObjectSeparatorX - lineWidth / 2, frameY, lineWidth, verticalLineEndY - frameY); // Catégories
        shapeRenderer.rect(objectDetailSeparatorX - lineWidth / 2, frameY, lineWidth, verticalLineEndY - frameY);  // Objets
        shapeRenderer.end();

        batch.begin();

        // Affiche les catégories
        drawCategoriesMenu(frameX, frameY, frameHeight);

        // Affiche les objets
        drawObjectsMenu(frameX - 45, frameY, frameHeight);

        // Affiche les détails de l'objet sélectionné
        drawItemDetails(objectDetailSeparatorX + 10, frameY, frameHeight);

        batch.end();
    }

    // partie création de la partie catégorie
    private void drawCategoriesMenu(float frameX, float frameY, float frameHeight) {
        for (int i = 0; i < categories.length; i++) {
            String category = categories[i];
            float textY = frameY + frameHeight - 60 - i * 30;
            font.draw(batch, (i == selectedCategoryIndex ? "-> " : "") + category, frameX + 20, textY);
        }
    }



    // partie création de la partie objet
    private void drawObjectsMenu(float frameX, float frameY, float frameHeight) {
        String category = categories[selectedCategoryIndex];
        List<Item> currentItems = getItemsByType(category);

        int start = scrollOffset;
        int end = Math.min(start + MAX_VISIBLE_ITEMS, currentItems.size());

        for (int i = start; i < end; i++) {
            Item item = currentItems.get(i);
            float textY = frameY + frameHeight - 60 - (i - start) * 30;

            // Indicateur pour l'objet sélectionné et équipé
            String prefix = (!inCategorySelection && i == selectedObjectIndex) ? "-> " : "";
            String equippedIndicator = item.getStates() ? " <-" : "";

            font.draw(batch, prefix + item.getName() + " (x" + item.getQuantity() + ")" + equippedIndicator, frameX + 220, textY);
        }
    }

    // partie création de la partie détail d'objet
    private void drawItemDetails(float frameX, float frameY, float frameHeight) {
        // Vérifie que l'utilisateur est dans le mode de sélection d'objet
        if (!inCategorySelection) {
            // Récupérer la catégorie et l'objet sélectionnés
            String category = categories[selectedCategoryIndex];
            List<Item> currentItems = getItemsByType(category);

            // Vérifie qu'il y a bien un objet sélectionné dans la catégorie
            if (!currentItems.isEmpty() && selectedObjectIndex < currentItems.size()) {
                Item selectedItem = currentItems.get(selectedObjectIndex);

                // Définir la même position de départ en Y que les sections catégories et objets
                float startY = frameY + frameHeight - 60;

                // Afficher le nom de l'objet
                font.draw(batch, "Nom : " + selectedItem.getName(), frameX + 10, startY);

                // Afficher la description de l'objet, légèrement en dessous
                font.draw(batch, "Description :", frameX + 10, startY - 30);
                font.draw(batch, selectedItem.getDescription(), frameX + 10, startY - 60);

                // Afficher la valeur de l'objet (si elle est différente de zéro), un peu plus bas
                if (selectedItem.getValeur() > 0) {
                    font.draw(batch, "Valeur : " + selectedItem.getValeur(), frameX + 10, startY - 90);
                }

            }
        }
    }


    // Appelle un objet selon son type
    private List<Item> getItemsByType(String type) {
        return items.stream().filter(item -> item.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

    //partie initialisation de touche
    public void update() {
        List<Item> currentItems = getItemsByType(categories[selectedCategoryIndex]);

        if(showInteractionFrame){
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                if (inCategorySelection) {
                    selectedCategoryIndex = (selectedCategoryIndex - 1 + categories.length) % categories.length;
                } else {
                    if (selectedObjectIndex > 0) {
                        selectedObjectIndex--;
                        // Vérifier si on doit défiler vers le haut
                        if (selectedObjectIndex < scrollOffset) {
                            scrollOffset--;
                        }
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                if (inCategorySelection) {
                    selectedCategoryIndex = (selectedCategoryIndex + 1) % categories.length;
                } else {
                    if (selectedObjectIndex < currentItems.size() - 1) {
                        selectedObjectIndex++;
                        // Vérifier si on doit défiler vers le bas
                        if (selectedObjectIndex >= scrollOffset + MAX_VISIBLE_ITEMS) {
                            scrollOffset++;
                        }
                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
                if (inCategorySelection) {
                    inCategorySelection = false;
                    selectedObjectIndex = 0;
                    scrollOffset = 0; // Réinitialiser le défilement pour le nouvel écran d'objets
                }
            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                if (!inCategorySelection) {
                    inCategorySelection = true;
                    selectedObjectIndex = 0;
                    scrollOffset = 0; // Réinitialiser le défilement pour le nouvel écran de catégories
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (!inCategorySelection && !currentItems.isEmpty() && selectedObjectIndex < currentItems.size()) {
                    Item selectedItem = currentItems.get(selectedObjectIndex);

                    // Vérifier si la quantité de l'objet est à 0 et le supprimer
                    items.removeIf(item -> item.getQuantity() == 0);

                    // Si l'objet est déjà équipé, on le déséquipe
                    if (selectedItem.getStates()) {
                        selectedItem.setStates(false);

                        // Déséquipement selon le type
                        if (selectedItem.getType().equalsIgnoreCase("Tenue")) {
                            equippedArmor = null;
                        } else if (selectedItem.getType().equalsIgnoreCase("Consommable")) {
                            equippedConsommable = null;
                        } else if (selectedItem.getType().equalsIgnoreCase("Armes")) {
                            // Déséquiper une arme spécifique selon son style
                            if (selectedItem.getStyle().equalsIgnoreCase("lance")) {
                                equippedLance = null;
                            } else if (selectedItem.getStyle().equalsIgnoreCase("épée")) {
                                equippedEpee = null;
                            } else if (selectedItem.getStyle().equalsIgnoreCase("arc")) {
                                equippedArc = null;
                            } else if (selectedItem.getStyle().equalsIgnoreCase("bouclier")) {
                                equippedBouclier = null;
                            } else if (selectedItem.getStyle().equalsIgnoreCase("carquois")) {
                                equippedCarquois = null;
                            }
                        }
                    } else { // Si l'objet n'est pas équipé, on l'équipe
                        if (selectedItem.getType().equalsIgnoreCase("Tenue")) {
                            // Si une tenue est déjà équipée, la déséquiper
                            if (equippedArmor != null) equippedArmor.setStates(false);
                            equippedArmor = selectedItem;
                            selectedItem.setStates(true);  // Marquer comme équipé
                        }

                        else if (selectedItem.getType().equalsIgnoreCase("Consommable")) {
                            // Si un consommable est déjà équipé, la déséquiper
                            if (equippedConsommable != null) equippedConsommable.setStates(false);
                            equippedConsommable = selectedItem;
                            selectedItem.setStates(true);  // Marquer comme équipé
                        }

                        else if (selectedItem.getType().equalsIgnoreCase("Armes")) {
                            switch (selectedItem.getStyle().toLowerCase()) {
                                case "lance":
                                    if (equippedLance != null) equippedLance.setStates(false); // Déséquiper la lance précédente
                                    equippedLance = selectedItem;
                                    break;
                                case "épée":
                                    if (equippedEpee != null) equippedEpee.setStates(false); // Déséquiper l'épée précédente
                                    equippedEpee = selectedItem;
                                    break;
                                case "arc":
                                    if (equippedArc != null) equippedArc.setStates(false); // Déséquiper l'arc précédent
                                    equippedArc = selectedItem;
                                    break;
                                case "bouclier":
                                    if (equippedBouclier != null) equippedBouclier.setStates(false); // Déséquiper le bouclier précédent
                                    equippedBouclier = selectedItem;
                                    break;
                                case "carquois":
                                    if (equippedCarquois != null) equippedCarquois.setStates(false); // Déséquiper le carquois précédent
                                    equippedCarquois = selectedItem;
                                    break;
                            }
                            selectedItem.setStates(true);
                        }

                    }
                    saveInventoryToJson(); // Sauvegarde après modification
                }
            }
        }

    }

    public static int getPiece() {
        String targetName = "Pièce Astrale"; // Nom de l'objet à rechercher
        return items.stream()
            .filter(item -> item.getName().equalsIgnoreCase(targetName)) // Trouver l'objet par son nom
            .findFirst() // Récupérer le premier match
            .map(Item::getQuantity) // Obtenir la quantité
            .orElse(0); // Retourner 0 si l'objet n'est pas trouvé
    }

    public static void setArgent(int newQuantity) {
        String targetName = "Pièce Astrale"; // Nom de l'objet à rechercher

        // Vérifier si l'objet existe déjà dans l'inventaire
        Item existingItem = items.stream()
            .filter(item -> item.getName().equalsIgnoreCase(targetName))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            // Si l'objet existe, mettre à jour sa quantité
            existingItem.setQuantity(newQuantity);
        } else {
            // Si l'objet n'existe pas, le créer et l'ajouter à l'inventaire
            Item astralPiece = new Item(
                targetName, // Nom de l'objet
                newQuantity, // Quantité
                "Divers", // Type
                "Blablabla", // Description
                0, // Valeur
                false, // States
                "null" // Style
            );
            items.add(astralPiece); // Ajouter le nouvel objet à l'inventaire
        }

        // Sauvegarder les changements dans le fichier JSON
        saveInventoryToJson();
    }

    public static boolean getShowInteractionFrame() {
        return showInteractionFrame;
    }

    public static void setShowInteractionFrame(boolean show) {
        showInteractionFrame = show;
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }


}
