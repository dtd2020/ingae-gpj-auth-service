package mz.gov.inage.authservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class CreateUserRequest {

	@NotNull
	private String username;

	@NotNull
	private String name;

	@NotNull
	@Size(min = 4,max=100)
	private String password;

	@Size(min = 4,max=100)
	private String device;

	@Email
	private String email;

	@Size(min = 4,max=21)
	private String mobile;

	@NotNull
	private Long createdBy;

	@NotNull
	@Size(min = 1,max = 100)
	private Set<Long> permissions;

	@NotNull
	private Long profileId;

}
