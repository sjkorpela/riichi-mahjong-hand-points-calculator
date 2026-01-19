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
//        System.out.println("Checking for flags...");

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
//        System.out.println("Checking for Thirteen Orphans...");

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

    public static void checkFlushYaku(PointsRequest request) {
//        System.out.println("Checking for flushes...");

        List<Tile> hand = request.getFullHandAsList();

        boolean hasHonors = false;
        Suit suit = null;

        for (Tile tile : hand) {
            if (!hasHonors && tile.getType() == Type.Honor) {
                hasHonors = true;
            } else if (suit == null && tile.getType() != Type.Honor) {
                suit = tile.getSuit();
            } else if (suit != null && tile.getType() != Type.Honor && tile.getSuit() != suit) {
                return;
            }
        }

        // save for later
        request.setFlushSuit(suit);

        // if hand has honors it's a half flush, otherwise it's a full flush
        if (hasHonors) {
            request.getYaku().add(Yaku.HalfFlush);
        } else {
            request.getYaku().add(Yaku.FullFlush);
        }
    }

    public static void checkForNineGates(PointsRequest request) {
//        System.out.println("Checking for Nine Gates...");

        List<Tile> gates = Tile.getAllTilesBySuit(request.getFlushSuit());

        Tile missing = null;

        // this got a bit messy, maybe because am tired, nts: read through this sometime

        for (Tile gate : gates) {
            int amount = request.getHand().getOrDefault(gate, 0);

//            System.out.println(gate);

//            System.out.println("Terminal short: " + (gate.getType() == Type.Terminal && amount <= 2 && request.getWinningTile() != gate));
            // 9g requires three of both terminals but one can be the winning tile
            if (gate.getType() == Type.Terminal && amount <= 2 && request.getWinningTile() != gate) { return; }

//            System.out.println("Missing but not winning: " + (missing != null && request.getWinningTile().getValue() != missing.getValue()));
            // if a tile is missing but not the winning tile, hand can't be 9n
            if (missing != null && request.getWinningTile().getValue() != missing.getValue() ) { return; }

            // check if missing tile is 5 and current tile is r5 and is
            // and if so mark 5 as not missing
//            System.out.println("Red gate replaces missing: " + (gate.getRed() && missing != null && missing.getValue() == gate.getValue() && amount > 0));
            if (gate.getRed() && missing != null && missing.getValue() == gate.getValue() && amount > 0) {
                missing = null;
            }

//            System.out.println("Double missing: " + (missing != null && amount == 0));
            if (missing != null && amount == 0 && missing.getValue() != gate.getValue()) { return; }

//            System.out.println("Found missing: " + (missing == null && amount == 0 && !gate.getRed()));
            if (missing == null && amount == 0 && !gate.getRed()) { missing = gate; }
        }

        // loop fail states:
            // hand has less than 3 of a terminal, but the winning tile isn't the missing terminal
            // hand has a missing simple, but the winning tile isn't that missing simple
            // hand has a missing simple, and discovers another missing simple
        // so, hand must have:
            // 3 of both terminals, or 2 of one terminal with the third as the winning tile
            // one missing tile that is the winning tile
            // at most one missing tile of 1-9
        // so everything should be covered

        // if no tiles are missing, hand is t9n, otherwise only 9n
        if (missing == null) {
            request.getYaku().add(Yaku.TrueNineGates);
            request.setYakumanAchieved(true);
        } else {
            request.getYaku().add(Yaku.NineGates);
            request.setYakumanAchieved(true);
        }
    }

    public static void checkForAllGreen(PointsRequest request) {

    }
}
