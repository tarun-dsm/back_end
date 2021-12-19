package toyproject.syxxn.back_end.entity.pet_info;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetInfoRepository extends CrudRepository<PetInfo, Integer> {
}
