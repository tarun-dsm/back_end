package toyproject.syxxn.back_end.entity.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import toyproject.syxxn.back_end.entity.account.Account;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByAccount(Account account);
}