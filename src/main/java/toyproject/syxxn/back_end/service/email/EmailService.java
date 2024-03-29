package toyproject.syxxn.back_end.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.verify_number.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify_number.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.EmailSendException;
import toyproject.syxxn.back_end.exception.UserAlreadyRegisteredException;
import toyproject.syxxn.back_end.exception.VerifyNumberNotMatchException;
import toyproject.syxxn.back_end.service.util.EmailUtil;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailService {

    private final AccountRepository accountRepository;
    private final VerifyNumberRepository verifyNumberRepository;

    private static final Random RANDOM = new Random();

    private final EmailUtil emailUtil;

    @Async
    @Transactional
    public void sendVerifyNumberEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {
                    throw UserAlreadyRegisteredException.EXCEPTION;
                });

        verifyNumberRepository.findById(email)
                .ifPresent(verifyNumberRepository::delete);
        String authNumber = generateVerifyNumber();

        try {
            String subject = "이메일 인증 안내드립니다.";
            String text = "인증 번호는 " + authNumber + "입니다.";
            emailUtil.sendEmail(email, subject, text);

            verifyNumberRepository.save(
                    new VerifyNumber(email, authNumber)
            );
        } catch (Exception e) {
            throw EmailSendException.EXCEPTION;
        }
    }

    public void verify(VerifyRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .filter(verifyNumber -> verifyNumber.getVerifyNumber().equals(request.getNumber()))
                .map(VerifyNumber::updateVerifyStatus)
                .map(verifyNumberRepository::save)
                .orElseThrow(() -> VerifyNumberNotMatchException.EXCEPTION);
    }

    private String generateVerifyNumber() {
        RANDOM.setSeed(System.currentTimeMillis());
        return Integer.toString(RANDOM.nextInt(1000000) % 1000000);
    }

}
