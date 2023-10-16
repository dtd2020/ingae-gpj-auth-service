package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {

    @Query(value = "select p.* from PASSWORD_RESET_TOKEN p where  p.user_id = :userId and p.token = :token  ",nativeQuery = true)
    Optional<PasswordResetTokenEntity> findTokenByUser(@Param("userId") final Long userId,@Param("token") final String token);

}
