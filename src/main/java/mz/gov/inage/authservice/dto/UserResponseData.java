package mz.gov.inage.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getDevice() {
		return device;
	}

	public String getEmail() {
		return email;
	}

	public String getMobile() {
		return mobile;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public boolean isActive() {
		return active;
	}

	public LocalDateTime getLastLoggedIn() {
		return lastLoggedIn;
	}
}
