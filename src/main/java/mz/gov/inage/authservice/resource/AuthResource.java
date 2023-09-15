package mz.gov.inage.authservice.resource;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.service.AuthenticationService;
import mz.gov.inage.authservice.dto.AuthenticationResponseData;
import mz.gov.inage.authservice.dto.ChangePasswordRequest;
import mz.gov.inage.authservice.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthResource {

	private final  AuthenticationService authService;
	
	@PostMapping("/login")
	ResponseEntity<AuthenticationResponseData> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(this.authService.authenticate(request));
	}

	@PostMapping("/change-password")
	public ResponseEntity<Void> changePassword(@RequestParam Long userId, @RequestBody ChangePasswordRequest changePasswordRequest) {
		authService.changePassword(userId, changePasswordRequest);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/request-reset-password")
	public ResponseEntity<Void> requestResetPassword(@RequestParam Long userId) {
		authService.requestResetPassword(userId);
		return ResponseEntity.noContent().build();
	}

}
