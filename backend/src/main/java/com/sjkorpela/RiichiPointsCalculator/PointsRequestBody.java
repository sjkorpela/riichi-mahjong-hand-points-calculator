package com.sjkorpela.RiichiPointsCalculator;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
public class PointsRequestBody implements Serializable {
//    String[] hand;
    String roundWind;
    String seatWind;
    String[] dora;
    boolean openHand;
    Object flags;
}