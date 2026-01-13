package com.sjkorpela.RiichiPointsCalculator;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidPointsRequest {
    private List<Tile> hand;
    private Wind roundWind;
    private Wind seatWind;
    private List<Tile> dora;
    private Boolean openHand;
}
