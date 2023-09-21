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
@Table(name = "USER_PERMISSION")
public class UserPermissionEntity extends LifeCycleEntity{

    @JoinColumn(name = "PERMISSION_ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private PermissionEntity permission;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @JoinColumn(name = "USER_ID", insertable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

}
