package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Yaku {
    private String englishName;
    private String japaneseName;
    private String description;
    private Integer closedHan = 0;
    private Integer openHan = 0;
    private List<Tile> usedTiles;
}
