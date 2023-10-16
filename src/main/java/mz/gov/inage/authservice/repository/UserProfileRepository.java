package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    Set<UserProfileEntity> findByUserId(final Long userId);

}