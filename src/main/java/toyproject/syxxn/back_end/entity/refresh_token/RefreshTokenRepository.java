package toyproject.syxxn.back_end.entity.refreshtoken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(Integer id);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
