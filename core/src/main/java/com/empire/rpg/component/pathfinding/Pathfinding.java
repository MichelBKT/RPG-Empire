package com.empire.rpg.component.pathfinding;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.empire.rpg.map.CollisionManager;
import com.empire.rpg.entity.mob.Mob;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.*;

/**
 * Classe Pathfinding utilisant l'algorithme A* pour calculer un chemin pour les mobs.
 * Cette classe inclut également une limitation de portée de recherche et un rendu optionnel pour le débogage.
 */
public class Pathfinding {
    private final CollisionManager collisionManager;
    private final ShapeRenderer shapeRenderer;
    private static final int TILE_SIZE = 48;
    private static final int MAX_SEARCH_DISTANCE = 6; // Limite de distance en nombre de cellules

    public Pathfinding(CollisionManager collisionManager) {
        this.collisionManager = collisionManager;
        this.shapeRenderer = new ShapeRenderer();
    }

    /**
     * Nœud pour représenter chaque position dans la grille lors du calcul de chemin.
     */
    private static class Node implements Comparable<Node> {
        Vector2 tile;
        Node parent;
        float gCost, hCost; // Coûts pour A*

        public Node(Vector2 tile, Node parent, float gCost, float hCost) {
            this.tile = tile;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        public float fCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(Node other) {
            return Float.compare(this.fCost(), other.fCost());
        }
    }

    /**
     * Calcule un chemin entre le mob et la position cible.
     * @param mob Mob pour lequel le chemin est calculé
     * @param targetPixel Position cible en pixels
     */
    public void calculatePath(Mob mob, Vector2 targetPixel) {
        if (targetPixel == null) return; // Aucun chemin à calculer si la cible est nulle

        List<Vector2> path = new ArrayList<>();
        Vector2 startTile = pixelToTile(mob.getPosition());
        Vector2 targetTile = pixelToTile(targetPixel);

        if (startTile.dst(targetTile) > MAX_SEARCH_DISTANCE) {
            mob.setPath(path); // Si la cible est trop éloignée, aucun chemin
            return;
        }

        Set<Vector2> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Vector2, Node> allNodes = new HashMap<>(); // Pour éviter les doublons

        Node startNode = new Node(startTile, null, 0, heuristic(startTile, targetTile));
        openSet.add(startNode);
        allNodes.put(startTile, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.tile.equals(targetTile)) {
                reconstructPath(path, current);
                mob.setPath(path);
                return;
            }

            closedSet.add(current.tile);

            for (Vector2 neighbor : getNeighbors(current.tile)) {
                if (closedSet.contains(neighbor) || isTileBlocked(neighbor) || startTile.dst(neighbor) > MAX_SEARCH_DISTANCE) {
                    continue;
                }

                float tentativeGCost = current.gCost + 1;
                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor, current, tentativeGCost, heuristic(neighbor, targetTile)));

