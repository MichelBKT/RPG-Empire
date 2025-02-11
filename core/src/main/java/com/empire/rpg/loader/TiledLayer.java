package com.empire.rpg.loader;

import java.util.List;

public class TiledLayer {
    private List<TiledObject> objects;

    public TiledLayer(List<TiledObject> objects) {
        this.objects = objects;
    }

    public List<TiledObject> getObjects() {
        return objects;
    }

    public void setObjects(List<TiledObject> objects) {
        this.objects = objects;
    }

}
