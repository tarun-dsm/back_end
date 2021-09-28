package toyproject.syxxn.back_end.entity.pet;

import lombok.*;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.BaseIdEntity;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PetInfo extends BaseIdEntity {

    @Column(length = 30, nullable = false)
    private String petName;

    @Column(length = 30, nullable = false)
    private String petSpecies;

    @Column(length = 6, nullable = false)
    private Sex petSex;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AnimalType animalType;

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
