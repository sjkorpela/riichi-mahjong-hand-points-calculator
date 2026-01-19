package com.sjkorpela.RiichiPointsCalculator;

import com.sjkorpela.RiichiPointsCalculator.entities.PointsRequestBody;
import com.sjkorpela.RiichiPointsCalculator.enums.Tile;
import com.sjkorpela.RiichiPointsCalculator.entities.ValidPointsRequest;
import com.sjkorpela.RiichiPointsCalculator.enums.Wind;

import java.util.Arrays;
import java.util.HashMap;

public class PointsService {

    public static ValidPointsRequest validateRequest(PointsRequestBody request) throws IllegalArgumentException {
        ValidPointsRequest result = new ValidPointsRequest();
        HashMap<Tile, Integer> spentTiles = new HashMap<Tile, Integer>();

//        result.setHand(Arrays.stream(request.hand).map(Tile::valueOf).toList());
        result.setDora(Arrays.stream(request.getDora()).map(Tile::valueOf).toList());


//        for (Tile tile : result.getHand()) {
//            spentTiles.put(tile, spentTiles.getOrDefault(tile, 0) + 1);
//        }
        for (Tile tile : result.getDora()) {
            spentTiles.put(tile, spentTiles.getOrDefault(tile, 0) + 1);
        }

        System.out.println(spentTiles);

        for (Tile tile : spentTiles.keySet()) {
            if (spentTiles.get(tile) > 4) { throw new IllegalArgumentException("Too many of tile: " + tile); }
        }

        result.setRoundWind(Wind.valueOf(request.getRoundWind()));
        result.setSeatWind(Wind.valueOf(request.getSeatWind()));
        result.setOpenHand(request.getOpenHand());

        return result;
    }

}
