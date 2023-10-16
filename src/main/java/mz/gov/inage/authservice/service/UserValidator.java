package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.exceptions.BusinessException;
import mz.gov.inage.authservice.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final PermissionRepository permissionRepository;

    private final UserRepository userRepository;
    public  void validateCreateUser(CreateUserRequest userRequest) {
        if(userRepository.findByUsername(userRequest.getUsername()).isPresent()){
            throw  new BusinessException("User with the provided username already exists");
        }
    }

    public void validateUpdateUser(EditUserRequest userRequest) {
    }

}
