package toyproject.syxxn.back_end.service.auth;

import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.dto.response.AccessTokenResponse;
import toyproject.syxxn.back_end.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(SignInRequest request);
    AccessTokenResponse tokenRefresh(String receivedToken);
}
