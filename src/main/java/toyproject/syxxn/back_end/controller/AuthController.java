package toyproject.syxxn.back_end.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;
import toyproject.syxxn.back_end.service.auth.AuthService;

import javax.validation.Valid;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse login(@RequestBody @Valid SignInRequest request) {
        return authService.login(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TokenResponse tokenRefresh(@RequestHeader("X-Refresh-Token") String receivedToken) {
        return authService.tokenRefresh(receivedToken);
    }

}
