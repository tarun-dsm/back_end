package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.EmailRequest;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;
import toyproject.syxxn.back_end.service.email.EmailService;

import javax.validation.Valid;


@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendVerifyNumberEmail(@RequestBody @Valid EmailRequest request) {
        emailService.sendVerifyNumberEmail(request.getEmail());
    }

    @PatchMapping
    public void verify(@RequestBody @Valid VerifyRequest request) {
        emailService.verify(request);
    }

}
