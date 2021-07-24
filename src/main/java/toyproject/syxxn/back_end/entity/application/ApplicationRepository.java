package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Long> {
    Optional<Application> findByAccountId(Long accountId);
}
