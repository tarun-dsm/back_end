package toyproject.syxxn.back_end.entity;

import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.pet_info.AnimalType;
import toyproject.syxxn.back_end.entity.post.Post;

public class GetPost {

    private static final String title = "제목입니동";
    private static final String description = "설명입니동";
    private static final String protectionStartDate = "2021-12-12";
    private static final String protectionEndDate = "2022-01-01";
    private static final String applicationEndDate = "2021-12-10";
    private static final String contactInfo = "010-1234-1234";
    private static final String petName = "펫이름";
    private static final String petSpecies = "멍멍이";
    private static final String petSex = String.valueOf(Sex.MALE);
    private static final String animalType = String.valueOf(AnimalType.FISH);

    public static final PostRequest postRequest = PostRequest.builder()
            .post(
                    PostRequest.PostRequestDto.builder()
                            .title(title)
                            .description(description)
                            .protectionStartDate(protectionStartDate)
                            .protectionEndDate(protectionEndDate)
                            .applicationEndDate(applicationEndDate)
                            .contactInfo(contactInfo)
                            .build()
            )
            .pet(
                    PostRequest.PetRequestDto.builder()
                            .petName(petName)
                            .petSpecies(petSpecies)
                            .petSex(petSex)
                            .animalType(animalType)
                            .build()
            )
            .build();

    private static final Account account = GetAccount.account;

    public static final Post post = Post.builder()
            .request(postRequest.getPost())
            .account(account)
            .build();

}
