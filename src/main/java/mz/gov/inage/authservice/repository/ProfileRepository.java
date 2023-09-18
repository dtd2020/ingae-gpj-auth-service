package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.ProfileEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

   Optional<ProfileEntity> findByCode(final String code);
}