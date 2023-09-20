package mz.gov.inage.authservice.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.dto.AuthenticationResponseData;
import mz.gov.inage.authservice.dto.ChangePasswordRequest;
import mz.gov.inage.authservice.dto.RegisterRequest;
import mz.gov.inage.authservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api(tags = "Authentication")
@RequiredArgsConstructor
public class AuthResource {

	private final AuthenticationService authService;

	@PostMapping("/login")
	@ApiOperation(value = "Authenticate User", notes = "Authenticate a user and return an authentication token.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User authenticated successfully"),
			@ApiResponse(code = 401, message = "Username or Password are incorrect")
	})
	public ResponseEntity<AuthenticationResponseData> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(this.authService.authenticate(request));
	}

	@PostMapping("/change-password")
	@ApiOperation(value = "Change Password", notes = "Change the password for a user.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Password changed successfully"),
			@ApiResponse(code = 401, message = "Current password is incorrect"),
			@ApiResponse(code = 400, message = "Invalid or expired token."),
			@ApiResponse(code = 400, message = "New password cannot be the same as the current password"),
			@ApiResponse(code = 400, message = "Invalid password format")
	})
	public ResponseEntity<Void> changePassword(
			@ApiParam(value = "userId", required = true)
			@RequestParam("userId") Long userId,
			@RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
		authService.changePassword(userId, changePasswordRequest);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/request-reset-password")
	@ApiOperation(value = "Request Password Reset", notes = "Request a password reset for a user.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Password reset requested successfully")
	})
	public ResponseEntity<Void> requestResetPassword(
			@ApiParam(value = "userId", required = true)
			@RequestParam("userId") Long userId) {
		authService.requestResetPassword(userId);
		return ResponseEntity.noContent().build();
	}
}
