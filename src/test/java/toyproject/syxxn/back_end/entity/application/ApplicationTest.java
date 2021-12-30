package toyproject.syxxn.back_end.entity.application;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {

    private static final Account account = GetAccount.account;
    private static final Post post = GetPost.post;

    private static final Application application = Application.builder()
            .applicant(account)
            .post(post)
            .build();

    @Test
    void 수락여부() {
        assertFalse(application.getIsAccepted());
    }

    @Test
    void 유저_확인() {
        assertEquals(account, application.getApplicant());
    }

    @Test
    void 게시글_확인() {
        assertEquals(post, application.getPost());
    }

}
