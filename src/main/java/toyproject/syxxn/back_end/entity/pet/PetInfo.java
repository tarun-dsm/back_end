package toyproject.syxxn.back_end.entity.pet;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class PetInfo extends BaseIdEntity {

    @NotNull
    @Column(length = 30)
    private String petName;

    @NotNull
    @Column(length = 30)
    private String petSpecies;

    @NotNull
    private Sex petSex;

    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public PetInfo(Post post, String petName, String petSpecies, Sex petSex) {
        this.post = post;
        this.petName = petName;
        this.petSpecies = petSpecies;
        this.petSex = petSex;
    }

    public void update(PostRequest pet) {
        this.petName = pet.getPetname();
        this.petSpecies = pet.getPetspecies();
        this.petSex = Sex.valueOf(pet.getPetsex());
    }

}
