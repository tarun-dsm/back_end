package toyproject.syxxn.back_end.entity.pet_info;

import lombok.*;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.post.Post;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PetInfo {

    @Id
    private Integer postId;

    @Column(length = 30, nullable = false)
    private String petName;

    @Column(length = 30, nullable = false)
    private String petSpecies;

    @Column(length = 6, nullable = false)
    private Sex petSex;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private AnimalType animalType;

    @MapsId
    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public PetInfo(Post post, String petName, String petSpecies, String petSex, String animalType) {
        this.post = post;
        this.petName = petName;
        this.petSpecies = petSpecies;
        this.petSex = Sex.valueOf(petSex);
        this.animalType = AnimalType.valueOf(animalType);
    }

    public void update(PostRequest pet) {
        this.petName = pet.getPetName();
        this.petSpecies = pet.getPetSpecies();
        this.petSex = Sex.valueOf(pet.getPetSex());
        this.animalType = AnimalType.valueOf(pet.getAnimalType());
    }

}
