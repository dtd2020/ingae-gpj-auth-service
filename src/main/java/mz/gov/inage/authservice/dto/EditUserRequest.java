package mz.gov.inage.authservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class EditUserRequest {

	@NotNull
	private String username;

	@NotNull
	private String name;

	@NotNull
	@Size(min = 4,max=100)
	private String device;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Size(min = 4,max=21)
	private String mobile;

	@NotNull
	private Long createdBy;

	@NotNull
	@Size(min = 1,max = 100)
	private Set<CreatePermissionRequest> permissions;

	@NotNull
	private CreateProfileRequest profile;

}
