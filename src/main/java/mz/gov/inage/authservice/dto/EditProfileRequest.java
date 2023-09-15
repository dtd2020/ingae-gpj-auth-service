package mz.gov.inage.authservice.dto;


import lombok.Data;

@Data
public class EditProfileRequest {
    private String code;
    private String description;
}
