package mz.gov.inage.authservice.helpers;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.PermissionResponseData;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.mapper.PermissionMapper;
import mz.gov.inage.authservice.mapper.ProfileMapper;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.repository.ProfileRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.service.IUserService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestHelper {
    private final IUserService userService;

    private final ProfileRepository profileRepository;

    private final UserRepository userRepository;

    private final PermissionRepository permissionRepository;

    public ProfileResponseData createOrFindProfile() {
        var profile =profileRepository.findByCode(UserMockFactory.PROFILE_CODE);
        if(!profile.isPresent()  ){
             var profileData = UserMockFactory.mockCreateProfileRequest();
             profileData.setCode(UserMockFactory.PROFILE_CODE);
            return userService.createProfile(profileData);
        }
        else{
           return ProfileMapper.toDto(profile.get());
        }

    }

    public PermissionResponseData createOrFindPermission() {
        var permission =permissionRepository.findByCode(UserMockFactory.DEFAULT_PERMISSION_CODE);
        if(!permission.isPresent()  ){
            var permissionData = UserMockFactory.mockCreatePermissionRequest();
            return PermissionMapper.toDto(permissionRepository.save(PermissionMapper.toEntity(permissionData)));
        }
        else{
            return PermissionMapper.toDto(permission.get());
        }

    }

    public UserResponseData createOrFindUser() {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var user=userRepository.findByUsername(UserMockFactory.DEFAULT_USERNAME);
        if(user.isPresent()){
            return UserMapper.toDto(user.get());
        }
        else{
            createUserRequest.setProfileId(createOrFindProfile().getId());
            createUserRequest.setUsername(UserMockFactory.DEFAULT_USERNAME);
            return userService.createUser(createUserRequest);
        }



    }
}
