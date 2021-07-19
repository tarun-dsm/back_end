package toyproject.syxxn.back_end.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.exception.UserEmailAlreadyExistsException;
import toyproject.syxxn.back_end.exception.UserNicknameAlreadyExistsException;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public void confirmEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {throw new UserEmailAlreadyExistsException(); });
    }

    @Override
    public void confirmNickname(String nickname) {
        accountRepository.findByNickname(nickname)
                .ifPresent(account -> {throw new UserNicknameAlreadyExistsException(); });
    }

}
