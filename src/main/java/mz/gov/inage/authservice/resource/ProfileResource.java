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
import mz.gov.inage.authservice.dto.PermissionResponseData;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
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
import java.util.Set;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@Api(tags = "Profile")
public class ProfileResource {
	private final IUserQueryService userQueryService;
	private final IUserService userService;


	@ApiOperation(value = "Create Profile", notes = "Creates a new user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile created successfully",response = ProfileResponseData.class),
			@ApiResponse(code = 400, message = "Invalid profile data")
	})
	@PostMapping("/{userId}")
	public ResponseEntity<ProfileResponseData> createProfile(@Valid @RequestBody CreateProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.createProfile(profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@ApiOperation(value = "Edit Profile", notes = "Edits an existing user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile edited successfully",response = ProfileResponseData.class),
			@ApiResponse(code = 400, message = "Invalid profile data"),
			@ApiResponse(code = 404, message = "Profile not found")
	})
	@PutMapping("/{profileId}")
	public ResponseEntity<ProfileResponseData> editProfile(@PathVariable Long profileId, @RequestBody EditProfileRequest profileRequest) {
		ProfileResponseData profileData = userService.editProfile(profileId, profileRequest);
		return ResponseEntity.ok(profileData);
	}

	@ApiOperation(value = "Delete Profile", notes = "Deletes an existing user profile.")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Profile deleted successfully"),
			@ApiResponse(code = 404, message = "Profile not found")
	})
	@DeleteMapping("/{profileId}")
	public ResponseEntity<Void> deleteProfile(@PathVariable Long profileId) {
		userService.deleteProfile(profileId);
		return ResponseEntity.noContent().build();
	}


	@ApiOperation(value = "Get all  Profiles", notes = "Get all  Profiles")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List of profiles"),

	})
	@GetMapping("/")
	public ResponseEntity<Set<ProfileResponseData>> getAllProfiles() {
		return ResponseEntity.ok(userQueryService.findAllProfiles());
	}

	@ApiOperation(value = "Get all  Permissions", notes = "Get all  Permissions")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List of permissions"),

	})
	@GetMapping("/permissions")
	public ResponseEntity<Set<PermissionResponseData>> getAllPermissions() {
		return ResponseEntity.ok(userQueryService.findAllPermissions());
	}
}





