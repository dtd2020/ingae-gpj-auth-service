package mz.gov.inage.authservice.dto;


import lombok.Data;

@Data
public class PermissionResponseData {
    private Long id ;
    private String code;
    private String description;
}
