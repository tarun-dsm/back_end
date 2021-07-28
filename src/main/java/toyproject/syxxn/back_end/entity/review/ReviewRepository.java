package toyproject.syxxn.back_end.entity.review;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.application.Application;

import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Optional<Review> findByWriterAndApplication(Account writer, Application application);
}
