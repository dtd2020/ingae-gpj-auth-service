package mz.gov.inage.authservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.dto.ResetPasswordRequest;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.UserPermissionRepository;
import mz.gov.inage.authservice.repository.UserProfileRepository;
import mz.gov.inage.authservice.security.JwtService;
import mz.gov.inage.authservice.entity.PasswordResetTokenEntity;
import mz.gov.inage.authservice.repository.PasswordResetTokenRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.dto.AuthenticationResponseData;
import mz.gov.inage.authservice.dto.ChangePasswordRequest;
import mz.gov.inage.authservice.dto.RegisterRequest;
import mz.gov.inage.authservice.exceptions.BusinessException;
import mz.gov.inage.authservice.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final  UserRepository userRepository;

	private final  JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	private final PasswordResetTokenRepository resetTokenRepository;

	private final UserPermissionRepository userPermissionRepository;

	private final UserProfileRepository userProfileRepository;

	@Value("${maximum.token.expiration}")
	private Integer maximumTokenExpiration;

	public AuthenticationResponseData authenticate(RegisterRequest request) {

		var userEntityOptional = this.userRepository.findByUsername(request.getUsername());

		if(!userEntityOptional.isPresent()){
			throw new UnauthorizedException("Username or Password are incorrect");
		}

		boolean isValid = passwordEncoder.matches(request.getPassword(), userEntityOptional.get().getPassword());
		if(!isValid){
			throw new UnauthorizedException("Username or Password are incorrect");
		}

		var user = userEntityOptional.get();
		user.setPermissions(userPermissionRepository.findByUserId(user.getId()));
		user.setRoles(userProfileRepository.findByUserId(user.getId()));
		String jwtToken = jwtService.generateToken(UserMapper.toDto(user));
		AuthenticationResponseData authenticationResponse = new AuthenticationResponseData();
		authenticationResponse.setToken(jwtToken);
		return authenticationResponse;
	}

	public void changePassword(final Long userId, final ChangePasswordRequest changePasswordRequest) {

		var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

		// Validate if the current password matches
		if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())) {
			throw new UnauthorizedException("Current password is incorrect");
		}

		// Validate the new password
		if (!isValidPassword(changePasswordRequest.getNewPassword())) {
			throw new BusinessException("Invalid password format");
		}

		// Check if the new password is the same as the current password
		if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
			throw new BusinessException("New password cannot be the same as the current password");
		}

		// Encode and set the new password
		String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
	}

	private boolean isValidPassword(String password) {
		// Defining minimum requirements for password
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
		return password.matches(regex);
	}


	public void requestResetPassword(final Long userId) {
		// Generate a unique token for the user
		String resetToken = generateUniqueToken();

		// Store the token along with the user's ID and expiration timestamp
		createToken(userId, resetToken, LocalDateTime.now().plusMinutes(maximumTokenExpiration));

	}

	private void createToken(Long userId, String token, LocalDateTime expirationDateTime) {
		var resetToken = new PasswordResetTokenEntity();
		resetToken.setUserId(userId);
		resetToken.setToken(token);
		resetToken.setExpirationDateTime(expirationDateTime);
		resetTokenRepository.save(resetToken);
	}

	private String generateUniqueToken() {
		return UUID.randomUUID().toString();
	}

	public void resetPassword(final Long userId, final ResetPasswordRequest resetPasswordRequest) {

		var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

		// Validate the reset token
		var token = resetTokenRepository.findTokenByUser(userId,resetPasswordRequest.getResetToken())
				.orElseThrow(()-> new mz.gov.inage.authservice.exceptions.EntityNotFoundException("Token Not found"));

		if (token.isExpired()) {
			throw  new BusinessException("Invalid or expired token.");
		}

		// Validate the new password
		if (!isValidPassword(resetPasswordRequest.getNewPassword())) {
			throw new BusinessException("Invalid password format");
		}
		// Encode and set the new password
		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
	}
}
