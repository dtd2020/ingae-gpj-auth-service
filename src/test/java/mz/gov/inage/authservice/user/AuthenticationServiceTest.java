package mz.gov.inage.authservice.user;

import com.github.javafaker.Faker;
import mz.gov.inage.authservice.GpjAuthServiceApplication;
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
    public void testRequestResetPassword() throws BusinessException {
        var user=testHelper.createOrFindUser();
        authenticationService.requestResetPassword(user.getId());
        var resetPassword= resetTokenRepository.findLastUnexpiredTokenByUser(user.getId());
        assertTrue(resetPassword.isPresent());
        assertNotNull(resetPassword.get().getToken());
        assertFalse(resetPassword.get().isExpired());
    }


}
