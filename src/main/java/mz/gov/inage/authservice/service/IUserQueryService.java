package mz.gov.inage.authservice.service;

import javax.persistence.EntityNotFoundException;

import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.entity.UserEntity;

public interface IUserQueryService {
	UserResponseData findByUsername(String username) throws EntityNotFoundException;
}
