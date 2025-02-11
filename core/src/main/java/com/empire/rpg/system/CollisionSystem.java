package com.empire.rpg.system;

import com.empire.rpg.component.Component;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.entity.Entity;

/**
 * Système de Collision (CollisionSystem)
 * Ce système détecte les collisions entre entités ayant un CollisionComponent. Lorsqu’une collision est détectée,
 * le système peut déclencher des actions spécifiques, comme empêcher le passage ou déclencher une interaction
 * (par exemple, un combat ou une ouverture de coffre).
 */

public class CollisionSystem implements GameSystem<Component> {
    private Entity entity;
    private PositionComponent positionComponent;
    private boolean isColliding;
    private boolean isBlocking;

    public CollisionSystem(Entity entity){
        this.entity = entity;
        this.positionComponent = (PositionComponent) entity.getComponent(PositionComponent.class);
        this.isColliding = false;
        this.isBlocking = false;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public void setPositionComponent(PositionComponent positionComponent) {
        this.positionComponent = positionComponent;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean isColliding) {
        this.isColliding = isColliding;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean blocking) {
        isBlocking = blocking;
    }

    public void detectCollision(){
        if (isColliding()){
            // Actions à effectuer en cas de collision
            setBlocking(true);
        }else {
            setBlocking(false);
        }
    }

    public void collided(){
        // Actions à effectuer en cas de collision
        setColliding(true);
    }

    public void uncollided(){
        // Actions à effectuer en cas de fin de collision
        setColliding(false);
    }

    public void block(){
        // Actions à effectuer en cas de blocage
        if (isBlocking()){
            System.out.println("Collision détectée, le passage est bloqué.");
        }else {
            System.out.println("Pas de collision détectée, le passage est libre.");
        }
    }

    public void unblock(){
        // Actions à effectuer en cas de déblocage
        if (!isBlocking()){
            System.out.println("Pas de collision détectée, le passage est libre.");
        }else {
            System.out.println("Collision détectée, le passage est bloqué.");
        }
    }

    public void interact(){
        // Actions à effectuer en cas d'interaction
        if (isColliding()){
            System.out.println("Collision détectée, interaction possible.");
        }else {
            System.out.println("Pas de collision détectée, pas d'interaction possible.");
        }
    }

    @Override
    public void update(Component component) {
        detectCollision();
        collided();
        uncollided();
        block();
        unblock();
        interact();
    }
}
