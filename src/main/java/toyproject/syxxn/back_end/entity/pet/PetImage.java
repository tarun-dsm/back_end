package toyproject.syxxn.back_end.entity.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String path;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "post_id")
    private Post post;

}
