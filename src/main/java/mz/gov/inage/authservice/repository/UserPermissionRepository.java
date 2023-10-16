package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.ProfilePermissionEntity;
import mz.gov.inage.authservice.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Long> {
    Set<UserPermissionEntity> findByUserId(final Long userId);
}