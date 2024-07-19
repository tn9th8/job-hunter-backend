package vn.nhannt.jobhunter.domain.entity;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.Constants;
import vn.nhannt.jobhunter.util.constant.GenderEnum;

/**
 * A User.
 */

@Entity
@Table(name = "users")
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotBlank(message = "The email field is not null")
    private String email;

    // @JsonIgnore // ko lấy password => ảnh hưởng đến spring security
    @NotBlank(message = "The password field is not null")
    private String password;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String address;

    private String refreshToken;

    @CreationTimestamp
    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant createdAt;

    private String createdBy;

    @UpdateTimestamp
    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant updatedAt;

    private String updatedBy;

    @PrePersist
    private void setUserBeforeCreation() {
        this.createdBy = this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
    }

    @PreUpdate
    private void setUserBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
    }
}
