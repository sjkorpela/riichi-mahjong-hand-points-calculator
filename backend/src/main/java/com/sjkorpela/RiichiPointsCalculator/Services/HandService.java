package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.*;
import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;

import java.util.*;

public class HandService {

    public static void getPossibleHands(PointsRequest request) {
        // LIST OF ALL POSSIBLE HANDS
        // BASICALLY JUST UHHH LIKE START AT THE START AND THEN JUST SPLIT FORM THERE
        // LIKE IF THE FIRST THREE TILES CAN BE USED AS EITHER A PAIR OR A TRIPLET, MAKE TWO BRANCHES!??!??!?!

        // a tile should only look forward to avoid overlapping finds!

        List<Tile> hand = request.getFullHandAsList();

        CheckingHand initalHand = new CheckingHand(hand);

        List<CheckingHand> readyHands = new ArrayList<CheckingHand>();
        checkForNextSet(initalHand, readyHands);

        for (CheckingHand readyHand : readyHands) {
            System.out.println(readyHand);
        }
    }

    private static void checkForNextSet(CheckingHand hand, List<CheckingHand> readyHands) {
        Integer currentIndex = hand.getFirstUnspentIndex();
        List<Tile> tiles = hand.getTiles();

        if (currentIndex == null || currentIndex == tiles.size() - 1) {
            System.out.println("Hand end!");
            hand.setReady(true);
            readyHands.add(hand);
            return;
        }

        Tile currentTile = hand.getTiles().get(currentIndex);
        boolean nextIsUnspent = !hand.getSpentTiles().contains(currentIndex + 1);
        boolean nextIsMatch = nextIsUnspent && currentTile.equals(tiles.get(currentIndex + 1));

        if (nextIsMatch && hand.getPair() == null) {
            CheckingHand nextCheck = new CheckingHand(hand);
            nextCheck.setPair(new Pair(currentTile, currentIndex, currentIndex + 1));
            nextCheck.getSpentTiles().add(currentIndex);
            nextCheck.getSpentTiles().add(currentIndex + 1);
            // start next iteration from this

            System.out.println("oldHand: " + hand.getSpentTiles());
            System.out.println("pairHand: " + nextCheck.getSpentTiles());

            checkForNextSet(nextCheck, readyHands);
        }

        boolean atLeastTwoTilesLeft = currentIndex + 2 < tiles.size();
        boolean nextTwoAreUnspent = nextIsUnspent && atLeastTwoTilesLeft && !hand.getSpentTiles().contains(currentIndex + 2);
        boolean nextTwoAreMatch = nextIsMatch && nextTwoAreUnspent && currentTile.equals(tiles.get(currentIndex + 2));

        if (nextTwoAreMatch) {
            CheckingHand nextCheck = new CheckingHand(hand);
            nextCheck.getTrips().add(new Triplet(currentTile, currentIndex, currentIndex + 1, currentIndex + 2));
            nextCheck.getSpentTiles().add(currentIndex);
            nextCheck.getSpentTiles().add(currentIndex + 1);
            nextCheck.getSpentTiles().add(currentIndex + 2);
            // start next iteration from this

            System.out.println("oldHand: " + hand.getSpentTiles());
            System.out.println("tripHand: " + nextCheck.getSpentTiles());

            checkForNextSet(nextCheck, readyHands);
        }

        Integer secondIndex = null;
        Integer thirdIndex = null;

        for (int checkIndex = currentIndex + 1; checkIndex < tiles.size(); checkIndex++) {
            Tile checkTile = tiles.get(checkIndex);

            if (secondIndex == null && currentTile.isNext(checkTile) && !hand.getSpentTiles().contains(checkIndex)) {
                secondIndex = checkIndex;
            }

            if (secondIndex != null && tiles.get(secondIndex).isNext(checkTile) && !hand.getSpentTiles().contains(checkIndex)) {
                thirdIndex = checkIndex;
                break;
            }
        }

        if (thirdIndex != null) {
            CheckingHand nextCheck = new CheckingHand(hand);
            nextCheck.getSequs().add(new Sequence(
                    currentTile, tiles.get(secondIndex), tiles.get(thirdIndex),
                    currentIndex, secondIndex + 1, thirdIndex + 2)
            );
            nextCheck.getSpentTiles().add(currentIndex);
            nextCheck.getSpentTiles().add(secondIndex);
            nextCheck.getSpentTiles().add(thirdIndex);
            // start next iteration from this

            System.out.println("oldHand: " + hand.getSpentTiles());
            System.out.println("sequHand: " + nextCheck.getSpentTiles());

            checkForNextSet(nextCheck, readyHands);
        }
    }

    public static void getHandStructure(PointsRequest request) {
        List<Sequence> sequences = new ArrayList<>();
        List<Triplet> triplets = new ArrayList<>();
        List<Pair> pairs = new ArrayList<>();

        List<Tile> listHand = request.getFullHandAsList();
        HashMap<Tile, Integer> mapHand = request.getFullHandAsMap();
        List<Tile> allTiles = List.of(Tile.values());

        for (Tile tile : allTiles) {
            System.out.println(tile + ": " + tile.getValue());
        }

        // Get all indexes of a tile as list
        // If three indexes, it's a triplet
        // if two, it's a pair
//        for (Tile tile : allTiles) {
//            List<Integer> indexes = new ArrayList<>();
//            for (int i = 0; i < listHand.size(); i++) { if (listHand.get(i) == tile) { indexes.add(i); } }
//            if (indexes.size() == 3) { triplets.add(new Triplet(tile, indexes)); }
//            else if (indexes.size() == 2) { pairs.add(new Pair(tile, indexes)); }
//        }

        // From the second tile to the second last tile
        // If neighbors are n-1 and n+1, add to sequences
        for (int i = 1; i < listHand.size() - 1; i++) {
            Tile prev = listHand.get(i - 1);
            Tile tile = listHand.get(i);
            Tile next = listHand.get(i + 1);

//            if (tile.getType() != Type.Honor && tile.getValue() - 1 == prev.getValue() && tile.getValue() + 1 == next.getValue()) {
//                sequences.add(new Sequence(Arrays.asList(prev, tile, next), Arrays.asList(i - 1, i, i + 1)));
//            }

            boolean sameAsPrev = tile == prev || (tile.getValue() == prev.getValue() && tile.getSuit() == prev.getSuit());
            boolean sameAsNext = tile == next || (tile.getValue() == next.getValue() && tile.getSuit() == next.getSuit());

            if (sameAsPrev && sameAsNext) {
//                triplets.add(new Triplet(tile, Arrays.asList(i - 1, i, i + 1)));
            }

            if (sameAsPrev) {
//                pairs.add(new Pair(tile, Arrays.asList(i - 1, i)));
            } else if (i == listHand.size() - 1 && sameAsNext) {
//                pairs.add(new Pair(tile, Arrays.asList(i, i + 1)));
            }
        }

        Tile prev = null;
        Tile prevprev = null;
        for (Map.Entry<Tile, Integer> entry : mapHand.entrySet()) {
            Tile tile = entry.getKey();

            if (prev != null && prevprev != null) {
                boolean prevSeq = prev.getValue() == tile.getValue() - 1 && prev.getSuit() == tile.getSuit();
                boolean prevprevSeq = prevprev.getValue() == tile.getValue() - 1 && prevprev.getSuit() == tile.getSuit();

//                if (prevSeq && prevprevSeq) { }
            }

            prevprev = prev;
            prev = tile;
        }

        request.setSequences(sequences);
        request.setTriplets(triplets);
        request.setPairs(pairs);
    }
}
