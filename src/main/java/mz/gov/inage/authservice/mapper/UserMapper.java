package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.UserResponseData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.stream.Collectors;


public class UserMapper {
    public static UserResponseData toDto(UserEntity entity){
     return  UserResponseData.builder()
               .id(entity.getId())
               .email(entity.getEmail())
               .active(entity.isActive())
               .createdBy(entity.getCreatedBy())
               .device(entity.getDevice())
               .lastLoggedIn(entity.getLastLoggedIn())
               .username(entity.getUsername())
               .name(entity.getName())
               .permissions(entity.getPermissions()==null?new HashSet<>():entity.getPermissions().stream()
                       .map(userPermission->PermissionMapper
                               .toDto(userPermission.getPermission())).collect(Collectors.toSet()))
               .profiles(entity.getRoles()==null?new HashSet<>():entity.getRoles().stream()
                       .map(userProfile->ProfileMapper.toDto(userProfile.getProfile()))
                       .collect(Collectors.toSet()))
               .build();

    }


    public static UserEntity toEntity(CreateUserRequest request){
        return  UserEntity.builder()
                .email(request.getEmail())
                .device(request.getDevice())
                .username(request.getUsername())
                .name(request.getName())
                .build();
    }
}
