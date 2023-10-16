package mz.gov.inage.authservice.service;

import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.dto.PermissionResponseData;
import mz.gov.inage.authservice.dto.ProfileResponseData;
import mz.gov.inage.authservice.dto.UserResponseData;
import mz.gov.inage.authservice.mapper.PermissionMapper;
import mz.gov.inage.authservice.mapper.ProfileMapper;
import mz.gov.inage.authservice.mapper.UserMapper;
import mz.gov.inage.authservice.repository.PermissionRepository;
import mz.gov.inage.authservice.repository.ProfileRepository;
import mz.gov.inage.authservice.repository.UserPermissionRepository;
import mz.gov.inage.authservice.repository.UserProfileRepository;
import mz.gov.inage.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import mz.gov.inage.authservice.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements IUserQueryService {
	private final UserRepository userRepository;

	private final ProfileRepository profileRepository;

	private final PermissionRepository permissionRepository;

	private final UserPermissionRepository userPermissionRepository;

	private final UserProfileRepository userProfileRepository;

	@Override
	public UserResponseData findByUsername(String username) throws EntityNotFoundException {

		var user =this.userRepository.findByUsername(username).orElseThrow(() ->
				new EntityNotFoundException(String.format("User with the following username %s  was not found",username)));
		user.setPermissions(userPermissionRepository.findByUserId(user.getId()));
		user.setRoles(userProfileRepository.findByUserId(user.getId()));
		return UserMapper.toDto(user);
	}

	@Override
	public Set<ProfileResponseData> findAllProfiles() {
		return profileRepository.findAll().stream().map(
				ProfileMapper::toDto).collect(Collectors.toSet());
	}

	@Override
	public Set<PermissionResponseData> findAllPermissions() {
		return permissionRepository.findAll().stream().map(
				PermissionMapper::toDto).collect(Collectors.toSet());
	}

	@Override
	public UserEntity findById(Long id) throws EntityNotFoundException {
		var user =this.userRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException(String.format("User with the following id %s  was not found",id+"")));
		user.setPermissions(userPermissionRepository.findByUserId(user.getId()));
		user.setRoles(userProfileRepository.findByUserId(user.getId()));
		return user;
	}

}
