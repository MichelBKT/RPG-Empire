package com.empire.rpg.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapManager {
    private final TiledMap tiledMap;
    private final OrthogonalTiledMapRenderer renderer;

    public MapManager(String mapPath, OrthographicCamera camera) {
        TmxMapLoader mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    // Rendre les couches inférieures
    public void renderLowerLayers(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(new int[] {
            tiledMap.getLayers().getIndex("sol-1"),
            tiledMap.getLayers().getIndex("sol-2"),
            tiledMap.getLayers().getIndex("sol-3"),
            tiledMap.getLayers().getIndex("mur-1"),
            tiledMap.getLayers().getIndex("mur-2"),
            tiledMap.getLayers().getIndex("mur-3"),
            tiledMap.getLayers().getIndex("deco-1"),
            tiledMap.getLayers().getIndex("deco-2"),
        });
    }

    // Rendre les couches supérieures
    public void renderUpperLayers(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render(new int[] {
            tiledMap.getLayers().getIndex("deco-3"),
            tiledMap.getLayers().getIndex("toit-1"),
            tiledMap.getLayers().getIndex("toit-2"),
            tiledMap.getLayers().getIndex("toit-3"),
        });
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
    }
}
