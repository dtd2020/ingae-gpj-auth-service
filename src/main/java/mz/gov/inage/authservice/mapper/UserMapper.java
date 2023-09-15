package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.UserResponseData;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserResponseData toDto(UserEntity entity);

    UserEntity toEntity(CreateUserRequest request);

    UserEntity toEntity(EditUserRequest request);
}
