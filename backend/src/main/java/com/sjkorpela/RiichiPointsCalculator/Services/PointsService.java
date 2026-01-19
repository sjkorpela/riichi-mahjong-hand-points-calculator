package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;

public class PointsService {

    public static void getYaku(PointsRequest request) {

        YakuService.checkFlagsYaku(request); // possible yakuman
        YakuService.checkThirteenOrphans(request); // possible yakuman

        // If Yakuman is achieved, all base Yaku can be skipped
        boolean yakuman = request.getYakumanAchieved();

        YakuService.checkFlushYaku(request);

        // Some yaku require a flush and some are incompatible with flushes
        boolean fullFlush = request.getYaku().contains(Yaku.FullFlush);
        boolean halfFlush = request.getYaku().contains(Yaku.HalfFlush);

        if (fullFlush) { YakuService.checkForNineGates(request); } // possible yakuman
        if (fullFlush || halfFlush) { YakuService.checkForAllGreen(request); } // possible yakuman
    }
}
