package mz.gov.inage.authservice.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import mz.gov.inage.authservice.dto.EditUserRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USER")
@Data
@AllArgsConstructor
@Builder
public class UserEntity extends LifeCycleEntity implements UserDetails {

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "NAME")
	private String name;

	@Column(name = "LAST_LOGGED_IN")
	private LocalDateTime lastLoggedIn;
	@Column(name = "DEVICE")
	private String device;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "PASSWORD_EXPIRATION_DATE")
	private LocalDateTime passwordExpirationDate;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<UserProfileEntity> roles;

	@Override
	public String getUsername(){
		return this.username;
	}

	@Override
	public String getPassword(){
		return this.password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream().map(role->new SimpleGrantedAuthority(role.getProfile().getCode()))
				.collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		return isCredentialsNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return passwordExpirationDate!=null
				&& passwordExpirationDate.isAfter(LocalDateTime.now());
	}

	@Override
	public boolean isEnabled() {
		return super.isActive();
	}

	public Map<String, Object> updateFields(EditUserRequest other) {
		var changes = new HashMap<String, Object>();

		if (!Objects.equals(this.name, other.getName())) {
			this.name = other.getName();
			changes.put("name", other.getName());
		}

		if (!Objects.equals(this.username, other.getUsername())) {
			this.username = other.getUsername();
			changes.put("username", other.getUsername());
		}

		if (!Objects.equals(this.device, other.getDevice())) {
			this.device = other.getDevice();
			changes.put("device", other.getDevice());
		}

		if (!Objects.equals(this.email, other.getEmail())) {
			this.email = other.getEmail();
			changes.put("email", other.getEmail());
		}

		if (!Objects.equals(this.mobile, other.getMobile())) {
			this.mobile = other.getMobile();
			changes.put("mobile", other.getMobile());
		}

		return changes;
	}



}
