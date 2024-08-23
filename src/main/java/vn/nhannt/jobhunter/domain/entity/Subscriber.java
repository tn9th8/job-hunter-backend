package vn.nhannt.jobhunter.domain.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "subscribers")
@SQLDelete(sql = "UPDATE permissions SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // FK
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = { "subscribers" })
    private List<Skill> skills;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    // log
    @CreationTimestamp
    private Instant createdAt;
    private String createdBy;

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
