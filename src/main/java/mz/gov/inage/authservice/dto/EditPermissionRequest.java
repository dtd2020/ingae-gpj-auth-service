package mz.gov.inage.authservice.dto;

import lombok.Data;

@Data
public class EditPermissionRequest {
    private String code;
    private String description;
}
