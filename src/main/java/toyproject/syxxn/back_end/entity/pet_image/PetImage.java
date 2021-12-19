package toyproject.syxxn.back_end.entity.pet_image;

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
    private String savedPath;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PetImage(Post post, String path) {
        this.post = post;
        this.savedPath = path;
    }

}
