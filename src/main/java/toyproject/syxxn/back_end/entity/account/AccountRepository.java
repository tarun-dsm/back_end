package toyproject.syxxn.back_end.entity.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByNickname(String nickname);
}
