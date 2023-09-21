package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.entity.ProfileEntity;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.entity.UserPermissionEntity;
import mz.gov.inage.authservice.mapper.ProfileMapper;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.repository.ProfileRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import mz.gov.inage.authservice.dto.CreateProfileRequest;
import mz.gov.inage.authservice.dto.CreateUserRequest;
import mz.gov.inage.authservice.dto.EditProfileRequest;
import mz.gov.inage.authservice.dto.EditUserRequest;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.exceptions.BusinessException;
import mz.gov.inage.authservice.security.UserSecurityHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
	private final UserRepository userRepository;
	private final UserValidator userValidator;

	private final ProfileRepository profileRepository;

	private final PasswordEncoder passwordEncoder;

	private final PermissionRepository permissionRepository;

	@Override
	public UserResponseData createUser(@Valid  CreateUserRequest userRequest) throws BusinessException {

		userValidator.validateCreateUser(userRequest);

		String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
		var user=UserMapper.toEntity(userRequest);
		user.setPassword(hashedPassword);
		user.setCreatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);
		addPermissions(userRequest, user);

		return UserMapper.toDto(user);
	}

	private  void addPermissions(CreateUserRequest userRequest, UserEntity user) {
		if(userRequest.getPermissions()!=null && !userRequest.getPermissions().isEmpty()){
			userRequest.getPermissions().stream().forEach(permissionId->{
				var permission=permissionRepository.findById(permissionId)
						.orElseThrow(()->new EntityNotFoundException("No permission found by the given Id"));
				UserPermissionEntity userPermissionEntity=new UserPermissionEntity();
				userPermissionEntity.setUser(user);
				userPermissionEntity.setPermission(permission);
			});
		}
	}


	@Override
	public UserResponseData updateUser(Long userId, @Valid  EditUserRequest userRequest) throws BusinessException {
		userValidator.validateUpdateUser(userRequest);
		var user =userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		user.updateFields(userRequest);
		user.setUpdatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);
		return UserMapper.toDto(user);
	}

	@Override
	public void inactivateUser(Long userId) throws BusinessException {
		var user=userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		user.setActive(false);
		user.setUpdatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);
	}

	@Override
	public void deleteUser(Long userId) throws BusinessException {
		var user=userRepository.findById(userId)
				.orElseThrow(()->new EntityNotFoundException("No user found by the given Id"));
		userRepository.delete(user);
	}

	@Override
	public ProfileResponseData createProfile(@Valid CreateProfileRequest profileRequest) throws BusinessException {
		ProfileEntity profileEntity= new ProfileEntity();
		profileEntity.setCode(profileRequest.getCode());
		profileEntity.setDescription(profileRequest.getDescription());
		profileEntity.setCreatedBy(UserSecurityHolder.getUserId());
		profileRepository.save(profileEntity);

		return ProfileMapper.toDto(profileEntity);
	}

	@Override
	public ProfileResponseData editProfile(Long profileId, @Valid EditProfileRequest profileRequest) throws BusinessException {
		var profile=profileRepository.findById(profileId)
				.orElseThrow(()->new EntityNotFoundException("No Profile found by the given Id"));
		profile.update(profileRequest);
		profile.setUpdatedBy(UserSecurityHolder.getUserId());
		profileRepository.save(profile);
		return ProfileMapper.toDto(profile);
	}

	@Override
	public void deleteProfile(Long profileId) throws BusinessException {
		var profile=profileRepository.findById(profileId)
				.orElseThrow(()->new EntityNotFoundException("No Profile found by the given Id"));
		profileRepository.delete(profile);
	}
}
