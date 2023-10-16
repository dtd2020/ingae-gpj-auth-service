package mz.gov.inage.authservice.service;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.entity.ProfileEntity;
import mz.gov.inage.authservice.entity.UserEntity;
import mz.gov.inage.authservice.entity.UserPermissionEntity;
import mz.gov.inage.authservice.entity.UserProfileEntity;
import mz.gov.inage.authservice.mapper.ProfileMapper;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.repository.ProfileRepository;
import mz.gov.inage.authservice.repository.UserPermissionRepository;
import mz.gov.inage.authservice.repository.UserProfileRepository;
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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
	private final UserRepository userRepository;
	private final UserValidator userValidator;

	private final ProfileRepository profileRepository;

	private final PasswordEncoder passwordEncoder;

	private final PermissionRepository permissionRepository;

	private final UserPermissionRepository userPermissionRepository;

	private final UserProfileRepository userProfileRepository;

	private final IUserQueryService userQueryService;

	@Override
	public UserResponseData createUser(@Valid  CreateUserRequest userRequest) throws BusinessException {

		userValidator.validateCreateUser(userRequest);

		String hashedPassword = passwordEncoder.encode(userRequest.getPassword());
		var user=UserMapper.toEntity(userRequest);
		user.setPassword(hashedPassword);
		user.setCreatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);
		addPermissions(userRequest, user);
		addProfiles(userRequest, user);

		return UserMapper.toDto(user);
	}

	private  void addProfiles(CreateUserRequest userRequest, UserEntity user) {
		if(userRequest.getProfileIds()!=null){
			userRequest.getProfileIds().stream().forEach(profileId->{
				var profile=profileRepository.findById(profileId)
						.orElseThrow(()->new EntityNotFoundException("No profile found by the given Id"));
				var userProfile =new UserProfileEntity();
				userProfile.setUserId(user.getId());
				userProfile.setProfile(profile);
				userProfileRepository.save(userProfile);
			});
		}
	}

	private  void addPermissions(CreateUserRequest userRequest, UserEntity user) {
		if(userRequest.getPermissions()!=null){
			userRequest.getPermissions().stream().forEach(permissionId->{
				var permission=permissionRepository.findById(permissionId)
						.orElseThrow(()->new EntityNotFoundException("No permission found by the given Id"));
				UserPermissionEntity userPermissionEntity=new UserPermissionEntity();
				userPermissionEntity.setUserId(user.getId());
				userPermissionEntity.setPermission(permission);
				userPermissionRepository.save(userPermissionEntity);
			});
		}
	}


	@Override
	public UserResponseData updateUser(Long userId, @Valid  EditUserRequest userRequest) throws BusinessException {
		userValidator.validateUpdateUser(userRequest);
		var user =userQueryService.findById(userId);
		user.updateFields(userRequest);
		user.setUpdatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);
		updateProfiles(userRequest,user);
		updatePermissions(userRequest,user);
		return UserMapper.toDto(user);
	}

	private void updatePermissions(EditUserRequest userRequest, UserEntity user) {
		// Get the original permission IDs from the user entity
		Set<Long> originalPermissionIds =user.getPermissions()==null?new HashSet<>() :user.getPermissions()
				.stream()
				.map(permissionEntity -> permissionEntity.getPermission().getId())
				.collect(Collectors.toSet());

		// Get the new permission IDs from the userRequest
		Set<Long> newPermissionIds =userRequest.getPermissions()==null
				?new HashSet<>(): userRequest.getPermissions();

		// Find the added permissions (new - original)
		Set<Long> addedPermissionIds = newPermissionIds.stream()
				.filter(permissionId -> !originalPermissionIds.contains(permissionId))
				.collect(Collectors.toSet());

		// Find the removed permissions (original - new)
		Set<Long> removedPermissionIds = originalPermissionIds.stream()
				.filter(permissionId -> !newPermissionIds.contains(permissionId))
				.collect(Collectors.toSet());

		// Create UserPermissionEntity records for added permissions
		addedPermissionIds.forEach(permissionId -> {
			var permission = permissionRepository.findById(permissionId)
					.orElseThrow(() -> new EntityNotFoundException("No permission found by the given Id"));
			UserPermissionEntity userPermissionEntity = new UserPermissionEntity();
			userPermissionEntity.setUser(user);
			userPermissionEntity.setPermission(permission);
			userPermissionRepository.save(userPermissionEntity);
		});

		// Delete UserPermissionEntity records for removed permissions
		userPermissionRepository.deleteAllByIdInBatch(removedPermissionIds);

	}

	private void updateProfiles(EditUserRequest userRequest, UserEntity user) {
		// Get the original profile IDs from the user entity
		Set<Long> originalProfileIds =user.getRoles()==null?new HashSet<>(): user.getRoles()
				.stream()
				.map(profileEntity -> profileEntity.getProfile().getId())
				.collect(Collectors.toSet());

		// Get the new profile IDs from the userRequest
		Set<Long> newProfileIds = userRequest.getProfileIds()==null?new HashSet<>():userRequest.getProfileIds();

		// Find the added profiles (new - original)
		Set<Long> addedProfileIds =newProfileIds.stream()
				.filter(profileId -> !originalProfileIds.contains(profileId))
				.collect(Collectors.toSet());

		// Find the removed profiles (original - new)
		Set<Long> removedProfileIds =originalProfileIds.stream()
				.filter(profileId -> !newProfileIds.contains(profileId))
				.collect(Collectors.toSet());

		// Create UserProfileEntity records for added profiles
		addedProfileIds.forEach(profileId -> {
			var profile = profileRepository.findById(profileId)
					.orElseThrow(() -> new EntityNotFoundException("No profile found by the given Id"));
			UserProfileEntity userProfileEntity = new UserProfileEntity();
			userProfileEntity.setUser(user);
			userProfileEntity.setProfile(profile);
			userProfileRepository.save(userProfileEntity);
		});

		// Delete UserProfileEntity records for removed profiles
		userProfileRepository.deleteAllByIdInBatch(removedProfileIds);

	}



	@Override
	public void inactivateUser(Long userId) throws BusinessException {
		var user =userQueryService.findById(userId);
		user.setActive(false);
		user.setUpdatedBy(UserSecurityHolder.getUserId());
		userRepository.save(user);

		// Inactivate user permissions
		user.getPermissions().forEach(userPermission -> {
			userPermission.setActive(false);
			userPermission.setUpdatedBy(UserSecurityHolder.getUserId());
			userPermissionRepository.save(userPermission);
		});

		// Inactivate user profiles
		user.getRoles().forEach(userProfile -> {
			userProfile.setActive(false);
			userProfile.setUpdatedBy(UserSecurityHolder.getUserId());
			userProfileRepository.save(userProfile);
		});
	}

	@Override
	public void deleteUser(Long userId) throws BusinessException {
		var user =userQueryService.findById(userId);
		var profileIds = user.getRoles().stream().map(userProfileEntity -> userProfileEntity.getId()).collect(Collectors.toSet());
		var permissionIds = user.getPermissions().stream().map(userPermissionEntity -> userPermissionEntity.getId()).collect(Collectors.toSet());
		userProfileRepository.deleteAllByIdInBatch(profileIds);
		userPermissionRepository.deleteAllByIdInBatch(permissionIds);
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
