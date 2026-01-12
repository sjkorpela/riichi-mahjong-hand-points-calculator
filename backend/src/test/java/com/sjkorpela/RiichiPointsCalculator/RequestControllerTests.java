package com.sjkorpela.RiichiPointsCalculator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
public class RequestControllerTests {

    @Autowired
    private RestTestClient client;

//    @Test
//    public void getHello() {
//        client.get().uri("/points").exchangeSuccessfully()
//                .expectBody(String.class)
//                .isEqualTo("points");
//    }
//
//    @Test
//    public void testValidHand() {
//        /*
//            {
//                "hand": {
//                    "s1": 1,
//                    "s2": 1,
//                    "s3": 1,
//                    "m4": 1,
//                    "m5r": 1,
//                    "p7": 1,
//                    "p8": 1,
//                    "p9": 1,
//                    "we": 3,
//                    "dw": 2
//                },
//                "roundWind": "we",
//                "seatWind": "we",
//                "dora": ["m3", "s9"],
//                "openHand": false,
//                "flags" : {
//                    "riichi": true
//                }
//            }
//         */
//        client.get().uri("/points").exchangeSuccessfully()
//                .expectBody(Object.class)
//                .isEqualTo(new Object());
//    }

}
