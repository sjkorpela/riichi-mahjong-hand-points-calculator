package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class PointsResponse {

    private final List<ResponseYaku> yaku;
    private final boolean openHand;

    public PointsResponse(PointsRequest request) {
        this.yaku = new ArrayList<>();
        this.openHand = request.getOpenHand();

        for (Yaku yaku : request.getYaku()) {
            this.yaku.add(new ResponseYaku(
                    "Green Dragon",
                    "Yakuhai",
                    "A triplet of Green Dragons.",
                    1,
                    Arrays.asList(Tile.dg, Tile.dg, Tile.dg)
            ));
        }
    }
}
