package toyproject.syxxn.back_end.entity.post;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

    @Query("select p from Post as p where p.petImages.size > 0 group by p.id order by p.createdAt desc")
    List<Post> findAllByPetImagesNotNullOrderByCreatedAtDesc();

    @Query("select p from Post as p where p.account = ?1 and p.petImages.size > 0 group by p.id order by p.createdAt desc")
    List<Post> findAllByAccountAndPetImagesNotNullOrderByCreatedAtDesc(Account account);

    @Query("select p from Post as p where p.isApplicationEnd = false and p.petImages.size > 0 group by p.id order by p.createdAt desc")
    List<Post> findAllByIsApplicationEndFalseAndPetImagesNotNullOrderByCreatedAtDesc();
}
