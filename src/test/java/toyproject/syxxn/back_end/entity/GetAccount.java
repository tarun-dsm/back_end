package toyproject.syxxn.back_end.entity;

import toyproject.syxxn.back_end.entity.account.Account;

public class GetAccount {

    private static final String email = "test@naver.com";
    private static final String password = "password";

    public static final Account account = Account.builder()
            .email(email)
            .password(password)
            .nickname("꼬순내친구들")
            .age(20)
            .sex(Sex.FEMALE)
            .isExperienceRaisingPet(false)
            .experienceDescription(null)
            .build();

}
