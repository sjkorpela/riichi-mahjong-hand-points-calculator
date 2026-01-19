package com.sjkorpela.RiichiPointsCalculator.Entities;


import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.Getter;

import java.util.List;

@Getter
public class YakumanData extends YakuData {
    private Integer yakumanCount;

    public YakumanData(
            String englishName,
            String japaneseName,
            String description,
            Integer closedHan,
            Integer openHan,
            List<Tile> usedTiles,
            Integer count
    ) {
        super(englishName, japaneseName, description, closedHan, openHan, usedTiles);
        this.yakumanCount = count;
    }
}