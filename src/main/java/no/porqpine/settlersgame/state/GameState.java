package no.porqpine.settlersgame.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.porqpine.settlersgame.state.Edge.Orientation.HORIZONTAL;
import static no.porqpine.settlersgame.state.Edge.Orientation.VERTICAL;

public class GameState {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;

    private Tile[][] tiles = new Tile[WIDTH][HEIGHT];
    public List<Player> players = new ArrayList<>();
    public List<Edge> edges = new ArrayList<>();
    public List<Crossing> crossings = new ArrayList<>();

    public GameState() {
        int id = 0;

        //Create tiles
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = new Tile(id++, x, y);
            }
        }

        //Add Crossings and Edges

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                Tile currentTile = tiles[x][y];
                Tile east = null;
                Tile south = null;
                Tile southEast = null;
                if (x < WIDTH - 1) {
                    east = tiles[x + 1][y];
                    Edge edge = new Edge(id++, currentTile, east, VERTICAL);
                    edges.add(edge);
                    currentTile.setE(edge);
                    east.setW(edge);
                }

                if (y > 0) {
                    Tile north = tiles[x][y - 1];
                    Edge edge = new Edge(id++, north, currentTile, HORIZONTAL);
                    edges.add(edge);
                    currentTile.setN(edge);
                    north.setS(edge);
                }

                if (x < WIDTH - 1 && y < HEIGHT - 1) {
                    Crossing crossing = new Crossing(id++, southEast, south, east, currentTile);
                    crossings.add(crossing);
                }

            }
        }
    }

    public List<Tile> getTiles() {
        return Stream.of(tiles).flatMap(row -> Stream.of(row)).collect(Collectors.toList());
    }
}
