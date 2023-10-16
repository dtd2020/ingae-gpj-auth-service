package mz.gov.inage.authservice.service;

import javax.persistence.EntityNotFoundException;

import mz.gov.inage.authservice.dto.PermissionResponseData;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.entity.UserEntity;

import java.util.Set;

public interface IUserQueryService {
	UserResponseData findByUsername(String username) throws EntityNotFoundException;

	Set<ProfileResponseData> findAllProfiles();

	Set<PermissionResponseData> findAllPermissions();

	UserEntity findById(final Long id) throws EntityNotFoundException;
}
