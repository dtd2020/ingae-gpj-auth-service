package mz.gov.inage.authservice.user;

import com.github.javafaker.Faker;
import mz.gov.inage.authservice.GpjAuthServiceApplication;
import mz.gov.inage.authservice.dto.ChangePasswordRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.RegisterRequest;
import mz.gov.inage.authservice.exceptions.BusinessException;
import mz.gov.inage.authservice.exceptions.UnauthorizedException;
import mz.gov.inage.authservice.helpers.TestHelper;
import mz.gov.inage.authservice.helpers.UserMockFactory;
import mz.gov.inage.authservice.repository.PasswordResetTokenRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.service.AuthenticationService;
import mz.gov.inage.authservice.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GpjAuthServiceApplication.class)
@ActiveProfiles("test")
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private  TestHelper testHelper;
    @Autowired
    private   UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private  IUserService userService;

    @Autowired
    private  PasswordResetTokenRepository resetTokenRepository;

    @Test
    public void testAuthenticateUser() throws BusinessException {
        var user=testHelper.createOrFindUser();
        var registerRequest=new RegisterRequest();
        registerRequest.setPassword(UserMockFactory.DEFAULT_PASSWORD);
        registerRequest.setUsername(user.getUsername());

     var token=authenticationService.authenticate(registerRequest);
        assertNotNull(token);
        assertNotNull(token.getToken());

    }

    @Test
    public void testFailAuthenticateUserUsernameNotFound() {
        var user=testHelper.createOrFindUser();
        var registerRequest=new RegisterRequest();
        registerRequest.setPassword(UserMockFactory.DEFAULT_PASSWORD);
        registerRequest.setUsername("non_existent_user_name");

        assertThrows(UnauthorizedException.class, () -> {
            try {
                authenticationService.authenticate(registerRequest);
            } catch (UnauthorizedException e) {
                assertEquals("Username or Password are incorrect", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testFailAuthenticateUserPasswordIncorrect() {
        var user=testHelper.createOrFindUser();
        var registerRequest=new RegisterRequest();
        registerRequest.setPassword("fake_pass");
        registerRequest.setUsername(user.getUsername());

        assertThrows(UnauthorizedException.class, () -> {
            try {
                authenticationService.authenticate(registerRequest);
            } catch (UnauthorizedException e) {
                assertEquals("Username or Password are incorrect", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testChangePassword() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var profile=testHelper.createOrFindProfile();
        assertNotNull(profile.getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findById(userResponseData.getId()).get();

        //Create a request to change the password
        authenticationService.requestResetPassword(user.getId());

        //Preparing to change the password
        var changePasswordRequest=new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword(UserMockFactory.DEFAULT_PASSWORD);
        changePasswordRequest.setNewPassword("InageM.20232@");

        //Before password change, the old password should not match with the new One
        assertFalse(passwordEncoder.matches(changePasswordRequest.getNewPassword(),user.getPassword()));

        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

        //Make sure the user is not providing the same password as the current
        assertNotEquals(changePasswordRequest.getNewPassword(),changePasswordRequest.getCurrentPassword());
        assertNotEquals(changePasswordRequest.getNewPassword(),changePasswordRequest.getCurrentPassword());

        authenticationService.changePassword(userResponseData.getId(),changePasswordRequest);

        var updatedUser=userRepository.findById(userResponseData.getId()).get();
        assertTrue(passwordEncoder.matches(changePasswordRequest.getNewPassword(),updatedUser.getPassword()));

        //Try to login with the new password
        var registerRequest=new RegisterRequest();
        registerRequest.setPassword(changePasswordRequest.getNewPassword());
        registerRequest.setUsername(userResponseData.getUsername());
        var token=authenticationService.authenticate(registerRequest);
        assertNotNull(token);
    }

    @Test
    public void testFailChangePasswordCurrentPasswordIsIncorrect() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var profile=testHelper.createOrFindProfile();
        assertNotNull(profile.getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findById(userResponseData.getId()).get();

        //Preparing to change the password
        var changePasswordRequest=new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("Bad_pass.2023#");
        changePasswordRequest.setNewPassword("InageM.20232@");

        //Create a request to change the password
        authenticationService.requestResetPassword(user.getId());

        //Old Password
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

        assertThrows(UnauthorizedException.class, () -> {
            try {
                authenticationService.changePassword(userResponseData.getId(),changePasswordRequest);
            } catch (UnauthorizedException e) {
                assertEquals("Current password is incorrect", e.getMessage());
                throw e;
            }
        });

        //Still the same old Password. So nothing changed
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

    }


    @Test
    public void testFailChangePasswordInvalidFormat() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var profile=testHelper.createOrFindProfile();
        assertNotNull(profile.getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findById(userResponseData.getId()).get();

        //Preparing to change the password
        var changePasswordRequest=new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword(UserMockFactory.DEFAULT_PASSWORD);
        changePasswordRequest.setNewPassword("nage20232");

        //Create a request to change the password
        authenticationService.requestResetPassword(user.getId());

        //Old Password
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

        assertThrows(BusinessException.class, () -> {
            try {
                authenticationService.changePassword(userResponseData.getId(),changePasswordRequest);
            } catch (BusinessException e) {
                assertEquals("Invalid password format", e.getMessage());
                throw e;
            }
        });

        //Still the same old Password. So nothing changed
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

    }


    @Test
    public void testFailChangePasswordSamePasswordAsPrevious() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var profile=testHelper.createOrFindProfile();
        assertNotNull(profile.getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findById(userResponseData.getId()).get();

        //Preparing to change the password
        var changePasswordRequest=new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword(UserMockFactory.DEFAULT_PASSWORD);
        changePasswordRequest.setNewPassword(UserMockFactory.DEFAULT_PASSWORD);

        //Create a request to change the password
        authenticationService.requestResetPassword(user.getId());

        //Old Password
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

        assertThrows(BusinessException.class, () -> {
            try {
                authenticationService.changePassword(userResponseData.getId(),changePasswordRequest);
            } catch (BusinessException e) {
                assertEquals("New password cannot be the same as the current password", e.getMessage());
                throw e;
            }
        });

        //Still the same old Password. So nothing changed
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));

    }


}
