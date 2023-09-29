package mz.gov.inage.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class UserResponseData {

	private Long id;

	private String username;

	private String name;

	private String device;

	private String email;

	private String mobile;

	private Long createdBy;

	private boolean active;

	private LocalDateTime lastLoggedIn;

	private Set<ProfileResponseData> profiles;

	private Set<PermissionResponseData> permissions;

}
