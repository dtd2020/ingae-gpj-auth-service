package mz.gov.inage.authservice.repository;

import mz.gov.inage.authservice.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(u) = :count THEN true ELSE false END FROM PermissionEntity u WHERE u.id IN :ids")
    boolean existByIds(@Param("ids") Set<Long> ids, @Param("count") Long count);

    Optional<PermissionEntity> findByCode(final String permissionCode);
}