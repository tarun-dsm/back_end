package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.syxxn.back_end.service.application.ApplicationService;

@RequestMapping("/application")
@RequiredArgsConstructor
@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{id}")
    public void protectionApplication(@PathVariable Integer id) {
        applicationService.protectionApplication(id);
    }

}
