package mz.gov.inage.authservice.user;

import com.github.javafaker.Faker;
import mz.gov.inage.authservice.GpjAuthServiceApplication;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.exceptions.BusinessException;
import mz.gov.inage.authservice.helpers.TestHelper;
import mz.gov.inage.authservice.helpers.UserMockFactory;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GpjAuthServiceApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private  IUserService userService;
    @Autowired
    private  TestHelper testHelper;
    @Autowired
    private   UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser() throws BusinessException {

        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var profile=testHelper.createOrFindProfile();
        createUserRequest.setProfileId(profile.getId());
        assertNotNull(profile.getId());
       var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findByUsername(userResponseData.getUsername())
                        .get();
        assertNotNull(userResponseData);
        assertNotNull(user.getId());
        assertTrue(user.isActive());
        assertTrue(passwordEncoder.matches(UserMockFactory.DEFAULT_PASSWORD,user.getPassword()));
        assertEquals(userResponseData.getUsername(),user.getUsername());
        assertEquals(userResponseData.getName(),user.getName());

    }

    @Test
    public void testFailCreateUserNoProfileProvided() {
        assertThrows(BusinessException.class, () -> {
            try {
                CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
                userService.createUser(createUserRequest);
            } catch (BusinessException e) {
                assertEquals("Profile should not be null", e.getMessage());
                throw e;
            }
        });
    }


    @Test
    public void testFailCreateUserExistingUsername() {
        assertThrows(BusinessException.class, () -> {
            try {
                CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
                createUserRequest.setProfileId(testHelper.createOrFindProfile().getId());
                userService.createUser(createUserRequest);
                userService.createUser(createUserRequest);

            } catch (BusinessException e) {
                assertEquals("User with the provided username already exists", e.getMessage());
                throw e;
            }
        });


    }


    @Test
    public void testCreateUserWithPermissions() throws BusinessException {

        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        var permission=testHelper.createOrFindPermission();
        var permissionIds=new HashSet<Long>();
        permissionIds.add(permission.getId());
        createUserRequest.setPermissions(permissionIds);
        createUserRequest.setProfileId(testHelper.createOrFindProfile().getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findByUsername(userResponseData.getUsername())
                .get();
        assertNotNull(userResponseData);
        assertNotNull(user.getId());
        assertTrue(user.isActive());
    }


    @Test
    public void testUpdateUser() throws BusinessException {
        var faker=new Faker();
        var newName=faker.name().fullName();
        var user= testHelper.createOrFindUser();
        var editUserRequest=new EditUserRequest();
        editUserRequest.setName(newName);
        assertNotNull(user.getId());
        assertTrue(user.isActive());
        assertNotEquals(user.getName(),editUserRequest.getName());
        var updatedUser=userService.updateUser(user.getId(),editUserRequest);
        assertEquals(updatedUser.getName(),editUserRequest.getName());
        assertNotNull(updatedUser.getId());
        assertTrue(updatedUser.isActive());
    }

    @Test
    public void testFailUpdateUserUserNotFound() throws BusinessException {
        var editUserRequest=new EditUserRequest();

        //we assume that we will never reach to this maximum
        var userId=Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.updateUser(userId,editUserRequest);
            } catch (BusinessException e) {
                assertEquals("No user found by the given Id", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testInactivateUser() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        createUserRequest.setProfileId(testHelper.createOrFindProfile().getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findByUsername(userResponseData.getUsername())
                .get();
        assertNotNull(user.getId());
        assertTrue(user.isActive());
        userService.inactivateUser(user.getId());
        var inactivatedUser=userRepository.findByUsername(userResponseData.getUsername())
                .get();
        assertNotNull(inactivatedUser.getId());
        assertFalse(inactivatedUser.isActive());
    }

    @Test
    public void testFailInactivateUserUserNotFound() throws BusinessException {
        //we assume that we will never reach to this maximum
        var userId=Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.inactivateUser(userId);
            } catch (BusinessException e) {
                assertEquals("No user found by the given Id", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    public void testDeleteUser() throws BusinessException {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        createUserRequest.setProfileId(testHelper.createOrFindProfile().getId());
        var userResponseData= userService.createUser(createUserRequest);
        var user=userRepository.findByUsername(userResponseData.getUsername())
                .get();
        assertNotNull(user.getId());
        assertTrue(user.isActive());
        userService.deleteUser(user.getId());
        var inactivatedUser=userRepository.findByUsername(userResponseData.getUsername());
        assertFalse(inactivatedUser.isPresent());
    }

    @Test
    public void testFailDeleteUserUserNotFound() throws BusinessException {
        //we assume that we will never reach to this maximum
        var userId=Long.MAX_VALUE;

        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.deleteUser(userId);
            } catch (BusinessException e) {
                assertEquals("No user found by the given Id", e.getMessage());
                throw e;
            }
        });
    }


    @Test
    public void testCreateProfile() throws BusinessException {
        var profileData = UserMockFactory.mockCreateProfileRequest();
        var profile= userService.createProfile(profileData);
        assertNotNull(profile);
        assertNotNull(profile.getId());
    }

    @Test
    public void testUpdateProfile() throws BusinessException {
        var faker=new Faker();
        var profileData = UserMockFactory.mockCreateProfileRequest();
        var profile= userService.createProfile(profileData);
        var newProfileDescription=faker.job().title();
        assertNotEquals(newProfileDescription,profile.getDescription());

        var editProfileRequest=new EditProfileRequest();
        editProfileRequest.setDescription(newProfileDescription);

        var newEditedProfile=userService.editProfile(profile.getId(),editProfileRequest);
        assertNotNull(newEditedProfile);
        assertNotNull(newEditedProfile.getId());
        assertEquals(newEditedProfile.getDescription(),editProfileRequest.getDescription());
    }


    @Test
    public void testDeleteProfile() throws BusinessException {
        var profileData = UserMockFactory.mockCreateProfileRequest();
        var profile= userService.createProfile(profileData);

        assertNotNull(profile);
        assertNotNull(profile.getId());
        userService.deleteProfile(profile.getId());
        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.deleteProfile(profile.getId());
            } catch (EntityNotFoundException e) {
                assertEquals("No Profile found by the given Id", e.getMessage());
                throw e;
            }
        });

    }
    @Test
    public void testFailUpdateProfileNotFound() throws BusinessException {

        var editProfileRequest=new EditProfileRequest();
        editProfileRequest.setDescription("will not update");

        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.editProfile(Long.MAX_VALUE,editProfileRequest);
            } catch (EntityNotFoundException e) {
                assertEquals("No Profile found by the given Id", e.getMessage());
                throw e;
            }
        });

    }

    @Test
    public void testFailDeleteProfileNotFound() throws BusinessException {
        assertThrows(EntityNotFoundException.class, () -> {
            try {
                userService.deleteProfile(Long.MAX_VALUE);
            } catch (EntityNotFoundException e) {
                assertEquals("No Profile found by the given Id", e.getMessage());
                throw e;
            }
        });

    }



}
