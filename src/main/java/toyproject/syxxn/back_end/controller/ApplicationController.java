package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.service.application.ApplicationService;

@RequestMapping("/application")
@RequiredArgsConstructor
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{id}")
    public void protectionApplication(@PathVariable("id") Integer postId) {
        applicationService.protectionApplication(postId);
    }

    @DeleteMapping("/{id}")
    void cancelApplication(@PathVariable("id") Integer applicationId) {
        applicationService.cancelApplication(applicationId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void acceptApplication(@PathVariable("id") Integer applicationId) {
        applicationService.acceptApplication(applicationId);
    }

}
