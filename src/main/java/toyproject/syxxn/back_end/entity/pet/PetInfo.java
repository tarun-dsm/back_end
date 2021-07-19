package toyproject.syxxn.back_end.entity.pet;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String petName;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String petSpecies;

    @Column(nullable = false)
    private Sex petSex;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "post_id")
    private Post post;

}
