package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
    Boolean existsByAccountAndPost(Account account, Post post);
    List<Application> findAllByAccount(Account account);
    List<Application> findAllByPost(Post post);
}