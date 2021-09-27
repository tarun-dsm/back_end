package toyproject.syxxn.back_end.entity.pet;

import lombok.AccessLevel;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PetInfo {

    @Id
    private Integer postId;

    @NotNull
    @Column(length = 30, nullable = false)
    private String petName;

    @NotNull
    @Column(length = 30, nullable = false)
    private String petSpecies;

    @Column(length = 6, nullable = false)
    private Sex petSex;

    @Column()
    private AnimalType animalType;

    @MapsId
    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public PetInfo(Post post, String petName, String petSpecies, Sex petSex, AnimalType animalType) {
        this.post = post;
        this.petName = petName;
        this.petSpecies = petSpecies;
        this.petSex = petSex;
        this.animalType = animalType;
    }

    public void update(PostRequest pet) {
        this.petName = pet.getPetName();
        this.petSpecies = pet.getPetSpecies();
        this.petSex = Sex.valueOf(pet.getPetSex());
        this.animalType = AnimalType.valueOf(pet.getAnimalType());
    }

}
