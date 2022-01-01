package toyproject.syxxn.back_end.entity.comment;

import org.springframework.data.repository.CrudRepository;
import toyproject.syxxn.back_end.entity.post.Post;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
    List<Comment> findAllByPostOrderByLastModifiedAtDesc(Post post);
}
