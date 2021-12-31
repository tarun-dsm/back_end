package toyproject.syxxn.back_end.entity.pet_info;

import org.junit.jupiter.api.Test;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.GetPost;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetInfoTest {

    private static final Post post = GetPost.post;
    private static final PetInfo petInfo = new PetInfo(post, GetPost.postRequest.getPet());

    @Test
    void 게시글_저장() {
        assertEquals(post, petInfo.getPost());
    }

    @Test
    void 업데이트() {
        petInfo.update(
                PostRequest.PetRequestDto.builder()
                        .petName("금붕어")
                        .petSpecies("금붕어")
                        .petSex(String.valueOf(Sex.MALE))
                        .animalType(String.valueOf(AnimalType.FISH))
                        .build()
        );
        assertEquals("금붕어", petInfo.getPetName());
    }

}
