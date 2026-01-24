package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.*;
import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;

import java.util.*;

public class HandService {

    public static void getPossibleHands(PointsRequest request) {
        CheckingHand initialHand = new CheckingHand(request.getFullHandAsList());

        List<CheckingHand> readyHands = new ArrayList<CheckingHand>();
        checkForNextSet(initialHand, readyHands);

        for (CheckingHand hand : readyHands) {
            request.getPossibleHands().add(new PossibleHand(hand));
        }
    }

    private static void checkForNextSet(CheckingHand hand, List<CheckingHand> readyHands) {

        Integer currentIndex = hand.getFirstUnspentIndex();
        List<Tile> tiles = hand.getTiles();

        if (currentIndex == null || currentIndex == tiles.size() - 1) {
            hand.setReady(true);
            readyHands.add(hand);
            return;
        }

        Tile currentTile = hand.getTiles().get(currentIndex);
        boolean nextIsUnspent = !hand.getSpentTiles().contains(currentIndex + 1);
        boolean nextIsMatch = nextIsUnspent && currentTile.equals(tiles.get(currentIndex + 1));

        if (nextIsMatch && hand.getPair() == null) {
            CheckingHand nextCheck = new CheckingHand(hand);
            nextCheck.setPair(currentTile, currentIndex, currentIndex + 1);
            checkForNextSet(nextCheck, readyHands);
        }

        boolean atLeastTwoTilesLeft = currentIndex + 2 < tiles.size();
        boolean nextTwoAreUnspent = nextIsUnspent && atLeastTwoTilesLeft && !hand.getSpentTiles().contains(currentIndex + 2);
        boolean nextTwoAreMatch = nextIsMatch && nextTwoAreUnspent && currentTile.equals(tiles.get(currentIndex + 2));

        if (nextTwoAreMatch) {
            CheckingHand nextCheck = new CheckingHand(hand);
            nextCheck.addTriplet(currentTile, currentIndex, currentIndex + 1, currentIndex + 2);
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
            nextCheck.addSequence(currentTile, tiles.get(secondIndex), tiles.get(thirdIndex), currentIndex, secondIndex, thirdIndex);
            checkForNextSet(nextCheck, readyHands);
        }
    }
}
