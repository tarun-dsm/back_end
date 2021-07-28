package toyproject.syxxn.back_end.service.profile;

import toyproject.syxxn.back_end.dto.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfile(Integer accountId);
}
