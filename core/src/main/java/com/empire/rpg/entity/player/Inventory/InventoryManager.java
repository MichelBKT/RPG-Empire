package com.empire.rpg.entity.player.Inventory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonParseException;
import com.google.gson.*;
import java.io.*;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class InventoryManager {

    private static final String INVENTORY_FILE = "BDD/inventory.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Méthode pour sauvegarder un item dans l'inventaire JSON.
     *
     * @param itemInfo Les informations de l'item (nom, quantité, description, type).
     */
    public static void saveItemToInventory(Map<String, Object> itemInfo) {
        // Vérification des clés et des valeurs
        if (itemInfo.get("Item-Name") == null || itemInfo.get("Item-Quantity") == null) {
            System.out.println("Erreur : certaines informations d'item sont manquantes.");
            return; // Sortie de la méthode si des informations manquent
        }

        // Charger l'inventaire existant
        JsonArray inventory = loadInventory();

        // Créer un objet pour représenter un item avec la structure demandée
        JsonObject newItem = new JsonObject();

        // Ajout des propriétés principales
        newItem.addProperty("class", "com.empire.rpg.entity.Item");
        newItem.addProperty("name", (String) itemInfo.get("Item-Name"));

        // Ajout des autres propriétés
        newItem.addProperty("type", (String) itemInfo.get("Item-Type"));
        newItem.addProperty("quantity", ((Number) itemInfo.get("Item-Quantity")).intValue());
        newItem.addProperty("description", (String) itemInfo.get("Item-Description"));

        // Vérifier si l'item existe déjà dans l'inventaire
        boolean itemExists = false;
        for (JsonElement element : inventory) {
            JsonObject item = element.getAsJsonObject();
            String itemName = item.get("name").getAsString();

            if (itemName.equals(itemInfo.get("Item-Name"))) {
                // Item existe déjà, on augmente la quantité
                int currentQuantity = item.get("quantity").getAsInt();
                item.addProperty("quantity", currentQuantity + 1);
                itemExists = true;
                break;
            }
        }

        // Si l'item n'existe pas, on l'ajoute
        if (!itemExists) {
            inventory.add(newItem);
        }

        // Sauvegarder l'inventaire modifié
        saveInventory(inventory);
    }



    /**
     * Méthode pour charger l'inventaire à partir du fichier JSON.
     *
     * @return L'inventaire chargé sous forme de JsonArray.
     */
    private static JsonArray loadInventory() {
        try (FileReader reader = new FileReader(INVENTORY_FILE)) {
            JsonArray inventory = gson.fromJson(reader, JsonArray.class);
            return inventory != null ? inventory : new JsonArray();
        } catch (IOException | JsonSyntaxException e) {
            System.out.println("Erreur de lecture ou de syntaxe du fichier JSON : " + e.getMessage());
            return new JsonArray();  // Retourne une liste vide en cas d'erreur
        }
    }




    /**
     * Méthode pour sauvegarder l'inventaire dans le fichier JSON.
     *
     * @param inventory L'inventaire sous forme de JsonArray.
     */
    private static void saveInventory(JsonArray inventory) {
        try (FileWriter writer = new FileWriter(INVENTORY_FILE)) {
            // Sauvegarder l'inventaire dans le fichier JSON
            gson.toJson(inventory, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JsonObject findItemInInventory(JsonArray inventory, String itemName) {
        for (JsonElement element : inventory) {
            JsonObject item = element.getAsJsonObject();
            if (item.get("name").getAsString().equals(itemName)) {
                return item;
            }
        }
        return null;
    }
}
