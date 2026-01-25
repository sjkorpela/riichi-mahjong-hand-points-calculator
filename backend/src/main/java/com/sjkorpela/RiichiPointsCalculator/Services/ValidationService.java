package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ValidationService {

    private static final int fullHandSize = 13;
    private static final int maxDoraCount = 10;
    private static final int maxTileAmount = 4;
    private static final int redFiveTileAmount = 1;

    public static void validatePointsRequest(PointsRequest request) {
        if (request.getHand() == null ) { throw new IllegalArgumentException("Hand wasn't provided."); }
        if (request.getWinningTile() == null ) { throw new IllegalArgumentException("Winning Tile wasn't provided."); }
        if (request.getRoundWind() == null ) { throw new IllegalArgumentException("Round Wind wasn't provided."); }
        if (request.getSeatWind() == null ) { throw new IllegalArgumentException("Seat Wind wasn't provided."); }
        if (request.getDora() == null ) { throw new IllegalArgumentException("Dora wasn't provided."); }
        if (request.getOpenHand() == null ) { throw new IllegalArgumentException("Hand State wasn't provided."); }
        if (request.getTsumo() == null ) { throw new IllegalArgumentException("Win Declaration wasn't provided."); }
        if (request.getFlags() == null ) { throw new IllegalArgumentException("Flags wasn't provided. If nothing is flagged, send an empty object."); }

        Tile[] dora = request.getDora();
        HashMap<Tile, Integer> hand = request.getHand();
        HashMap<Tile, Integer> spentTiles = new HashMap<Tile, Integer>();

        int handSize = 0;
        for (Map.Entry<Tile, Integer> entry : hand.entrySet()) {
            Tile tile = entry.getKey();
            Integer count = entry.getValue();
            spentTiles.merge(tile, count, Integer::sum);
            handSize += count;
        }
        if (handSize != fullHandSize) { throw new IllegalArgumentException("Hand must be 13 tiles total. Winning Tile is a separate field."); }

        if (dora.length > maxDoraCount) { throw new IllegalArgumentException("Too many Dora provided. Limit is 10."); }
        Arrays.stream(dora).forEach((tile) -> {
            spentTiles.merge(tile, 1, Integer::sum);
        });

        spentTiles.put(request.getWinningTile(), 1);

        for (Map.Entry<Tile, Integer> entry : spentTiles.entrySet()) {
            Tile tile = entry.getKey();
            Integer count = entry.getValue();
            if (tile.getRed() && count > redFiveTileAmount) { throw new IllegalArgumentException(count + " of tile " + tile.getReadableName() + " is too many. Limit is " + redFiveTileAmount + " for red fives."); }
            else if (tile.getValue() == 5 && count > maxTileAmount - redFiveTileAmount) { throw new IllegalArgumentException(count + " of tile " + tile.getReadableName() + " is too many. Limit is " + (maxTileAmount - redFiveTileAmount) + " because of red fives."); }
            else if (count > maxTileAmount) { throw new IllegalArgumentException(count + " of tile " + tile.getReadableName() + " is too many. Limit is " + maxTileAmount + " for most tiles."); }
        }
    }
}
