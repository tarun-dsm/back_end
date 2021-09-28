package toyproject.syxxn.back_end.entity.pet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PetImage extends BaseIdEntity {

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PetImage(Post post, String path) {
        this.post = post;
        this.path = path;
    }

}
