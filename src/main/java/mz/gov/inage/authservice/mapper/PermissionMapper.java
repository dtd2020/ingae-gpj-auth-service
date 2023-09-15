package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.entity.PermissionEntity;
import mz.gov.inage.authservice.dto.CreatePermissionRequest;
import mz.gov.inage.authservice.dto.EditPermissionRequest;
import mz.gov.inage.authservice.dto.PermissionResponseData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseData toDto(PermissionEntity entity);

    PermissionEntity toEntity(CreatePermissionRequest request);

    PermissionEntity toEntity(EditPermissionRequest request);
}
