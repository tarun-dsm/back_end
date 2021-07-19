package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import toyproject.syxxn.back_end.service.account.AccountService;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/email/{email}")
    public void confirmEmail(@PathVariable @Email String email) {
        accountService.confirmEmail(email);
    }

    @GetMapping("/nickname/{nickname}")
    public void confirmNickname(@PathVariable String nickname) {
        accountService.confirmNickname(nickname);
    }

}
