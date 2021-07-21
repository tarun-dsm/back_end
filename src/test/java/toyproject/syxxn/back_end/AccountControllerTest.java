package toyproject.syxxn.back_end;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import toyproject.syxxn.back_end.config.EmbeddedRedisConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {BackEndApplication.class, EmbeddedRedisConfig.class})
@ActiveProfiles("test")
public class AccountControllerTest {

    @Test
    public void test() {
        System.out.println("해결했다 얏호-!");
    }

}
