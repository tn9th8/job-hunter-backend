package vn.nhannt.jobhunter.domain.entity;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.StatusEnum;

@Entity
@Table(name = "resumes")
@SQLDelete(sql = "UPDATE resumes SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class Resume implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TO DO @NotBlank
    private String email;
    private String url;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    // FK
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User property is not null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    @NotNull(message = "Job property is not null")
    private Job job;

    // log
    @CreationTimestamp
    private Instant createdAt;

    private String createdBy;

    @UpdateTimestamp
    private Instant updatedAt;

    private String updatedBy;

    private boolean deleted = Boolean.FALSE;

    @PrePersist
    private void setBeforeCreate() {
        this.createdBy = this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
    }

    @PreUpdate
    private void setBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
    }

}
