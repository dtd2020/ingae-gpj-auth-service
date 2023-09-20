package mz.gov.inage.authservice.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.service.IUserQueryService;
import mz.gov.inage.authservice.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Api(tags = "User")
public class UserResource {
	private final IUserQueryService userQueryService;
	private final IUserService userService;

	@ApiOperation(value = "Find User by Username", notes = "Finds a user by their username.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User found successfully",response = UserResponseData.class),
			@ApiResponse(code = 404, message = "User not found")
	})
	@GetMapping("/username/{username}")
	ResponseEntity<UserResponseData> findUserByUsername(@Valid @PathVariable("username") String username) throws EntityNotFoundException {
		var user = this.userQueryService.findByUsername(username);
		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "Create User", notes = "Creates a new user.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User created successfully",response = UserResponseData.class),
			@ApiResponse(code = 400, message = "Invalid user data")
	})
	@PostMapping
	public ResponseEntity<UserResponseData> createUser(@RequestBody @Valid CreateUserRequest userRequest) {
		UserResponseData userData = userService.createUser(userRequest);
		return ResponseEntity.ok(userData);
	}

	@ApiOperation(value = "Update User", notes = "Updates an existing user.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User updated successfully",response = UserResponseData.class),
			@ApiResponse(code = 400, message = "Invalid user data"),
			@ApiResponse(code = 404, message = "User not found")
	})
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponseData> updateUser(@PathVariable Long userId, @Valid @RequestBody EditUserRequest userRequest) {
		UserResponseData userData = userService.updateUser(userId, userRequest);
		return ResponseEntity.ok(userData);
	}

	@ApiOperation(value = "Inactivate User", notes = "Inactivates an existing user.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "User inactivated successfully"),
			@ApiResponse(code = 404, message = "User not found")
	})
	@PutMapping("/{userId}/inactivate")
	public ResponseEntity<Void> inactivateUser(@PathVariable Long userId) {
		userService.inactivateUser(userId);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Delete User", notes = "Deletes an existing user.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "User deleted successfully"),
			@ApiResponse(code = 404, message = "User not found")
	})
	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Create Profile", notes = "Creates a new user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile created successfully",response = ProfileResponseData.class),
			@ApiResponse(code = 400, message = "Invalid profile data")
	})
	@PostMapping("/{userId}/profile/create")
	public ResponseEntity<ProfileResponseData> createProfile(@PathVariable Long userId, @Valid @RequestBody CreateProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.createProfile(profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@ApiOperation(value = "Edit Profile", notes = "Edits an existing user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile edited successfully",response = ProfileResponseData.class),
			@ApiResponse(code = 400, message = "Invalid profile data"),
			@ApiResponse(code = 404, message = "Profile not found")
	})
	@PutMapping("/{userId}/profile/edit")
	public ResponseEntity<ProfileResponseData> editProfile(@PathVariable Long profileId, @RequestBody EditProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.editProfile(profileId, profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@ApiOperation(value = "Delete Profile", notes = "Deletes an existing user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Profile deleted successfully"),
			@ApiResponse(code = 404, message = "Profile not found")
	})
	@DeleteMapping("/{userId}/profile")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
		userService.deleteProfile(profileId);
		return ResponseEntity.noContent().build();
	}
}





