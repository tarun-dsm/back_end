package toyproject.syxxn.back_end.service.application;

import toyproject.syxxn.back_end.dto.response.ApplicationResponse;
import toyproject.syxxn.back_end.dto.response.MyApplicationResponse;

public interface ApplicationService {
    void protectionApplication(Integer postId);
    void cancelApplication(Integer applicationId);
    void acceptApplication(Integer applicationId);
    MyApplicationResponse getMyApplications();
    ApplicationResponse getApplications(Integer postId);
}
