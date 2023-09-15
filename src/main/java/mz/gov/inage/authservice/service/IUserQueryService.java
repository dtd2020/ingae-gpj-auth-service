package mz.gov.inage.authservice.service;

import javax.persistence.EntityNotFoundException;

import mz.gov.inage.authservice.entity.UserEntity;

public interface IUserQueryService {
	UserEntity findByUsername(String username) throws EntityNotFoundException;
}
