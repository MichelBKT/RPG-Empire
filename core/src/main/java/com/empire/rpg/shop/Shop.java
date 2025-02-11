package com.empire.rpg.shop;

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
import com.empire.rpg.entity.player.Inventory.Inventory;


public class Shop {

    //partie ouverture du shop
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private static boolean showShopFrame = false;


    //partie organisation du shop
    private final String[] categories = {"Armes", "Tenue", "Consommable", "Outil",  "Objet de quête", "Divers"};
    private int selectedCategoryIndex = 0;
    private int selectedObjectIndex = 0;
    private boolean inCategorySelection = true;
    private List<Item> items;  // Liste des objets chargés à partir du JSON

    //scroll des objet
    private static final int MAX_VISIBLE_ITEMS = 10; // Nombre maximum d'objets affichés en même temps
    private int scrollOffset = 0; // Décalage de défilement pour l'affichage des objets


    public Shop(OrthographicCamera camera, SpriteBatch batch) {
        this.camera = camera;
        this.batch = batch;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.items = new ArrayList<>();
        loadShopFromJson();  // Charger les données JSON à l'initialisation
    }

    //partie trie de la liste du fichier JSON
    private void loadShopFromJson() {
        Json json = new Json();
        JsonValue base = new JsonReader().parse(Gdx.files.internal("BDD/shop.json"));

        for (JsonValue itemJson : base.get("shop")) {
            String name = itemJson.getString("name");
            int quantity = itemJson.getInt("quantity");
            String type = itemJson.getString("type");
            String description = itemJson.getString("description");
            int valeur = itemJson.getInt("valeur", 0);  // Valeur par défaut 0 si absent
            boolean states = itemJson.getBoolean("states", false);
            String style = itemJson.getString("style", "null");

            Item item = new Item(name, quantity, type, description, valeur, states, style);
            items.add(item);


        }
    }

    private void addItemToInventory(Item selectedItem) {
        // Charger l'inventaire existant depuis le fichier JSON
        Json json = new Json();
        JsonValue base = new JsonReader().parse(Gdx.files.local("BDD/inventory.json"));
        List<Item> inventory = new ArrayList<>();

        for (JsonValue itemJson : base.get("inventory")) {
            String name = itemJson.getString("name");
            int quantity = itemJson.getInt("quantity");
            String type = itemJson.getString("type");
            String description = itemJson.getString("description");
            int valeur = itemJson.getInt("valeur", 0); // Valeur par défaut 0 si absent
            boolean states = itemJson.getBoolean("states", false);
            String style = itemJson.getString("style", "null");

            inventory.add(new Item(name, quantity, type, description, valeur, states, style));
        }

        // Vérifier si l'item existe déjà dans l'inventaire
        boolean itemExists = false;
        for (Item item : inventory) {
            if (item.getName().equals(selectedItem.getName())) {
                // Augmenter la quantité
                item.setQuantity(item.getQuantity() + selectedItem.getQuantity());
                System.out.println(item.getQuantity());
                itemExists = true;
                break;
            }
        }

        // Si l'item n'existe pas, l'ajouter
        if (!itemExists) {
            inventory.add(selectedItem);
        }

        // Sauvegarder les modifications dans le fichier JSON
        String inventoryJson = json.prettyPrint(inventory);
        Gdx.files.local("BDD/inventory.json").writeString("{\"inventory\":" + inventoryJson + "}", false);
    }

    public void render(Vector2 playerPosition) {
        if (showShopFrame) {
            drawShopFrame(playerPosition);
        }
    }

