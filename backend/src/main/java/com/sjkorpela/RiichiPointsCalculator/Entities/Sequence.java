package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Sequence implements Set {
    private Tile[] tiles;
    private Integer[] indexes;

    public Sequence(Tile firstTile, Tile secondTile, Tile thirdTile, Integer firstIndex, Integer secondIndex, Integer thirdIndex) {
        this.tiles = new Tile[]{firstTile, secondTile, thirdTile};
        this.indexes = new Integer[]{firstIndex, secondIndex, thirdIndex};
    }
}
