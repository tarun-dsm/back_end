package toyproject.syxxn.back_end.entity.comment;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    private static final Account account = GetAccount.account;
    private static final Post post = GetPost.post;
    private static final String c = "혹시 강아지를 저희 집에서 보호해야하나요??";

    private static final Comment comment = Comment.builder()
            .comment(c)
            .post(post)
            .writer(account)
            .build();

    @Test
    void 수정여부() {
        assertFalse(comment.getIsUpdated());
        assertNull(comment.getLastModifiedAt());
    }

    @Test
    void 업데이트() {
        comment.update("새해 복 많이 받으세요");
        assertTrue(comment.getIsUpdated());
        assertEquals("새해 복 많이 받으세요", comment.getComment());
    }


}
