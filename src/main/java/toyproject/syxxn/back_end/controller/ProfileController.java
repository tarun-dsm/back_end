package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.syxxn.back_end.dto.response.ProfileResponse;
import toyproject.syxxn.back_end.dto.response.ProfileReviewResponse;
import toyproject.syxxn.back_end.service.profile.ProfileService;

@RequestMapping("/profile")
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/{id}")
    public ProfileResponse getProfile(@PathVariable("id") Integer accountId) {
        return profileService.getProfile(accountId);
    }

    @GetMapping("/review/{id}")
    public ProfileReviewResponse getReviews(@PathVariable("id") Integer accountId) {
        return profileService.getReviews(accountId);
    }

}
