package vn.nhannt.jobhunter.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.ApiMethodEnum;
import vn.nhannt.jobhunter.util.constant.ModuleEnum;

@Entity
@Table(name = "permissions")
@SQLDelete(sql = "UPDATE permissions SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "name field is not null")
    private String name;

    @NotBlank(message = "apiPath field is not null")
    private String apiPath;

    // @NotBlank(message = "method field is not null")
    @Enumerated(EnumType.STRING)
    private ApiMethodEnum method;

    // @NotBlank(message = "module field is not null")
    @Enumerated(EnumType.STRING)
    private ModuleEnum module;

    // FK
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

    // log
    @CreationTimestamp
    private String createdBy;
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
    private String updatedBy;

    private boolean deleted = Boolean.FALSE;

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
