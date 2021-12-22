package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
    Optional<Application> findByPostIdAndApplicantEmail(Integer id, String email);
    List<Application> findAllByPost(Post post);
    Optional<Application> findByPostAndIsAcceptedTrue(Post post);
    Optional<Application> findByPostAndApplicant(Post post, Account account);
    @Query("select a from Application a where a.isAccepted = true " +
            "and ((a.applicant = ?1 and a.post.account = ?2) or (a.post.account = ?1 and a.applicant = ?2))")
    List<Application> findAllByVisitAccountAndMe(Account visitAccount, Account me);

    @Query(value = "select count(*) > 0 from application a" +
            "join post p on a.post_id = p.id" +
            "join account ac on a.applicant_id = ac.email" +
            "and ((p.is_application_end = false) or (a.is_accepted = true))" +
            "and ?2 between a.created_at and p.application_end_date limit 1", nativeQuery = true)
    boolean existsNotEndApplication(Account account, LocalDateTime now);
}