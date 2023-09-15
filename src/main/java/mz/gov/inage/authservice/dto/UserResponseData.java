package mz.gov.inage.authservice.dto;

import lombok.Data;

@Data
public class UserResponseData {

	private Long id;

	private String username;

	private String name;

	private String device;

	private String email;

	private String mobile;

	private Long createdBy;

	private boolean active;

}
