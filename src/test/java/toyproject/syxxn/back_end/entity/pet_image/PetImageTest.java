package toyproject.syxxn.back_end.entity.pet_image;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetImageTest {

    private static final Post post = GetPost.post;
    private static final PetImage petImage = new PetImage(post, "https://github.com/syxxn");

    @Test
    void 널_여부() {
        assertNotNull(petImage.getSavedPath());
    }

    @Test
    void 게시글_일치_여부() {
        assertEquals(post, petImage.getPost());
    }

}
