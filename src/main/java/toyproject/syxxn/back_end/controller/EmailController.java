package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.EmailRequest;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;
import toyproject.syxxn.back_end.dto.response.AccessTokenResponse;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.service.email.EmailService;

import javax.validation.Valid;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public void sendVerifyNumberEmail(EmailRequest request) {
        emailService.sendVerifyNumberEmail(request.getEmail());
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verify(VerifyRequest request) {
        emailService.verify(request);
    }

}