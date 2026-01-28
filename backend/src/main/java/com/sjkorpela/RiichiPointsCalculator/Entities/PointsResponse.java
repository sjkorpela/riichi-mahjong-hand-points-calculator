package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;
import com.sjkorpela.RiichiPointsCalculator.Services.JsonService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PointsResponse {

    private final List<ResponseYaku> yaku;
    private final boolean openHand;

    public PointsResponse(PointsRequest request) {
        this.yaku = new ArrayList<>();
        this.openHand = request.getOpenHand();

        for (Yaku yaku : request.getYaku()) {
            this.yaku.add(JsonService.getYakuDetails(yaku, request.getOpenHand()));
        }

        this.yaku.addAll(request.getResponseYaku());
    }
}
