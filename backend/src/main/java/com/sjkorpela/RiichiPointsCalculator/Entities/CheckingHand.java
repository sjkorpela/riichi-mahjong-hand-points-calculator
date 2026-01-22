package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CheckingHand {
    private List<Tile> tiles;
    private List<Integer> spentTiles;
    private List<Triplet> trips;
    private List<Sequence> sequs;
    private Pair pair = null;
    private boolean ready = false;

    public CheckingHand(List<Tile> tiles) {
        this.tiles = tiles;
        this.spentTiles = new ArrayList<Integer>();
        this.trips = new ArrayList<Triplet>();
        this.sequs = new ArrayList<Sequence>();
    }

    public CheckingHand(CheckingHand old) {
        this.tiles = new ArrayList<Tile>(old.tiles);
        this.spentTiles = new ArrayList<Integer>(old.spentTiles);
        this.trips = new ArrayList<Triplet>(old.trips);
        this.sequs = new ArrayList<Sequence>(old.sequs);
        this.pair = old.pair;
        this.ready = old.ready;
    }

    public Integer getFirstUnspentIndex() {
        for (int i = 0; i < tiles.size(); i++) {
            if (!spentTiles.contains(i)) {
                return i;
            }
        }

        return null;
    }

    public Integer getRemainingTileCount() {
        return tiles.size() - spentTiles.size();
    }

    @Override
    public String toString() {
        String seqStr = "";
        String triStr = "";
        String paiStr = "";
        for (Sequence s : sequs) {
            seqStr += "[" + s.getTiles()[0] + " " + s.getTiles()[1] + " " + s.getTiles()[2] + "]";
        }
        for (Triplet t : trips) {
            triStr += "[" + t.getTile() + " " + t.getTile() + " " +t.getTile() + "]";
        }
        paiStr = "[" + pair.getTile() + " " + pair.getTile() + "]";
        return "{ " + seqStr + triStr + paiStr + ", " + spentTiles.size() + "/" + tiles.size() + " }";
    }
}
