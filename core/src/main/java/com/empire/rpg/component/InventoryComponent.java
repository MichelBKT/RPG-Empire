package com.empire.rpg.component;

import com.empire.rpg.entity.Item;

/**
 * Composant d'inventaire pour gérer les objets d'un joueur.
 */
public class InventoryComponent implements Component {
    private Item itemName;
    private Item itemQuantity;
    private Item itemType;

    /**
     * Constructeur pour initialiser les objets de l'inventaire.
     *
     * @param itemName Nom de l'objet.
     * @param itemQuantity Quantité de l'objet.
     * @param itemType Type de l'objet.
     */
    public InventoryComponent(Item itemName, Item itemQuantity, Item itemType) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemType = itemType;
    }

    /**
     * Retourne la réduction de dégâts.
     *
     * @return Réduction de dégâts (toujours 0).
     */
    @Override
    public int getDamageReduction() {
        return 0;
    }

    /**
     * Définit les points de vie actuels.
     *
     * @param i Points de vie actuels.
     */
    @Override
    public void setCurrentHealthPoints(int i) {

    }

    /**
     * Retourne les points de vie actuels.
     *
     * @return Points de vie actuels (toujours 0).
     */
    @Override
    public int getCurrentHealthPoints() {
        return 0;
    }

    /**
     * Retourne le nom de l'objet.
     *
     * @return Nom de l'objet.
     */
    public Item getItemName() {
        return itemName;
    }

    /**
     * Définit le nom de l'objet.
     *
     * @param itemName Nom de l'objet.
     */
    public void setItemName(Item itemName) {
        this.itemName = itemName;
    }

    /**
     * Retourne la quantité de l'objet.
     *
     * @return Quantité de l'objet.
     */
    public Item getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Définit la quantité de l'objet.
     *
     * @param itemQuantity Quantité de l'objet.
     */
    public void setItemQuantity(Item itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * Retourne le type de l'objet.
     *
     * @return Type de l'objet.
     */
    public Item getItemType() {
        return itemType;
    }

    /**
     * Définit le type de l'objet.
     *
     * @param itemType Type de l'objet.
     */
    public void setItemType(Item itemType) {
        this.itemType = itemType;
    }

    /**
     * Ajoute un objet à l'inventaire.
     *
     * @param itemName Nom de l'objet.
     * @param itemQuantity Quantité de l'objet.
     * @param itemType Type de l'objet.
     */
    public void addItem(Item itemName, Item itemQuantity, Item itemType) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.itemType = itemType;
    }

    /**
     * Supprime l'objet de l'inventaire.
     */
    public void removeItem() {
        this.itemName = null;
        this.itemQuantity = null;
        this.itemType = null;
    }
}
