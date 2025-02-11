package com.empire.rpg.entity;

import com.empire.rpg.component.Component;
import java.util.Map;
import java.util.UUID;

/**
 * Classe abstraite représentant une entité dans le jeu.
 * Une entité est identifiée de manière unique par un UUID et contient une collection de composants.
 */
public abstract class Entity extends EntityManager {
    private final UUID id;  // Identifiant unique de l'entité
    protected final Map<Class<? extends Component>, Component> components; // Map des composants

    /**
     * Constructeur de l'entité.
     *
     * @param name       Le nom de l'entité.
     * @param components La map des composants de l'entité.
     * @param id         L'identifiant unique de l'entité.
     */
    public Entity(String name, Map<Class<? extends Component>, Component> components, UUID id) {
        super(name);
        this.components = components;
        this.id = id;
    }

    /**
     * Ajouter une entité.
     *
     * @return L'entité ajoutée.
     */
    public abstract Entity addEntity();

    /**
     * Supprimer une entité par son nom.
     *
     * @param name Le nom de l'entité à supprimer.
     * @return L'entité supprimée.
     */
    public abstract Entity removeEntity(String name);

    /**
     * Crée une entité avec un identifiant donné.
     *
     * @param id L'identifiant de l'entité à créer.
     */
    @Override
    public void createEntity(UUID id) {
        System.out.println("Creating entity with id: " + id);
    }

    /**
     * Retourne l'identifiant unique de l'entité.
     *
     * @return L'identifiant unique de l'entité.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Retourne la map des composants de l'entité.
     *
     * @return La map des composants de l'entité.
     */
    public Map<Class<? extends Component>, Component> getComponents() {
        return components;
    }

    /**
     * Ajoute un composant à l'entité.
     *
     * @param value Le composant à ajouter.
     */
    public void addComponent(Component value) {
        components.put(value.getClass(), value);
    }

    /**
     * Supprime un composant de l'entité.
     *
     * @param component La classe du composant à supprimer.
     */
    public void removeComponent(Class<? extends Component> component) {
        components.remove(component);
    }

    /**
     * Retourne un composant de l'entité.
     *
     * @param component La classe du composant à retourner.
     * @return Le composant correspondant à la classe donnée.
     */
    public Component getComponent(Class<? extends Component> component) {
        return components.get(component);
    }

    /**
     * Met à jour un composant de l'entité.
     *
     * @param component La classe du composant à mettre à jour.
     * @param value     Le nouveau composant.
     */
    public void updateComponent(Class<? extends Component> component, Component value) {
        components.replace(component, value);
    }

    /**
     * Met à jour les composants de l'entité.
     *
     * @param components La map des nouveaux composants.
     */
    public void updateComponents(Map<Class<? extends Component>, Component> components) {
        this.components.putAll(components);
    }

    /**
     * Supprime tous les composants de l'entité.
     */
    public void removeComponents() {
        components.clear();
    }

    /**
     * Met à jour l'entité avec les composants d'une autre entité.
     *
     * @param entity L'entité source des composants.
     */
    public void updateEntity(Entity entity) {
        this.components.putAll(entity.getComponents());
    }

    /**
     * Supprime l'entité en vidant ses composants.
     */
    public void removeEntity() {
        components.clear();
    }

    /**
     * Retourne le nom de l'entité.
     *
     * @return Le nom de l'entité.
     */
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * Définit un nouveau nom pour l'entité.
     *
     * @param name Le nouveau nom de l'entité.
     */
    @Override
    public void setName(String name) {
        super.setName(name);
    }
}
