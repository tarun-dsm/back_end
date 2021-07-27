package toyproject.syxxn.back_end.service.application;

public interface ApplicationService {
    void protectionApplication(Integer postId);
    void cancelApplication(Integer applicationId);
    void acceptApplication(Integer applicationId);
}
