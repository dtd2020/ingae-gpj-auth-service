package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.entity.ProfileEntity;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.entity.UserPermissionEntity;
import mz.gov.inage.authservice.mapper.ProfileMapper;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.ProfileRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.exceptions.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
	private final UserRepository userRepository;
	private final UserValidator userValidator;

	private final ProfileRepository profileRepository;
	private final ProfileMapper profileMapper;

	@Override
	public UserResponseData createUser(CreateUserRequest userRequest) throws BusinessException {

		userValidator.validateCreateUser(userRequest);

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String hashedPassword = encoder.encode(userRequest.getPassword());
		var user=UserMapper.toEntity(userRequest);
		user.setPassword(hashedPassword);
		userRepository.save(user);
		userRequest.getPermissions().stream().forEach(permissionId->{
			UserPermissionEntity userPermissionEntity=new UserPermissionEntity();
			userPermissionEntity.setUser(user);
			userPermissionEntity.setPermissionId(permissionId);
		});

		return UserMapper.toDto(user);
	}



	@Override
	public UserResponseData updateUser(Long userId, EditUserRequest userRequest) throws BusinessException {
		userValidator.validateUpdateUser(userRequest);
		var user =userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		user.updateFields(userRequest);
		userRepository.save(user);
		return UserMapper.toDto(user);
	}

	@Override
	public void inactivateUser(Long userId) throws BusinessException {
		var user=userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		user.setActive(false);
		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) throws BusinessException {
		var user=userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		userRepository.delete(user);
	}

	@Override
	public ProfileResponseData createProfile(CreateProfileRequest profileRequest) throws BusinessException {
		ProfileEntity profileEntity= new ProfileEntity();
		profileEntity.setCode(profileRequest.getCode());
		profileEntity.setDescription(profileRequest.getDescription());
		profileRepository.save(profileEntity);

		return profileMapper.toDto(profileEntity);
	}

	@Override
	public ProfileResponseData editProfile(Long profileId, EditProfileRequest profileRequest) throws BusinessException {
		var profile=profileRepository.findById(profileId)
				.orElseThrow(()->new EntityNotFoundException("No Profile found by the given Id"));
		profile.setDescription(profileRequest.getDescription());
		profile.setCode(profileRequest.getCode());
		profileRepository.save(profile);
		return profileMapper.toDto(profile);
	}

	@Override
	public void deleteProfile(Long profileId) throws BusinessException {
		var profile=profileRepository.findById(profileId)
				.orElseThrow(()->new EntityNotFoundException("No Profile found by the given Id"));
		profileRepository.delete(profile);
	}
}
