package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ResponseYaku {
    private String englishName;
    private String japaneseName;
    private String description;
    private Integer han = 0;
    private List<Tile> tiles;

    public ResponseYaku(String englishName, String japaneseName, String description, Integer han) {
        this.englishName = englishName;
        this.japaneseName = japaneseName;
        this.description = description;
        this.han = han;
        this.tiles = null;
    }
}
