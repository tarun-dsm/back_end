package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
    List<Application> findAllByAccount(Account account);
    List<Application> findAllByPost(Post post);
    Optional<Application> findByPostAndIsAcceptedTrue(Post post);
    Optional<Application> findByPostAndAccount(Post post, Account account);
}