                if (tentativeGCost < neighborNode.gCost || !allNodes.containsKey(neighbor)) {
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.parent = current;

                    if (!allNodes.containsKey(neighbor)) {
                        openSet.add(neighborNode);
                        allNodes.put(neighbor, neighborNode);
                    }
                }
            }
        }

        mob.setPath(path); // Aucun chemin trouvé
    }

    /**
     * Calcule l'heuristique pour A* (distance estimée entre deux tuiles).
     * @param tile Position actuelle
     * @param target Position cible
     * @return Estimation de la distance de déplacement entre les deux tuiles
     */
    private float heuristic(Vector2 tile, Vector2 target) {
        float dx = Math.abs(tile.x - target.x);
        float dy = Math.abs(tile.y - target.y);
        return TILE_SIZE * (dx + dy) + (1.414f - 2f) * TILE_SIZE * Math.min(dx, dy);
    }

    /**
     * Reconstruit le chemin en suivant les parents des nœuds.
     * @param path Liste dans laquelle le chemin reconstruit est stocké
     * @param current Nœud actuel
     */
    private void reconstructPath(List<Vector2> path, Node current) {
        while (current != null) {
            path.add(tileToPixel(current.tile));
            current = current.parent;
        }
        Collections.reverse(path);
    }

    /**
     * Vérifie si une tuile est bloquée pour le mob (collision).
     * @param tile Position de la tuile
     * @return true si la tuile est bloquée, false sinon
     */
    public boolean isTileBlocked(Vector2 tile) {
        Rectangle tileRect = new Rectangle(tile.x * TILE_SIZE, tile.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        return collisionManager.isColliding(tileRect);
    }

    /**
     * Obtient les tuiles voisines de la position actuelle.
     * @param tile Position de la tuile
     * @return Liste des positions des voisins
     */
    private List<Vector2> getNeighbors(Vector2 tile) {
        List<Vector2> neighbors = new ArrayList<>(Arrays.asList(
            new Vector2(tile.x + 1, tile.y),
            new Vector2(tile.x - 1, tile.y),
            new Vector2(tile.x, tile.y + 1),
            new Vector2(tile.x, tile.y - 1)
        ));
        return neighbors;
    }

    /**
     * Convertit une position en pixels en coordonnées de tuile.
     * @param pixel Position en pixels
     * @return Coordonnées de tuile correspondantes
     */
    public Vector2 pixelToTile(Vector2 pixel) {
        return new Vector2((int) (pixel.x / TILE_SIZE), (int) (pixel.y / TILE_SIZE));
    }

    /**
     * Convertit une position en coordonnées de tuile en pixels.
     * @param tile Coordonnées de tuile
     * @return Position en pixels correspondante
     */
    public Vector2 tileToPixel(Vector2 tile) {
        return new Vector2(tile.x * TILE_SIZE, tile.y * TILE_SIZE);
    }

    /**
     * Calcule un chemin entre deux points sans interaction directe avec un mob.
     * @param start Point de départ en pixels
     * @param end Point de destination en pixels
     * @return Une liste de positions en pixels représentant le chemin
     */
    public List<Vector2> calculateSegmentPath(Vector2 start, Vector2 end) {
        List<Vector2> path = new ArrayList<>();
        Vector2 startTile = pixelToTile(start);
        Vector2 targetTile = pixelToTile(end);

        // Vérifie si la distance dépasse la limite
        if (startTile.dst(targetTile) > MAX_SEARCH_DISTANCE) {
            return path; // Retourne un chemin vide si la distance dépasse la limite
        }

        Set<Vector2> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Map<Vector2, Node> allNodes = new HashMap<>();

        Node startNode = new Node(startTile, null, 0, heuristic(startTile, targetTile));
        openSet.add(startNode);
        allNodes.put(startTile, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // Si le nœud actuel est la cible, reconstruire le chemin
            if (current.tile.equals(targetTile)) {
                reconstructPath(path, current);
                return path;
            }

            closedSet.add(current.tile);

            for (Vector2 neighbor : getNeighbors(current.tile)) {
                if (closedSet.contains(neighbor) || isTileBlocked(neighbor) || startTile.dst(neighbor) > MAX_SEARCH_DISTANCE) {
                    continue;
                }

                float tentativeGCost = current.gCost + 1;
                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor, current, tentativeGCost, heuristic(neighbor, targetTile)));

                if (tentativeGCost < neighborNode.gCost || !allNodes.containsKey(neighbor)) {
                    neighborNode.gCost = tentativeGCost;
                    neighborNode.parent = current;

                    if (!allNodes.containsKey(neighbor)) {
                        openSet.add(neighborNode);
                        allNodes.put(neighbor, neighborNode);
                    }
                }
            }
        }

        // Retourne un chemin vide si aucun chemin n'a été trouvé
        return path;
    }

    /**
     * Libère les ressources utilisées par le ShapeRenderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}
