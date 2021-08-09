package toyproject.syxxn.back_end.entity.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetImage extends BaseIdEntity {

    @NotNull
    private String path;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
