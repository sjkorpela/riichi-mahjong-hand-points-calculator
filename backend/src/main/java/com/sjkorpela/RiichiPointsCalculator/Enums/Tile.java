package com.sjkorpela.RiichiPointsCalculator.Enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Tile {
    s1("Sou 1"),
    s2("Sou 2"),
    s3("Sou 3"),
    s4("Sou 4"),
    s5("Sou 5"),
    s5r("Red Sou 5"),
    s6("Sou 6"),
    s7("Sou 7"),
    s8("Sou 8"),
    s9("Sou 9"),
    m1("Man 1"),
    m2("Man 2"),
    m3("Man 3"),
    m4("Man 4"),
    m5("Man 5"),
    m5r("Red Man 5"),
    m6("Man 6"),
    m7("Man 7"),
    m8("Man 8"),
    m9("Man 9"),
    p1("Pin 1"),
    p2("Pin 2"),
    p3("Pin 3"),
    p4("Pin 4"),
    p5("Pin 5"),
    p5r("Red Pin 5"),
    p6("Pin 6"),
    p7("Pin 7"),
    p8("Pin 8"),
    p9("Pin 9"),
    we("East Wind"),
    ws("South Wind"),
    ww("West Wind"),
    wn("North Wind"),
    dg("Green Dragon"),
    dr("Red Dragon"),
    dw("White Dragon"),
    any("Debug Tile"),
    ;

    private final Suit suit;
    private final Integer value;
    private final Type type;
    private final String readableName;
    private final Boolean red;

    Tile(String readableName) {
        this.readableName = readableName;
        this.red = this.toString().length() == 3 && this.toString().charAt(2) == 'r';

        switch (this.toString().charAt(0)) {
            case 's':
                this.suit = Suit.Sou;
                this.value = Character.getNumericValue(this.toString().charAt(1));
                this.type = (this.value == 1 || this.value == 9) ? Type.Terminal : Type.Simple;
                break;
            case 'm':
                this.suit = Suit.Man;
                this.value = Character.getNumericValue(this.toString().charAt(1));
                this.type = (this.value == 1 || this.value == 9) ? Type.Terminal : Type.Simple;
                break;
            case 'p':
                this.suit = Suit.Pin;
                this.value = Character.getNumericValue(this.toString().charAt(1));
                this.type = (this.value == 1 || this.value == 9) ? Type.Terminal : Type.Simple;
                break;
            case 'w':
                this.suit = Suit.Wind;
                this.value = Wind.valueOf(this.toString()).ordinal();
                this.type = Type.Honor;
                break;
            case 'd':
                this.suit = Suit.Dragon;
                this.value = Dragon.valueOf(this.toString()).ordinal();
                this.type = Type.Honor;
                break;
            default:
                this.suit = Suit.Debug;
                this.value = 0;
                this.type = Type.Debug;
                break;
        }

//        System.out.println(this + ": " + suit + ", " + ", " + value + ", " + type + ", " + readableName + ", " + red);
    }

    public static List<Tile> getAllTilesBySuit(Suit target) {
        return Arrays.stream(Tile.values()).filter(tile -> tile.suit == target).toList();
    }

    public static List<Tile> getAllTilesByType(Type target) {
        return Arrays.stream(Tile.values()).filter(tile -> tile.type == target).toList();
    }

}
