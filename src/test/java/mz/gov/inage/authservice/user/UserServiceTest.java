package mz.gov.inage.authservice.user;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.helpers.TestHelper;
import mz.gov.inage.authservice.helpers.UserMockFactory;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.service.IUserService;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.config.name=application-integration")
@RequiredArgsConstructor
public class UserServiceTest {

    private final  IUserService userService;
    private final TestHelper testHelper;
    private final  UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Test
    public void testCreateUser_Success() throws BusinessException {
       var profile= testHelper.createProfile();
        UserResponseData userResponseData = testHelper.createUser(profile);
        var user=userRepository.findByUsername(userResponseData.getUsername())
                        .get();
        assertNotNull(userResponseData);
        assertNotNull(user.getId());
        assertTrue(user.isActive());
        assertTrue(passwordEncoder.matches(user.getPassword(),UserMockFactory.DEFAULT_PASSWORD));
        assertEquals(userResponseData.getUsername(),user.getUsername());
        assertEquals(userResponseData.getName(),user.getName());

    }

    @Test
    public void testCreateUser_NoProfileProvided() {
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.createUser(createUserRequest));
        assertEquals("Profile should not be null", exception.getMessage());
    }


    @Test
    public void testCreateUser_ExistingUsername() {
        // Create a fake CreateUserRequest with an existing username
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        createUserRequest.setUsername("existing_username"); // Assuming "existing_username" already exists

        // Assert that a BusinessException is thrown with the correct message
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.createUser(createUserRequest));
        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    public void testCreateUser_PasswordRequirements() {
        // Create a fake CreateUserRequest with a weak password
        CreateUserRequest createUserRequest = UserMockFactory.mockCreateUserRequest();
        createUserRequest.setPassword("weakpass"); // Assuming this password does not meet the requirements

        // Assert that a BusinessException is thrown with the correct message
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.createUser(createUserRequest));
        assertTrue(exception.getMessage().contains("Invalid password format"));
    }

}
