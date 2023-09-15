package mz.gov.inage.authservice.helpers;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.service.IUserService;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestHelper {
    private final IUserService userService;

    public ProfileResponseData createProfile() {
        var profile = UserMockFactory.mockCreateProfileRequest();
        return userService.createProfile(profile);
    }

    public UserResponseData createUser(ProfileResponseData profile) {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        createUserRequest.setProfileId(profile.getId());
        return userService.createUser(createUserRequest);

    }
}
