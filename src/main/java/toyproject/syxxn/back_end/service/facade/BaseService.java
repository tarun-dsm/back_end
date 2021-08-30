package toyproject.syxxn.back_end.service.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.exception.BlockedUserException;
import toyproject.syxxn.back_end.exception.UserNotUnauthenticatedException;

@RequiredArgsConstructor
@Component
public class BaseService {

    private final AccountRepository accountRepository;
    private final AuthenticationFacade authenticationFacade;

    public Account getLocalConfirmAccount() {
        return accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .filter(Account::getIsLocationConfirm)
                .map(this::isBlocked)
                .orElseThrow(UserNotUnauthenticatedException::new);
    }

    public Account isBlocked(Account account) {
        if (account.getIsBlocked()) {
            throw new BlockedUserException();
        }
        return account;
    }

}
