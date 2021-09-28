package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.service.review.ReviewService;

import javax.validation.Valid;

@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void writeReview(@PathVariable("id") Integer applicationId,
                            @RequestBody @Valid ReviewRequest request) {
        reviewService.writeReview(applicationId, request);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable("id") Integer reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}")
    public void updateReview(@PathVariable("id") Integer reviewId,
                             @RequestBody @Valid ReviewRequest request) {
        reviewService.updateReview(reviewId, request);
    }

}
