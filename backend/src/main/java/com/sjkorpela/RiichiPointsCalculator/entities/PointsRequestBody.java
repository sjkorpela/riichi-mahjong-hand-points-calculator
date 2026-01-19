package com.sjkorpela.RiichiPointsCalculator.entities;

import java.io.Serializable;
import java.util.HashMap;

import lombok.*;

@Getter
@Setter
public class PointsRequestBody implements Serializable {
//    String[] hand;
    String roundWind;
    String seatWind;
    String[] dora;
    Boolean openHand;
    HashMap<String, Boolean> flags;
}