package mz.gov.inage.authservice.helpers;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.RegisterRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMockFactory {

    public static final String DEFAULT_USERNAME = "fake_user_name";
    public static final String DEFAULT_PASSWORD = "Pass.1234#";
    public static final String PROFILE_CODE = "admin";
    public static final String PROFILE_DESCRIPTION = "Administrator";

    public static CreateUserRequest mockCreateUserRequest() {
        Faker faker = new Faker();
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(faker.name().username());
        request.setName(faker.name().fullName());
        request.setPassword(DEFAULT_PASSWORD);
        request.setDevice(faker.lorem().word());
        request.setEmail(faker.internet().emailAddress());
        request.setMobile(faker.phoneNumber().cellPhone());
        request.setCreatedBy(faker.number().randomNumber());

        return request;
    }

    public static UserEntity mockUserEntity() {
       var user= UserEntity.builder()
               .username(DEFAULT_USERNAME)
               .password(DEFAULT_PASSWORD)
               .build();

        return user;
    }

    public static RegisterRequest mockRegisterRequest() {
        var registerRequest= new RegisterRequest();
        registerRequest.setPassword(DEFAULT_PASSWORD);
        registerRequest.setUsername(DEFAULT_USERNAME);
        return registerRequest;
    }

    public static CreateProfileRequest mockCreateProfileRequest() {
        Faker faker = new Faker();
        var profile= new CreateProfileRequest();
        profile.setCode(faker.cat().name());
        profile.setDescription(faker.job().title());
        return profile;
    }
}

