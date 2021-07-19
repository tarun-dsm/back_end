package toyproject.syxxn.back_end.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.exception.UserNotFoundException;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public AuthDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return accountRepository.findById(Integer.parseInt(id))
                .map(AuthDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }

}
