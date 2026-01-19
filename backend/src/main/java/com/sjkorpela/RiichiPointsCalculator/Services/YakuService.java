package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Entities.Yaku;
import com.sjkorpela.RiichiPointsCalculator.Entities.Yakuman;

import java.util.*;

public class YakuService {
    public static void getFlagsYaku(PointsRequest request) {
        if (request.getFlags().getOrDefault("blessedHand", false)) {
            request.getYaku().add(new Yakuman(
                    "Blessing of Heaven/Earth",
                    "Tenhou / Chiihou",
                    "Win by starting with a ready hand as the dealer, or by drawing a winning tile on your first draw as a non-dealer.",
                    13,
                    13,
                    request.getFullHandAsList(),
                    1
            ));
        }

        if (request.getFlags().getOrDefault("afterKan", false)) {
            request.getYaku().add(new Yaku(
                    "After/Robbing a Kan",
                    "Rinshan kaihou / Chankan",
                    "Win by drawing your winning tile from the Dead Wall after a Kan, or by calling Ron on another player upgrading an Open Triplet into an Added Kan of your winning tile.",
                    1,
                    1,
                    Collections.singletonList(request.getWinningTile())
            ));
        }

        if (request.getFlags().getOrDefault("lastTile", false)) {
            request.getYaku().add(new Yaku(
                    "Under the Sea/River",
                    "Haitei / Houtei",
                    "Win by drawing your winning tile from the last tile in the Live Wall, or by calling Ron on the last discard of the hand.",
                    1,
                    1,
                    Collections.singletonList(request.getWinningTile())
            ));
        }
    }
}
