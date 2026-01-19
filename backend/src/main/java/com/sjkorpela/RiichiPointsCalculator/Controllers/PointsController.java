package com.sjkorpela.RiichiPointsCalculator.Controllers;

import com.sjkorpela.RiichiPointsCalculator.Entities.PointsRequest;
import com.sjkorpela.RiichiPointsCalculator.Services.PointsService;
import com.sjkorpela.RiichiPointsCalculator.Services.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointsController {

    @GetMapping("/points")
    public ResponseEntity<?> CalculatePoints(@RequestBody PointsRequest request) {

        try {
            ValidationService.validatePointsRequest(request);
            request.initalizeOtherFields();
            PointsService.getYaku(request);
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
