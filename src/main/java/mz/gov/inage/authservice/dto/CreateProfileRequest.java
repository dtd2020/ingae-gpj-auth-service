package mz.gov.inage.authservice.dto;

import lombok.Data;

@Data
public class CreateProfileRequest {
    private String code;
    private String description;
}
