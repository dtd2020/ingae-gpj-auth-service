package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionRepository extends JpaRepository<UserPermissionEntity, Long> {

}