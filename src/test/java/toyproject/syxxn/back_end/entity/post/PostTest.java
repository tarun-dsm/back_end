package toyproject.syxxn.back_end.entity.post;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.GetAccount;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.account.Account;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostTest {

    private static final Account account = GetAccount.account;

    private static final Post post = GetPost.post;

    @Order(0)
    @Test
    void 게시글_마감여부() {
        assertFalse(post.getIsApplicationEnd());
    }

    @Test
    void 작성자_확인() {
        assertEquals(account, post.getAccount());
    }

    @Test
    void 게시글_마감() {
        post.isEnd();
        assertTrue(post.getIsApplicationEnd());
    }

    @Test
    void 게시글_업데이트() {
        post.update(
                PostRequest.PostRequestDto.builder()
                        .title("새해복많이받으세요")
                        .protectionStartDate("2021-12-31")
                        .protectionEndDate("2021-01-03")
                        .applicationEndDate("2021-12-30")
                        .description("배고파용")
                        .contactInfo("012-1234-4567")
                        .build()
        );
        assertEquals("새해복많이받으세요", post.getTitle());
    }

    @Test
    void 게시글_수정일() {
        assertNull(post.getLastModifiedAt()); // @LastModifiedDate는 JPA와 관련된 어노테이션이기 때문에 데이터베이스에 저장할 때 생성된다.
    }

}
