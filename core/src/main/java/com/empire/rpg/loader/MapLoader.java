package com.empire.rpg.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Classe MapLoader pour charger et gérer les entités et objets d'une carte Tiled.
 */
public class MapLoader {

    private final String mapPath;

    /**
     * Constructeur de la classe MapLoader.
     *
     * @param mapPath Chemin vers le fichier de la carte Tiled.
     */
    public MapLoader(String mapPath) {
        this.mapPath = mapPath;
    }

    /**
     * Charge la carte Tiled à partir du fichier spécifié.
     *
     * @return L'objet TiledMap chargé.
     * @throws Exception Si une erreur survient lors du chargement de la carte.
     */
    public TiledMap loadMap() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(mapPath), TiledMap.class);
    }
}
