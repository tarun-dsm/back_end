package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.ReviewRequest;
import toyproject.syxxn.back_end.dto.request.UpdateReviewRequest;
import toyproject.syxxn.back_end.service.review.ReviewService;

import javax.validation.Valid;

@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void writeReview(@RequestBody @Valid ReviewRequest request) {
        reviewService.writeReview(request);
    }

    @DeleteMapping("/{id}")
    void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateReview(@PathVariable Long id,
                      @RequestBody @Valid UpdateReviewRequest request) {
        reviewService.updateReview(id, request);
    }

}
