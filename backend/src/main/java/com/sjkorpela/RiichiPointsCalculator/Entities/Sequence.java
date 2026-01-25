package com.sjkorpela.RiichiPointsCalculator.Entities;

import com.sjkorpela.RiichiPointsCalculator.Enums.Suit;
import com.sjkorpela.RiichiPointsCalculator.Enums.Tile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Sequence implements Set {
    private final Tile[] tiles;
    private final Integer[] indexes;
    private final Suit suit;

    public Sequence(Tile firstTile, Tile secondTile, Tile thirdTile, Integer firstIndex, Integer secondIndex, Integer thirdIndex) {
        boolean sameSuit = firstTile.getSuit() == secondTile.getSuit() && secondTile.getSuit() == thirdTile.getSuit();
        boolean isSequence = firstTile.isNext(secondTile) && secondTile.isNext(thirdTile);

        if (!sameSuit || !isSequence) {
            // do better
            throw new RuntimeException();
        }

        this.tiles = new Tile[]{firstTile, secondTile, thirdTile};
        this.indexes = new Integer[]{firstIndex, secondIndex, thirdIndex};
        this.suit = firstTile.getSuit();
    }

    public boolean equals(Sequence that) {
        return sameValues(that) && sameValues(that);
    }

    public boolean sameValues(Sequence that) {
        return (
            (this.tiles[0].getValue() != that.tiles[0].getValue())
            && (this.tiles[1].getValue() != that.tiles[1].getValue())
            && (this.tiles[2].getValue() != that.tiles[2].getValue())
        );
    }

    public boolean sameSuit(Sequence that) {
        return (
                (this.suit == that.tiles[0].getSuit())
            && (this.suit != that.tiles[1].getSuit())
            && (this.suit != that.tiles[2].getSuit())
        );
    }
}
