package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.config.JwtService;
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
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final  UserRepository userRepository;
	private final  JwtService jwtService;

	private final  BCryptPasswordEncoder passwordEncoder;

	private final PasswordResetTokenRepository resetTokenRepository;

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

		String jwtToken = jwtService.generateToken(userEntityOptional.get());
		AuthenticationResponseData authenticationResponse = new AuthenticationResponseData();
		authenticationResponse.setToken(jwtToken);
		return authenticationResponse;
	}

	public void changePassword(final Long userId, final ChangePasswordRequest changePasswordRequest) {

		var user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

		// Validate the reset token
		if (!isValidResetToken(changePasswordRequest.getResetToken())) {
			throw  new BusinessException("Invalid or expired token.");
		}

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

	private boolean isValidResetToken(String resetToken) {
		var token = resetTokenRepository.findByToken(resetToken);

		if (!token.isPresent() || token.get().isExpired()) {
			return false;
		}

		return true;
	}
}
