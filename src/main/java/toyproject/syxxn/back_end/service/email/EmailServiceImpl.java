package toyproject.syxxn.back_end.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.VerifyRequest;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.verify.VerifyNumber;
import toyproject.syxxn.back_end.entity.verify.VerifyNumberRepository;
import toyproject.syxxn.back_end.exception.EmailSendException;
import toyproject.syxxn.back_end.exception.UserAlreadyRegisteredException;
import toyproject.syxxn.back_end.exception.VerifyNumberNotMatchException;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private static final Random RANDOM = new Random();

    private final JavaMailSender javaMailSender;

    private final AccountRepository accountRepository;

    private final VerifyNumberRepository verifyNumberRepository;

    @Async
    @Transactional
    @Override
    public void sendVerifyNumberEmail(String email) {
        accountRepository.findByEmail(email)
                .ifPresent(account -> {
                    throw new UserAlreadyRegisteredException();
                });

        verifyNumberRepository.findById(email)
                .ifPresent(verifyNumberRepository::delete);
        String authNumber = generateVerifyNumber();

        try {
            final MimeMessagePreparator preparator = mimeMessage -> {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("ggosoonnaefreinds@gmail.com");
                helper.setTo(email);
                helper.setSubject("이메일 인증 안내드립니다.");
                helper.setText("인증 번호는 " + authNumber + "입니다.");
            };
            javaMailSender.send(preparator);

            verifyNumberRepository.save(
                    new VerifyNumber(email, authNumber, false)
            );
        } catch (Exception e) {
            throw new EmailSendException();
        }
    }

    @Override
    public void verify(VerifyRequest request) {
        verifyNumberRepository.findById(request.getEmail())
                .filter(verifyNumber -> verifyNumber.getVerifyNumber().equals(request.getNumber()))
                .map(VerifyNumber::isVerifiedTrue)
                .map(verifyNumberRepository::save)
                .orElseThrow(VerifyNumberNotMatchException::new);
    }

    private String generateVerifyNumber() {
        RANDOM.setSeed(System.currentTimeMillis());
        return Integer.toString(RANDOM.nextInt(1000000) % 1000000);
    }

}
