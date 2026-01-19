package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;

public class PointsService {

    public static void getYaku(PointsRequest request) {

        // check flags based yaku, possible yakuman
        YakuService.checkFlagsYaku(request);

        // check for 13o
        YakuService.checkThirteenOrphans(request);
    }
}
