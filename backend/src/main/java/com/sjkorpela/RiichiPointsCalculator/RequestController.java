package com.sjkorpela.RiichiPointsCalculator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
public class RequestController {

    @GetMapping("/points")
    public ResponseEntity<?> CalculatePoints(@RequestBody PointsRequestBody body) {
        ValidPointsRequest valid;

        try { valid = PointsService.validateRequest(body); }
        catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        System.out.println(body);

        return new ResponseEntity<>(valid, HttpStatus.OK);
    }
}
