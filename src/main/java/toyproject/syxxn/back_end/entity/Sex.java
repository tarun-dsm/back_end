package toyproject.syxxn.back_end.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Sex {
    FEMALE("여성"), MALE("남성");

    private final String korean;
}
