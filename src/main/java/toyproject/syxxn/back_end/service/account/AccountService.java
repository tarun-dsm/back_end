package toyproject.syxxn.back_end.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import toyproject.syxxn.back_end.dto.request.CoordinatesRequest;
import toyproject.syxxn.back_end.dto.request.SignUpRequest;
import toyproject.syxxn.back_end.dto.response.TokenResponse;

public interface AccountService {
    TokenResponse signUp(SignUpRequest request);
    void confirmEmail(String email);
    void confirmNickname(String nickname);
    void saveCoordinate(CoordinatesRequest request) throws JsonProcessingException, UnirestException;
    void makeReport(String comment, Integer id);
}
