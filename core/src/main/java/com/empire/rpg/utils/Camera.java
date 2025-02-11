package com.empire.rpg.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Classe Camera pour gérer les mouvements et la configuration de la caméra.
 */
public class Camera {
    private final OrthographicCamera camera;
    private float viewportWidth;
    private float viewportHeight;


    /**
     * Constructeur de la classe Camera.
     *
     * @param viewportWidth  Largeur de la zone visible
     * @param viewportHeight Hauteur de la zone visible
     */
    public Camera(float viewportWidth, float viewportHeight) {
        this.camera = new OrthographicCamera();
    }

    /**
     * Centrer la caméra sur une position donnée.
     *
     */
    public void centerOn(float viewportWidth, float viewportHeight) {
        camera.position.set(viewportWidth, viewportHeight, 0);
        camera.update();
    }

    /**
     * Déplacer la caméra d'une certaine distance.
     *
     * @param deltaX Distance horizontale
     * @param deltaY Distance verticale
     */
    public void move(float deltaX, float deltaY) {
        camera.translate(deltaX, deltaY);
        camera.update();
    }

    /**
     * Zoomer ou dézoomer la caméra.
     *
     * @param zoomFactor Facteur de zoom (plus grand que 1 pour dézoomer, entre 0 et 1 pour zoomer)
     */
    public void zoom(float zoomFactor) {
        camera.zoom = Math.max(0.1f, camera.zoom * zoomFactor); // Empêche un zoom trop proche
        camera.update();
    }

    /**
     * Récupérer la caméra OrthographicCamera.
     *
     * @return Instance de OrthographicCamera.
     */
    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setToOrtho(boolean b, int mapPixelWidth, int mapPixelHeight) {
        camera.setToOrtho(b, mapPixelWidth, mapPixelHeight);
    }

    public void update() {
        camera.update();
    }
}
