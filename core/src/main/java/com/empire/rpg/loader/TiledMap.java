package com.empire.rpg.loader;

import java.util.List;

public class TiledMap {
    private List<TiledLayer> layers;

    public TiledMap(List<TiledLayer> layers) {
        this.layers = layers;
    }

    public List<TiledLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<TiledLayer> layers) {
        this.layers = layers;
    }
}
