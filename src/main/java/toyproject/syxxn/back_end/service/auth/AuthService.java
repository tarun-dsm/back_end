package toyproject.syxxn.back_end.service.auth;

import toyproject.syxxn.back_end.dto.request.SignInRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(SignInRequest request);
    TokenResponse tokenRefresh(String receivedToken);
}
