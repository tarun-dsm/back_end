package toyproject.syxxn.back_end.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyApplicationResponse {

    private List<MyApplicationDto> myApplications;

}
