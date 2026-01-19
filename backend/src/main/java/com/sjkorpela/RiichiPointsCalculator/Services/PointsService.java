package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;

public class PointsService {

    public static void getYaku(PointsRequest request) {

        // flags based yaku
        YakuService.getFlagsYaku(request);
    }
}
