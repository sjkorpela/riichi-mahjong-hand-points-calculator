package com.sjkorpela.RiichiPointsCalculator.Services;

import com.sjkorpela.RiichiPointsCalculator.Entities.ResponseYaku;
import com.sjkorpela.RiichiPointsCalculator.Enums.Yaku;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;


public class JsonService {
    public static ResponseYaku getYakuDetails(Yaku yaku, boolean open) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(new File("src/main/resources/yaku.json"));
        JsonNode entry = jsonNode.get(yaku.toString());

        return new ResponseYaku(
            entry.get("englishName").asString("Error"),
            entry.get("japaneseName").asString("Error"),
            entry.get("description").asString("Error"),
            entry.get(open ? "openHan" : "closedHan").asInt(0)
        );
    }
}
