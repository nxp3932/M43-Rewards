package com.projectrewards.demo.controller;

import com.projectrewards.demo.dto.EarnRequest;
import com.projectrewards.demo.dto.RedeemRequest;
import com.projectrewards.demo.exception.InsufficientPointsException;
import com.projectrewards.demo.service.PointsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PointsController {

    private final PointsService pointsService;

    public PointsController(PointsService pointsService) { this.pointsService = pointsService; }

    @PostMapping("/earn")
    public ResponseEntity<Map<String, Long>> earn(@RequestBody EarnRequest req) {
        long balance = pointsService.earn(req.getUserId(), req.getPointsEarned(), req.getPurchaseId());
        return ResponseEntity.ok(Map.<String, Long>of("balance", balance));
    }

    @PostMapping("/redeem")
    public ResponseEntity<Map<String, Long>> redeem(@RequestBody RedeemRequest req) {
        long balance = pointsService.redeem(req.getUserId(), req.getPointsRedeemed());
        return ResponseEntity.ok(Map.<String, Long>of("balance", balance));
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Map<String, Long>> balance(@PathVariable Long userId) {
        long balance = pointsService.getBalance(userId);
        return ResponseEntity.ok(Map.<String, Long>of("balance", balance));
    }

    @ExceptionHandler(InsufficientPointsException.class)
    public ResponseEntity<Map<String, String>> handleInsufficient(InsufficientPointsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }
}
