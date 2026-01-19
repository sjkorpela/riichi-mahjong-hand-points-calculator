package com.sjkorpela.RiichiPointsCalculator.Entities;


import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.Getter;

import java.util.List;

@Getter
public class Yakuman extends Yaku {
    private Integer yakumanCount;

    public Yakuman(
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