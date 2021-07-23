package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.service.account.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return accountService.signUp(request);
    }

    @GetMapping("/email/{email}")
    public void confirmEmail(@PathVariable @Email String email) {
        accountService.confirmEmail(email);
    }

    @GetMapping("/nickname/{nickname}")
    public void confirmNickname(@PathVariable @Length(min = 2, max = 10) String nickname) {
        accountService.confirmNickname(nickname);
    }

}
