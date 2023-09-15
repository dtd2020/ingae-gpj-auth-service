package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

}