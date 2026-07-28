package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Entities.PossibleHand;
import com.sjkorpela.RiichiPointsCalculator.Enums.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class PointsService {

    // Seven Pairs always gets 25 Fu
    private static final int sevenPairsFu = 25;

    public static void getYaku(PointsRequest request) {

        // Yaku that are based on factors outside the winning hand itself, ex.:
        // - Calling Ron on the last discard of the hand, aka Under the River
        // There's a possible Yakuman, so this should be checked early to limit the search to Yakuman only
        YakuService.checkForFlagsYaku(request); // possible Yakuman

        // Seven Pairs, All Simples, and Half/Full Flush aren't Yakuman, but they should still be checked even if a
        // Yakuman is achieved. Because they're much cheaper checks and so can still be used to filter out Yakuman with
        // more expensive checking processes.

        // Due to its unique hand structure, Seven Pairs is compatible with only 5 Yaku/Yakuman:
        // - All Simples
        // - All Honors (Yakuman)
        // - All Terminals and Honors
        // - Half Flush
        // - Full Flush
        // So, checking for 7P early cuts out like 32 Yaku.
        YakuService.checkForSevenPairs(request);
        boolean sevenPairs = request.hasYaku(Yaku.SevenPairs);
        if (sevenPairs) { request.setFu(sevenPairsFu); }

        // CHECK FOR PURE DOUBLE SEQUENCE BECAUSE IT'S BETTER THAN 7P!!!!!

        // All Simples also cuts out like 16 possible Yaku/Yakuman. Basically any that require honors or terminals.
        YakuService.checkForAllSimples(request);
        boolean allSimples = request.getYaku().contains(Yaku.AllSimples);

        // Flushes are also a prerequisite for All Green and (True) Nine Gates
        // Either kind of Flush blocks:
        // - Triple Triplets
        // - Mixed Triple Sequence
        // And a Full Flush blocks:
        // - Half Outside Hand
        // - Any Yaku that require honors
        YakuService.checkForFlushYaku(request);
        boolean fullFlush = request.getYaku().contains(Yaku.FullFlush);
        boolean halfFlush = request.getYaku().contains(Yaku.HalfFlush);
        boolean eitherFlush = fullFlush || halfFlush;

        // Check for Riichi and Tsumo
        if (!request.getYakumanAchieved() ||!request.getOpenHand()) {
            YakuService.checkForRiichiAndTsumo(request);
        }
        boolean tsumo = request.getYaku().contains(Yaku.Tsumo);

        // All Triplets cuts out all sequence dependant Yaku/Yakuman
        if (!sevenPairs) { YakuService.checkForAllTriplets(request); }
        boolean allTriplets = request.getYaku().contains(Yaku.AllTriplets);

        // Thirteen Orphans is a possible Yakuman, and is the third Yaku in determining if the
        // hand has sequences
        if (!allSimples) { YakuService.checkForThirteenOrphans(request); } // possible Yakuman
        boolean thirteenOrphans = request.getYaku().contains(Yaku.ThirteenOrphans) || request.getYaku().contains(Yaku.ThirteenWaitThirteenOrphans);
        boolean hasSequences = !sevenPairs && !allTriplets && !thirteenOrphans;

        // 7p and 13o are the only two hands with a unique hand structure, so if it's not that,
        // then we can check the hand for a standard structure.
        if (!(sevenPairs || thirteenOrphans)) {
            // Get hand structure
            HandService.getPossibleHands(request);
            if (request.getPossibleHands().isEmpty()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Hand isn't valid! Not Seven Pairs, Thirteen Orphans, or a valid collection of four sets and one pair."
                );
            }
            // Count Fu and check for Pinfu
            HandService.countFu(request);
            YakuService.checkForPinfu(request);
        } else {
            HandService.formatUniqueHand(request);
        }


        // Check for a bunch of Yaku
        if (fullFlush && hasSequences) { YakuService.checkForNineGates(request); } // possible Yakuman
        if (!sevenPairs) YakuService.checkForAllGreen(request); // possible Yakuman
        if (!allSimples && !thirteenOrphans) {
            YakuService.checkForAllTerminalsAndOrHonors(request);
            boolean allTerminals = request.getYaku().contains(Yaku.AllTerminals);

            if (!allTerminals && !sevenPairs) {
                YakuService.checkForBigOrLittleThreeDragons(request);
                YakuService.checkForFourBigOrLittleWinds(request);
            }

            if (!request.getYakumanAchieved()) {
                YakuService.checkForYakuhai(request);

                for (PossibleHand hand : request.getPossibleHands()) {
                    YakuService.checkForOutsideHand(hand);
                    YakuService.checkForPureStraight(hand);
                }
            }
        }

        if (!request.getOpenHand()) {
            for (PossibleHand hand : request.getPossibleHands()) {
                YakuService.checkForPureDoubleSequences(hand);
            }
        }

        if (!eitherFlush) {
            for (PossibleHand hand : request.getPossibleHands()) {
                YakuService.checkForMixedTriples(request);
            }
        }

        YakuService.checkForConcealedTriplets(request);

        for (PossibleHand hand : request.getPossibleHands()) {
            System.out.println("hand: " + hand);
        }

        // Apply best hand
        List<PossibleHand> hands = request.getPossibleHands();
        if (hands.size() == 1) {
            request.getResponseYaku().addAll(hands.getFirst().getResponseYaku());
            request.setFu(HandService.roundFu(hands.getFirst().getFu()));
        } else {
            PossibleHand bestHand = HandService.getBestPossiblehand(hands);

            request.getResponseYaku().addAll(bestHand.getResponseYaku());
            request.setFu(HandService.roundFu(bestHand.getFu()));
        }

        YakuService.checkForDora(request);
    }

    public static void calculatePoints(PointsRequest request) {

        HandService.getPossibleHands(request);

        YakuService.checkForThirteenOrphans(request);
        boolean thirteenOrphans = request.hasYaku(Yaku.ThirteenOrphans) || request.hasYaku(Yaku.ThirteenWaitThirteenOrphans);

        YakuService.checkForSevenPairs(request);
        boolean sevenPairs = request.getYaku().contains(Yaku.SevenPairs);

        if (request.getPossibleHands().isEmpty() && !thirteenOrphans && !sevenPairs) {
            throw new IllegalArgumentException("Hand isn't valid! Not Seven Pairs, Thirteen Orphans, or a valid collection of four sets and one pair.");
        }

        boolean regularStructure = !request.getPossibleHands().isEmpty() && !sevenPairs && !thirteenOrphans;

        HandService.countFu(request);

        if (!request.getOpenHand()) {
            YakuService.checkForRiichiAndTsumo(request);
            YakuService.checkForPinfu(request);
        }

        YakuService.checkForFlagsYaku(request);
        YakuService.checkForDora(request);
        YakuService.checkForFlushYaku(request);
        YakuService.checkForAllSimples(request);

        if (regularStructure) {
            YakuService.checkForMixedTriples(request);
        }





    }
}
