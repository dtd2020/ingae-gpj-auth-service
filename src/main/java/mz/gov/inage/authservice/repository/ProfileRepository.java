package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

}