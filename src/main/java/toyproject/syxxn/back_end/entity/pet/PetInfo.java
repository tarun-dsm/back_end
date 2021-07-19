package toyproject.syxxn.back_end.entity.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String petName;

    @NotNull
    @Column(columnDefinition = "varchar(30)")
    private String petSpecies;

    @NotNull
    private Sex petSex;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "post_id")
    private Post post;

}
