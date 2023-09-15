package mz.gov.inage.authservice.mapper;

import mz.gov.inage.authservice.entity.ProfileEntity;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileResponseData toDto(ProfileEntity entity);

    ProfileEntity toEntity(CreateProfileRequest dto);

    ProfileEntity toEntity(EditProfileRequest dto);
}
