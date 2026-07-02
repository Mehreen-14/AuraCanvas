package com.example.auracanvas.controller;

import com.example.auracanvas.dto.ReviewDto;
import com.example.auracanvas.model.User;
import com.example.auracanvas.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewDto> addReview(@AuthenticationPrincipal User user,
                                                @PathVariable Long productId,
                                                @RequestBody Map<String, Object> body) {
        Integer rating = Integer.valueOf(body.get("rating").toString());
        String comment = (String) body.get("comment");
        return ResponseEntity.ok(reviewService.addReview(user.getId(), productId, rating, comment));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
