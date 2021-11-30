package toyproject.syxxn.back_end.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.CoordinatesRequest;
import toyproject.syxxn.back_end.dto.request.ReportRequest;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.HaveEverBeenEntrustedResponse;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.service.account.AccountService;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
@RestController
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

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void saveCoordinate(@RequestBody @Valid CoordinatesRequest request) throws JsonProcessingException, UnirestException {
        accountService.saveCoordinate(request);
    }

    @PostMapping("/report/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void makeReport(@PathVariable Integer id, @RequestBody @Valid ReportRequest request) {
        accountService.makeReport(request.getComment(), id);
    }

    @GetMapping("/account/enterested")
    public HaveEverBeenEntrustedResponse haveEverBeenEntrusted(@RequestParam int id) {
        return accountService.haveEverBeenEntrusted(id);
    }

}
