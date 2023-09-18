package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.entity.PermissionEntity;
import mz.gov.inage.authservice.dto.CreatePermissionRequest;
import mz.gov.inage.authservice.dto.EditPermissionRequest;
import mz.gov.inage.authservice.dto.PermissionResponseData;
import mz.gov.inage.authservice.entity.ProfileEntity;
import org.mapstruct.Mapper;

public class PermissionMapper {


    public static PermissionResponseData toDto(PermissionEntity entity){
        var permissionData=new PermissionResponseData();
        permissionData.setCode(entity.getCode());
        permissionData.setId(entity.getId());
        permissionData.setDescription(entity.getDescription());
        return permissionData;
    }

    public static PermissionEntity toEntity(CreatePermissionRequest request){
        var entity=new PermissionEntity();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public static PermissionEntity toEntity(EditPermissionRequest request){
        var entity=new PermissionEntity();
        entity.setCode(request.getCode());
        entity.setDescription(request.getDescription());
        return entity;
    }
}
