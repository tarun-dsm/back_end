package toyproject.syxxn.back_end.entity.pet;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetInfoRepository extends CrudRepository<PetInfo, Integer> {
}
