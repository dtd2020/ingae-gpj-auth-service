package mz.gov.inage.authservice.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "USER_PROFILE")
public class UserProfileEntity extends LifeCycleEntity {

    @JoinColumn(name = "PROFILE_ID", nullable = false)
    @ManyToOne
    private ProfileEntity profile;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @JoinColumn(name = "USER_ID", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    public ProfileEntity getProfile(){
        return this.profile;
    }

}
