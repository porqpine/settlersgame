package no.porqpine.settlersgame.state.tiles;

import no.porqpine.settlersgame.Game;
import no.porqpine.settlersgame.state.PheromoneType;
import no.porqpine.settlersgame.state.Player;

import java.util.Optional;

public class BlockerTile extends OwnedTile {

    public static final int COST = 500;

    public BlockerTile(int x, int y, Player owner, Game game) {
        super(x, y, owner, game);
    }

    @Override
    public String getType() {
        return "BLOCKER";
    }

    @Override
    public boolean acceptsPheromone(PheromoneType pheromoneType) {
        return false;
    }

    @Override
    public void diffuse() {
        //Nothing spreads from this
    }

    @Override
    public void degrade() {
        Optional<PheromoneType> highestPlayerPheromone = pAmounts.keySet().stream().max((p1, p2) -> pAmounts.get(p1).compareTo(pAmounts.get(p2)));
        if(highestPlayerPheromone.isPresent()){
            owner = highestPlayerPheromone.get().player.get();
        }
        pAmounts.keySet().forEach(pheromoneType -> adjustPheromone(pheromoneType, -pAmounts.get(pheromoneType)));
    }

    @Override
    public long cost() {
        return COST;
    }

    @Override
    public void tick(int i) {
    }
}