package mz.gov.inage.authservice.service;

import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import mz.gov.inage.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import mz.gov.inage.authservice.entity.UserEntity;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements IUserQueryService {
	private final UserRepository userRepository;

	@Override
	public UserEntity findByUsername(String username) throws EntityNotFoundException {
		return this.userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException(String.format("Utilizador  com o nome %s  nao encontrado",username)));
	}

}
