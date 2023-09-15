package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.UserResponseData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


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
