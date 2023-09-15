package mz.gov.inage.authservice.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "PERMISSION")
public class PermissionEntity extends LifeCycleEntity {

    @Column(name = "CODE", unique = true,nullable = false)
    private String code;

    @Column(name = "DESCRIPTION",nullable = false)
    private String description;

}
