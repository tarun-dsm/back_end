package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
    Optional<Application> findByPostIdAndApplicantEmail(Integer id, String email);
    List<Application> findAllByApplicant(Account account);
    List<Application> findAllByPost(Post post);
    Optional<Application> findByPostAndIsAcceptedTrue(Post post);
    Optional<Application> findByPostAndApplicant(Post post, Account account);
    @Query("select a from Application a where a.isAccepted = true and ((a.applicant = ?1 and a.post.account = ?2) or (a.post.account = ?1 and a.applicant = ?2))")
    List<Application> findAllByVisitAccountAndMe(Account visitAccount, Account me);
}