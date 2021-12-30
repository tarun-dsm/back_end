package toyproject.syxxn.back_end.entity.verify_number;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyNumberRepository extends CrudRepository<VerifyNumber, String> {
}
