package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.*;
import com.sjkorpela.RiichiPointsCalculator.Enums.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class YakuService {
    public static void checkForFlagsYaku(PointsRequest request) {
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

    public static void checkForThirteenOrphans(PointsRequest request) {
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

    public static void checkForFlushYaku(PointsRequest request) {
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

    /**
     * Seven Pairs is a yaku that requires:
     * - The hand to be made of seven pairs
     * <p>
     * Seven Pairs is a unique hand structure, but is still compatible with:
     * - All Honors (Yakuman)
     * - All Terminals and Honors
     * - All Simples
     * - Half/Full Flush
     * And of those some are also compatible.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForSevenPairs(PointsRequest request) {

        for (Map.Entry<Tile, Integer> entry : request.getFullHandAsMap().entrySet()) {
            if (entry.getValue() != 2) { return; }
        }

        request.getYaku().add(Yaku.SevenPairs);
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

    /**
     * All Green is a Yakuman that requires:
     * - The hand to be made entirely of tiles that only have green on them.
     *      - These tiles are Sou 2-4, Sou 6, Sou 8, and the Green Dragon.
     * <p>
     * Some rulesets have different additional requirements, such as:
     * - The hand must have the Green Dragon.
     * - The hand must be four triplets and a pair.
     * But they aren't accounted for here.
     *
     * @param request PointsRequest object that the hand is checked from
     */
    public static void checkForAllGreen(PointsRequest request) {
        ArrayList<Tile> greens = new ArrayList<>(List.of(new Tile[]{Tile.s2, Tile.s3, Tile.s4, Tile.s6, Tile.s8, Tile.dg}));
        List<Tile> hand = request.getFullHandAsList();

        for (Tile tile : hand) {
            if (!greens.contains(tile)) { return; }
        }

        // it's possible that the hand isn't a valid 3-3-3-3-2 or 7p? nts: implement seq/tri/pair check

        request.getYaku().add(Yaku.AllGreen);
        request.setYakumanAchieved(true);
    }

    /**
     * All Triplets is a Yaku that requires:
     * - The hand to be made of 4 triplets and a pair.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForAllTriplets(PointsRequest request) {

        Tile pair = null;

        for (Map.Entry<Tile, Integer> entry : request.getFullHandAsMap().entrySet()) {
            if (entry.getValue() < 2 || entry.getValue() > 3) {
                return;
            }

            if (entry.getValue() == 2 && pair == null) {
                pair = entry.getKey();
            } else if (entry.getValue() == 2) {
                return;
            }
        }

        request.getYaku().add(Yaku.AllTriplets);
    }

    /**
     * All Simples is a Yaku that requires:
     * - The hand to be made of all simples, aka no honors or terminals
     * <p>
     * All Simples is incompatible with all Yaku/Yakuman that require honors or terminals:
     * - Thirteen Orphans
     * - All Honors
     * - All Terminals and Honors
     * - All Terminals
     * - Big/Little Three Dragons
     * - Four Big/Little Winds
     * - half/FUlly Outside Hand
     * - Any Yakuhai (ex. Green Dragon or Seat Wind)
     * So checking for it early cuts down on Yaku checks.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForAllSimples(PointsRequest request) {
        for (Tile tile : request.getFullHandAsList()) {
            if (tile.getType() != Type.Simple) { return; }
            request.getYaku().add(Yaku.AllSimples);
        }
    }

    /**
     * All these Yaku are compatible with most other Yaku and require a closed hand:
     * - Tsumo requires that the winning tile is self-drawn.
     * - Riichi requires that Riichi is called when in Tenpai.
     * - Double Riichi requires Riichi to be called on the player's first turn.
     * - Ippatsu is complex, but basically requires that the player that called Riichi draws/calls their winning tile
     *   before they can discard again, or before another player calls Chi/Pon.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForRiichiAndTsumo(PointsRequest request) {
        if (!request.getOpenHand()) { return; }

        if (request.getTsumo()) { request.getYaku().add(Yaku.Tsumo); }

        boolean riichi = request.getFlags().getOrDefault("riichi", false);
        boolean doubleRiichi = request.getFlags().getOrDefault("doubleRiichi", false);
        boolean ippatsu = request.getFlags().getOrDefault("ippatsu", false);

        if (riichi) { request.getYaku().add(Yaku.Riichi); }
        if (riichi && doubleRiichi) { request.getYaku().add(Yaku.DoubleRiichi); }
        if (riichi && ippatsu) { request.getYaku().add(Yaku.Ippatsu); }
    }

    public static void checkForPinfu(PointsRequest request) {

    }

    /**
     * All Terminals and Honors, All Terminals, and All Honors are as their names imply.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForAllTerminalsAndOrHonors(PointsRequest request) {
        List<Tile> hand = request.getFullHandAsList();

        boolean hasTerminals = false;
        boolean hasHonors = false;

        for (Tile tile : hand) {
            switch (tile.getType()) {
                case Terminal:
                    hasTerminals = true;
                    break;
                case Honor:
                    hasHonors = true;
                    break;
                default:
                    return;
            }
        }

        if (hasTerminals && hasHonors) {
            request.getYaku().add(Yaku.AllTerminalsAndHonors);
        } else if (hasTerminals) {
            request.getYaku().add(Yaku.AllTerminals);
            request.setYakumanAchieved(true);
        } else if (hasHonors) {
            request.getYaku().add(Yaku.ALlHonors);
            request.setYakumanAchieved(true);
        }
    }

    /**
     * Big Three Dragons requires a triplet of each dragon tile: Green, Red, and White.
     * Little Three Dragons is a downgrade of Big Three Dragons where one of the dragon triplets is a pair.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForBigOrLittleThreeDragons(PointsRequest request) {
        List<Tile> dragons = Tile.getAllTilesBySuit(Suit.Dragon);
        HashMap<Tile, Integer> hand = request.getFullHandAsMap();

        // One wind pair is allowed
        boolean dragonPair = false;

        for (Tile dragon : dragons) {
            int windAmount = hand.getOrDefault(dragon, 0);

            if (windAmount < 2) {
                return;
            } else if (windAmount == 2 && !dragonPair) {
                dragonPair = true;
            } else if (windAmount == 2) {
                return;
            } else if (windAmount > 3) {
                // Theoretically not possible but check anyway
                return;
            }
        }

        if (!dragonPair) {
            request.getYaku().add(Yaku.BigThreeDragons);
            request.setYakumanAchieved(true);
        } else {
            request.getYaku().add(Yaku.LittleThreeDragons);
        }
    }

    public static void checkForFourBigOrLittleWinds(PointsRequest request) {
        List<Tile> winds = Tile.getAllTilesBySuit(Suit.Wind);
        HashMap<Tile, Integer> hand = request.getFullHandAsMap();

        // One wind pair is allowed
        boolean windPair = false;

        for (Tile wind : winds) {
            int windAmount = hand.getOrDefault(wind, 0);

            if (windAmount < 2) {
                return;
            } else if (windAmount == 2 && !windPair) {
                windPair = true;
            } else if (windAmount == 2) {
                return;
            } else if (windAmount > 3) {
                // Theoretically not possible but check anyway
                return;
            }
        }

        if (!windPair) {
            request.getYaku().add(Yaku.FourBigWinds);
            request.setYakumanAchieved(true);
        } else {
            request.getYaku().add(Yaku.FourLittleWinds);
            request.setYakumanAchieved(true);
        }
    }

    /**
     * Yakuhai is actually a shared name for 6 different Yaku:
     * - Green Dragon Yakuhai: A triplet of Green Dragons.
     * - Red Dragon Yakuhai: A triplet of Red Dragons.
     * - White Dragon Yakuhai: A triplet of White Dragons.
     * - Prevalent Wind Yakuhai: A triplet of the round wind. Most commonly East.
     * - Seat Wind Yakuhai: A triplet of the player's seat wind.
     * <p>
     * If the round wind and seat wind are the same, both Yakuhai are awarded.
     *
     * @param request object that the hand is checked from
     */
    public static void checkForYakuhai(PointsRequest request) {
        List<Tile> honors = Tile.getAllTilesByType(Type.Honor);
        Wind roundWind = request.getRoundWind();
        Wind seatWind = request.getSeatWind();
        HashMap<Tile, Integer> hand = request.getFullHandAsMap();

        for (Tile honor : honors) {
            int honorAmount = hand.getOrDefault(honor, 0);
            Suit honorSuit = honor.getSuit();

            if (honorSuit == Suit.Dragon && honorAmount == 3) {
                switch (honor) {
                    case dg:
                        request.getYaku().add(Yaku.GreenDragon);
                        break;
                    case dr:
                        request.getYaku().add(Yaku.RedDragon);
                        break;
                    case dw:
                        request.getYaku().add(Yaku.WhiteDragon);
                        break;
                }
            } else if (honorSuit == Suit.Wind && honorAmount == 3) {
                if (honor.isWind(roundWind)) { request.getYaku().add(Yaku.PrevalentWind); }
                if (honor.isWind(seatWind)) { request.getYaku().add(Yaku.SeatWind); }
            }
        }
    }

    /**
     * Fully Outside Hand is a Yaku that requires:
     * - All sets and pairs to include a terminal
     * <p>
     * If the hand has sets or pairs of honors, it's a Half Outside Hand.
     *
     * @param hand hand that the Yaku is checked from
     */
    public static void checkForOutsideHand(PossibleHand hand) {

        boolean hasHonors = false;

        for (Set set : hand.getSets()) {
            if (set instanceof Sequence) {
                Tile[] tiles = ((Sequence) set).getTiles();
                if (tiles[0].getValue() != 1 || tiles[2].getValue() != 9) {
                    return;
                }
            } else if (set instanceof Triplet) {
                Type tileType = ((Triplet) set).getTile().getType();
                if (tileType == Type.Honor) {
                    hasHonors = true;
                } else if (tileType == Type.Simple) {
                    return;
                }
            } else if (set instanceof Pair) {
                Type tileType = ((Pair) set).getTile().getType();
                if (tileType == Type.Honor) {
                    hasHonors = true;
                } else if (tileType == Type.Simple) {
                    return;
                }
            }
        }

        if (hasHonors) {
            hand.getYaku().add(Yaku.HalfOutsideHand);
        } else {
            hand.getYaku().add(Yaku.FullyOutsideHand);
        }
    }

    /**
     * Pure Straight is a Yaku that requires:
     * - A sequence of 123.
     * - A sequence of 456.
     * - A sequence of 789.
     * - And for those three sequences to be of the same suit.
     *
     * @param hand hand that the Yaku is checked from
     */
    public static void checkForPureStraight(PossibleHand hand) {
        for (Suit suit : Suit.values()) {
            boolean oneTwoThree = false;
            boolean fourFiveSix = false;
            boolean sevenEightNine = false;

            for (Set set : hand.getSets()) {
                if (set instanceof Sequence) {
                    Tile[] tiles = ((Sequence) set).getTiles();
                    if (tiles[0].getValue() == 1) {
                        oneTwoThree = true;
                    } else if (tiles[1].getValue() == 5) {
                        fourFiveSix = true;
                    } else if (tiles[2].getValue() == 9) {
                        sevenEightNine = true;
                    }
                }
            }

            if (oneTwoThree && fourFiveSix && sevenEightNine) {
                hand.getYaku().add(Yaku.PureStraight);
                return;
            }
        }
    }

    public static void checkForPureDoubleSequences(PossibleHand hand) {

        int pureDoubleSequenceAmount = 0;

        for (Set set : hand.getSets()) {
            if (set instanceof Sequence) {
                for (Set checkSet : hand.getSets()) {
                    if (checkSet != set && set.equals(checkSet)) {
                        pureDoubleSequenceAmount++;
                    }
                }
            }
        }

        if (pureDoubleSequenceAmount == 1) {
            hand.getYaku().add(Yaku.PureDoubleSequence);
        } else if (pureDoubleSequenceAmount == 2) {
            hand.getYaku().add(Yaku.TwicePureDoubleSequence);
        }
    }

    public static void checkForMixedTriples(PossibleHand hand) {




    }
}
