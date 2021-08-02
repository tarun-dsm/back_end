package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.syxxn.back_end.dto.response.ProfilePostResponse;
import toyproject.syxxn.back_end.dto.response.ProfileResponse;
import toyproject.syxxn.back_end.dto.response.ProfileReviewResponse;
import toyproject.syxxn.back_end.service.profile.ProfileService;

import java.util.Optional;

@RequestMapping("/profile")
@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(value = {"/{id}", ""})
    public ProfileResponse getProfile(@PathVariable(value = "id", required = false) Integer accountId) {
        return profileService.getProfile(accountId);
    }

    @GetMapping(value = {"/reviews/{id}", "/reviews"})
    public ProfileReviewResponse getReviews(@PathVariable(value = "id", required = false) Integer accountId) {
        return profileService.getReviews(accountId);
    }

    @GetMapping(value = {"/posts/{id}", "/posts"})
    public ProfilePostResponse getPosts(@PathVariable(value = "id", required = false) Integer accountId) {
        return profileService.getPosts(accountId);
    }

}
