package mz.gov.inage.authservice.entity;


import lombok.Data;
import mz.gov.inage.authservice.dto.EditProfileRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "PROFILE")
public class ProfileEntity extends LifeCycleEntity{

    @Column(name = "CODE", unique = true)
    private String code;

    @Column(name = "DESCRIPTION",nullable = false)
    private String description;

    public String getCode(){
        return this.code;
    }

    public void update(EditProfileRequest profileRequest) {
        if(profileRequest.getCode()!=null){
            this.setCode(profileRequest.getCode());
        }

        if(profileRequest.getDescription()!=null){
            this.setDescription(profileRequest.getDescription());
        }


    }
}
