package mz.gov.inage.authservice.service;


import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.exceptions.BusinessException;

public interface IUserService {

	UserResponseData createUser(final CreateUserRequest userRequest) throws BusinessException;

	UserResponseData updateUser(final Long userId,final EditUserRequest userRequest) throws BusinessException;

	void inactivateUser(final Long userId) throws BusinessException;

	void deleteUser(final Long userId) throws BusinessException;

	ProfileResponseData createProfile(final CreateProfileRequest profileRequest) throws BusinessException;

	ProfileResponseData editProfile(Long profileId, final EditProfileRequest profileRequest) throws BusinessException;

	void deleteProfile(final Long profileId) throws BusinessException;
}