    // partie création de l'interface graphique du shop
    private void drawShopFrame(Vector2 playerPosition) {
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

        // Dessiner le titre "Magasin" avec une taille de police plus grande
        batch.begin();
        String messageTop = "Magasin";
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


        // Ajouter la section Achat/Vente
        font.getData().setScale(1.0f); // Réduire la taille de la police pour cette section
        float sectionHeight = frameHeight / 2; // Moitié de la hauteur du cadre
        float sectionY = textTopY - layout.height * 2f - 20; // Position de la section

        // Dessiner "Achat"
        String achatText = "->Achat<-";
        GlyphLayout achatLayout = new GlyphLayout(font, achatText);
        float achatX = frameX + (frameWidth / 4) - (achatLayout.width / 2);
        font.draw(batch, achatText, achatX, sectionY);


        // Dessiner "Vente"
        String venteText = "Vente";
        GlyphLayout venteLayout = new GlyphLayout(font, venteText);
        float venteX = frameX + (3 * frameWidth / 4) - (venteLayout.width / 2);
        font.draw(batch, venteText, venteX, sectionY);


        batch.end();

        // Barre de séparation sous "Achat" prenant toute la largeur
        float achatSeparatorY = sectionY - 20; // Position de la barre sous Achat
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(frameX, achatSeparatorY - lineWidth / 2, frameWidth, lineWidth); // Toute la largeur du cadre
        shapeRenderer.end();

        // Barre de séparation sous "Magasin"
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        float separatorY = textTopY - layout.height * 2f - 10; // Adapter à la taille du texte
        shapeRenderer.rect(frameX, separatorY - lineWidth / 2, frameWidth, lineWidth); // Toute la largeur du cadre
        shapeRenderer.end();


        // Colonnes de séparation verticales ajustées
        float verticalLineEndY = achatSeparatorY - lineWidth / 2; // Arrêter les lignes verticales ici
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float categoryObjectSeparatorX = frameX - 80 + frameWidth / 3;
        float objectDetailSeparatorX = frameX - 80 + (2 * frameWidth / 3);

        shapeRenderer.rect(categoryObjectSeparatorX - lineWidth / 2, frameY, lineWidth, verticalLineEndY - frameY); // Catégories
        shapeRenderer.rect(objectDetailSeparatorX - lineWidth / 2, frameY, lineWidth, verticalLineEndY - frameY);  // Objets
        shapeRenderer.end();

        float achatVenteSeparatorX = frameX + frameWidth / 2; // Position centrale entre "Achat" et "Vente"
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(achatVenteSeparatorX - lineWidth / 2, separatorY, lineWidth, achatSeparatorY - separatorY); // Dessiner la barre verticale
        shapeRenderer.end();

        batch.begin();


        // Affiche les catégories
        drawCategoriesMenu(frameX, frameY-30, frameHeight);

        // Affiche les objets
        drawObjectsMenu(frameX - 45, frameY-30, frameHeight);

        // Affiche les détails de l'objet sélectionné
        drawItemDetails(objectDetailSeparatorX + 10, frameY-30, frameHeight);

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


            font.draw(batch, prefix + item.getName() + " (x" + item.getQuantity() + ")" , frameX + 220, textY);
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

    //partie initialisation des touche
    public void update() {
        List<Item> currentItems = getItemsByType(categories[selectedCategoryIndex]);

        if(showShopFrame){
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
                if (!inCategorySelection) {

                    if (!currentItems.isEmpty() && selectedObjectIndex < currentItems.size()) {
                        Item selectedItem = currentItems.get(selectedObjectIndex);

                        if (selectedItem.getValeur() <= Inventory.getPiece()) {

                            Inventory.setArgent(Inventory.getPiece() - selectedItem.getValeur());

                            // Ajouter ou mettre à jour l'item dans l'inventaire
                            addItemToInventory(selectedItem);

                            Inventory.loadInventoryFromJson();

                            // Optionnel : Message pour informer que l'item a été ajouté
                            System.out.println(selectedItem.getName() + "l'objet a été acheté ");
                        }

                    }
                }
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {

                Vente.setShowVenteFrame(true);// Active le cadre d'inventaire
                showShopFrame = false;//inverse true en false et inversement
                Shop.setShowShopFrame(false);


                Vente.loadVenteFromJson();

            }

        }



    }



    public static boolean getShowShopFrame() {
        return showShopFrame;
    }

    public static void setShowShopFrame(boolean show) {
        showShopFrame = show;
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }


}
