package mz.gov.inage.authservice.resource;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.service.IUserQueryService;
import mz.gov.inage.authservice.service.IUserService;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {
	private final IUserQueryService userQueryService;
	private final IUserService userService;

	@GetMapping("/username/{username}")
	ResponseEntity<UserEntity> findUserByUsername(@Valid @PathVariable("username") String username) throws EntityNotFoundException {
		UserEntity user = this.userQueryService.findByUsername(username);
		return ResponseEntity.ok(user);
	}

	@PostMapping
	public ResponseEntity<UserResponseData> createUser(@RequestBody @Valid CreateUserRequest userRequest) {
		UserResponseData userData = userService.createUser(userRequest);
		return ResponseEntity.ok(userData);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseData> updateUser(@PathVariable Long userId,@Valid @RequestBody EditUserRequest userRequest) {
		UserResponseData userData = userService.updateUser(userId, userRequest);
		return ResponseEntity.ok(userData);
	}

	@PutMapping("/{userId}/inactivate")
	public ResponseEntity<Void> inactivateUser(@PathVariable Long userId) {
		userService.inactivateUser(userId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{userId}/profile/create")
	public ResponseEntity<ProfileResponseData> createProfile(@PathVariable Long userId,@Valid @RequestBody CreateProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.createProfile(profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@PutMapping("/{userId}/profile/edit")
	public ResponseEntity<ProfileResponseData> editProfile(@PathVariable Long profileId, @RequestBody EditProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.editProfile(profileId,profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@DeleteMapping("/{userId}/profile")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
		userService.deleteProfile(profileId);
		return ResponseEntity.noContent().build();
	}
}




