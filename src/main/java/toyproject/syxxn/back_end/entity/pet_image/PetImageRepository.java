package toyproject.syxxn.back_end.entity.pet_image;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;

@Repository
public interface PetImageRepository extends CrudRepository<PetImage, Integer> {
    List<PetImage> findAllByPost(Post post);
}
