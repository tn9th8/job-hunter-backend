package vn.nhannt.jobhunter.domain.entity;

import java.io.Serializable;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.Constants;

@Entity
@Table(name = "companies")
@Getter
@Setter
// @Data // auto create toString, constructor => unsafe
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description; // MEDIUM_TEXT of MySQL with 16 MB

    private String address;

    private String logo;

    @CreationTimestamp
    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7) // return GMT+7, but DB save GMT+0
    private Instant createdAt; // Datetime

    private String createdBy;

    @UpdateTimestamp
    @JsonFormat(pattern = Constants.Datetime, timezone = Constants.GMT7)
    private Instant updatedAt;

    private String updatedBy;

    // before go to database
    @PrePersist
    public void setLastPersist() {
        // this.createdAt = Instant.now();
        this.createdBy = this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }

    @PreUpdate
    public void setLastUpdate() {
        // this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
    }
}
