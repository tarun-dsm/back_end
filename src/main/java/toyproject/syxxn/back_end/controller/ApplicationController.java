package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.response.ApplicationResponse;
import toyproject.syxxn.back_end.dto.response.MyApplicationResponse;
import toyproject.syxxn.back_end.service.application.ApplicationService;

@RequiredArgsConstructor
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/application/{id}")
    public void protectionApplication(@PathVariable("id") Integer postId) {
        applicationService.protectionApplication(postId);
    }

    @DeleteMapping("/application/{id}")
    public void cancelApplication(@PathVariable("id") Integer postId) {
        applicationService.cancelApplication(postId);
    }

    @PatchMapping("/application/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptApplication(@PathVariable("id") Integer applicationId) {
        applicationService.acceptApplication(applicationId);
    }

    @GetMapping("/applications")
    public MyApplicationResponse getMyApplications() {
        return applicationService.getMyApplications();
    }

    @GetMapping("/applications/post/{id}")
    public ApplicationResponse getApplications(@PathVariable("id") Integer postId) {
        return applicationService.getApplications(postId);
    }

}
