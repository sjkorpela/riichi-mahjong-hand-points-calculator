package com.sjkorpela.RiichiPointsCalculator.Entities;


import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.Getter;

import java.util.List;

@Getter
public class YakumanData extends ResponseYaku {
    private Integer yakumanCount;

    public YakumanData(
            String englishName,
            String japaneseName,
            String description,
            Integer han,
            List<Tile> usedTiles,
            Integer count
    ) {
        super(englishName, japaneseName, description, han, usedTiles);
        this.yakumanCount = count;
    }
}