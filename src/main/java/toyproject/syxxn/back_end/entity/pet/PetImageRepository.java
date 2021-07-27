package toyproject.syxxn.back_end.entity.pet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetImageRepository extends CrudRepository<PetImage, Integer> {
}
