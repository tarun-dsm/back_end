package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Optional<Application> findByAccountAndPost(Account account, Post post);
    List<Application> findAllByAccount(Account account);
    List<Application> findAllByPost(Post post);
}