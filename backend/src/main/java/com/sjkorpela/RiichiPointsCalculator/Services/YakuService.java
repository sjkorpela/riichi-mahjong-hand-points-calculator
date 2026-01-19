package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Enums.Suit;
import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import com.sjkorpela.RiichiPointsCalculator.Enums.Type;
import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;

import java.util.List;
import java.util.stream.Stream;

public class YakuService {
    public static void checkFlagsYaku(PointsRequest request) {

        if (request.getFlags().getOrDefault("blessedHand", false)) {
            request.getYaku().add(Yaku.BlessedHand);
            request.setYakumanAchieved(true);
            return;
        }

        if (request.getFlags().getOrDefault("afterKan", false)) {
            request.getYaku().add(Yaku.AfterKan);
        }

        if (request.getFlags().getOrDefault("lastTile", false)) {
            request.getYaku().add(Yaku.LastTile);
        }
    }

    public static void checkThirteenOrphans(PointsRequest request) {
        // get all orphans aka all terminal and honor tiles
        List<Tile> orphans = Stream.concat(
                Tile.getAllTilesByType(Type.Terminal).stream(),
                Tile.getAllTilesByType(Type.Honor).stream()
        ).toList();

        // check which orphan is paired and which one is missing
        Tile pair = null;
        Tile missing = null;

        for (Tile orphan : orphans) {
            int amount = request.getHand().getOrDefault(orphan, 0);

            // if there's more than 2 of any orphan, 13o isn't possible
            if (amount > 2) { return; }

            // if multiple orphans are paired or missing, 13o is not possible
            if (missing != null && amount == 0) { return; }
            else if (pair != null && amount == 2) { return; }
            else if (missing == null && amount == 0) { missing = orphan; }
            else if (pair == null && amount == 2) { pair = orphan; }
        }

        // if only one is null, something went wrong
        if (pair == null ^ missing == null) { throw new IllegalArgumentException("Hand isn't valid."); }

        // if no orphans were paired or missing, and winning tile is an orphan, hand is 13w13o
        // only pair null needs to be checked because of above check
        if (pair == null && orphans.contains(request.getWinningTile())) {
            request.getYaku().add(Yaku.ThirteenWaitThirteenOrphans);
            request.setYakumanAchieved(true);
            return;
        }

        // if the winning tile isn't the missing tile, the hand can't be valid
        // otherwise hand is 13o
        if (request.getWinningTile() != missing) { throw new IllegalArgumentException("Hand isn't valid."); }
        else {
            request.getYaku().add(Yaku.ThirteenOrphans);
            request.setYakumanAchieved(true);
        }
    }
}
