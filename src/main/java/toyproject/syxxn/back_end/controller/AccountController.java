package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.service.account.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public TokenResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return accountService.signUp(request);
    }

    @GetMapping("/email/{email}")
    public void confirmEmail(@PathVariable @Email String email) {
        accountService.confirmEmail(email);
    }

    @GetMapping("/nickname/{nickname}")
    public void confirmNickname(@PathVariable String nickname) {
        accountService.confirmNickname(nickname);
    }

}
