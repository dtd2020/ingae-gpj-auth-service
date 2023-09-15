package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.exceptions.BusinessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final PermissionRepository permissionRepository;
    public  void validateCreateUser(CreateUserRequest userRequest) {
        if(userRequest.getProfileId()==null){
            throw  new BusinessException("Profile should not be null");
        }
    }

    public void validateUpdateUser(EditUserRequest userRequest) {
    }

}
