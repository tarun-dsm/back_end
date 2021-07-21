package toyproject.syxxn.back_end.service.email;

import toyproject.syxxn.back_end.dto.request.EmailRequest;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;

public interface EmailService {
    void sendVerifyNumberEmail(String email);
    void verify(VerifyRequest request);
}
