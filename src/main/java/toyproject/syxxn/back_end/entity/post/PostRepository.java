package toyproject.syxxn.back_end.entity.post;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByAccountAndPetImagesNotNullOrderByCreatedAtDesc(Account account);
    List<Post> findAllByIsApplicationEndFalseOrderByCreatedAtDesc();
}
