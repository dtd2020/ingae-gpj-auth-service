package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.ProfilePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ProfilePermissionRepository extends JpaRepository<ProfilePermissionEntity, Long> {
}