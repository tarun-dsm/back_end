package toyproject.syxxn.back_end.service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.exception.BlockedUserException;
import toyproject.syxxn.back_end.exception.UserNotAuthenticatedException;

@RequiredArgsConstructor
@Component
public class UserUtil {

    private final AccountRepository accountRepository;
    private final AuthenticationUtil authenticationFacade;

    public Account getLocalConfirmAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .filter(this::isNotBlocked)
                .orElseThrow(() -> UserNotAuthenticatedException.EXCEPTION);
    }

    public Boolean isNotBlocked(Account account) {
        if (account.getIsBlocked()) {
            throw BlockedUserException.EXCEPTION;
        }
        return true;
    }

}
