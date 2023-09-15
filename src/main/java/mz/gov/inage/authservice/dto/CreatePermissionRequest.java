package mz.gov.inage.authservice.dto;

import lombok.Data;

@Data
public class CreatePermissionRequest {
    private String code;
    private String description;
}
