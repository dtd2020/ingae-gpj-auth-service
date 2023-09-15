package mz.gov.inage.authservice.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "USER_PROFILE")
public class ProfilePermissionEntity extends LifeCycleEntity {

    @Column(name = "PROFILE_ID", nullable = false)
    private Long profileId;

    @JoinColumn(name = "PROFILE_ID", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfileEntity profile;

    @Column(name = "PERMISSION_ID", nullable = false)
    private Long permissionId;

    @JoinColumn(name = "PERMISSION_ID", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private PermissionEntity permission;

}
