package toyproject.syxxn.back_end.service.account;

public interface AccountService {
    void confirmEmail(String email);
    void confirmNickname(String nickname);
}
