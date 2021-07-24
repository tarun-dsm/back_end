package toyproject.syxxn.back_end.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Integer getUserId() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        return authentication == null ? 0 : Integer.parseInt(authentication.getName());
    }

}
