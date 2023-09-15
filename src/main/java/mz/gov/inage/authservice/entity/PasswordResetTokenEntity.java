package mz.gov.inage.authservice.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name = "USER_ID")
    private Long userId;

    @Column(nullable = false,name = "TOKEN")
    private String token;

    @Column(nullable = false,name = "EXPIRATION_DATE_TIME")
    private LocalDateTime expirationDateTime;

    public boolean isExpired(){
        return expirationDateTime.isBefore(LocalDateTime.now());
    }

}

