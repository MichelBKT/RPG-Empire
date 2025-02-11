package com.empire.rpg.system;

import com.empire.rpg.component.Component;
import com.empire.rpg.component.PositionComponent;
import com.empire.rpg.entity.Entity;


/**
 * Système de Mouvement (MovementSystem)
 * Ce système est responsable de mettre à jour les positions des entités mobiles (par exemple, le Player et les MOB).
 * En fonction de la vitesse et de la direction définies dans leur MovementComponent, ce système modifie leur PositionComponent.
 */

public class MovementSystem implements GameSystem<Component> {
    private Entity mobileEntity;
    private PositionComponent positionComponent;
    private float speed;
    private float direction;

    public MovementSystem(Entity mobileEntity){
        this.mobileEntity = mobileEntity;
        this.positionComponent = (PositionComponent) mobileEntity.getComponent(PositionComponent.class);
        this.speed = getSpeed();
        this.direction = getDirection();
    }

    public Entity getMobileEntity() {
        return mobileEntity;
    }

    public void setMobileEntity(Entity mobileEntity) {
        this.mobileEntity = mobileEntity;
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
    }

    public void setPositionComponent(PositionComponent positionComponent) {
        this.positionComponent = positionComponent;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void moved(){
        // Actions à effectuer en cas de mouvement
        if (getSpeed() > 0){
            // Déplacement de l'entité
            float x = getPositionComponent().getX() + getSpeed() * (float) Math.cos(Math.toRadians(getDirection()));
            float y = getPositionComponent().getY() + getSpeed() * (float) Math.sin(Math.toRadians(getDirection()));
            getPositionComponent().setPosition(x, y);

            // Affichage de la nouvelle position
            System.out.println("X: " + getPositionComponent().getX() + " Y: " + getPositionComponent().getY());

        }else {
            System.out.println("Vitesse nulle, l'entité ne se déplace pas (immobile).");
        }
    }

    public void move(float speed, float direction){
        setSpeed(speed);
        setDirection(direction);
        moved();
    }

    public void stop(){
        setSpeed(0);
        moved();
    }

    public void turn(float direction){
        setDirection(direction);
        moved();
    }

    public void moveForward(float speed){
        move(speed, getDirection());
    }

    public void moveBackward(float speed){
        move(speed, getDirection() + 180);
    }

    public void turnLeft(float angle){
        turn(getDirection() - angle);
    }

    public void turnRight(float angle){
        turn(getDirection() + angle);
    }

    public void moveLeft(float speed){
        move(speed, getDirection() - 90);
    }

    public void moveRight(float speed){
        move(speed, getDirection() + 90);
    }

    public void moveUp(float speed){
        move(speed, 0);
    }

    public void moveDown(float speed){
        move(speed, 180);
    }

    public void moveUpLeft(float speed){
        move(speed, -45);
    }

    public void moveUpRight(float speed){
        move(speed, 45);
    }

    public void moveDownLeft(float speed){
        move(speed, -135);
    }

    public void moveDownRight(float speed){
        move(speed, 135);
    }

    public void moveRandom(float speed){
        move(speed, (float) (Math.random() * 360));
    }

    public void moveRandom(){
        moveRandom(getSpeed());
    }

    public void moveRandom(float minSpeed, float maxSpeed){
        moveRandom((float) (Math.random() * (maxSpeed - minSpeed) + minSpeed));
    }

    public void moveRandom(float minSpeed, float maxSpeed, float minDirection, float maxDirection){
        moveRandom((float) (Math.random() * (maxSpeed - minSpeed) + minSpeed), (float) (Math.random() * (maxDirection - minDirection) + minDirection));
    }

    public void moveRandom(float minSpeed, float maxSpeed, float minDirection, float maxDirection, int nbMovements){
        for (int i = 0; i < nbMovements; i++){
            moveRandom(minSpeed, maxSpeed, minDirection, maxDirection);
        }
    }

    @Override
    public void update(Component component) {
        // Logique de mise à jour du mouvement
        moved();
        move(speed, direction);
        stop();
        turn(direction);
        moveForward(speed);
        moveBackward(speed);
        turnLeft(direction);
        turnRight(direction);
        moveLeft(speed);
        moveRight(speed);
        moveUp(speed);
        moveDown(speed);
        moveUpLeft(speed);
        moveUpRight(speed);
    }
}
