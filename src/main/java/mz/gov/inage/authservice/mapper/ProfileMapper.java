package mz.gov.inage.authservice.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.entity.ProfileEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileMapper {
    public static ProfileResponseData toDto(ProfileEntity entity){
        var profileData=new ProfileResponseData();
        profileData.setCode(entity.getCode());
        profileData.setId(entity.getId());
        profileData.setDescription(entity.getDescription());
        return profileData;
    }

}
