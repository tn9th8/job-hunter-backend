package vn.nhannt.jobhunter.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.constant.LevelEnum;

/**
 * Define Job Entity
 * 
 * @Properties
 *             Long id;
 *             String name;
 *             String location;
 *             double salary;
 *             int quantity;
 *             LevelEnum level;
 *             String description;
 *             Instant startDate;
 *             Instant endDate;
 *             boolean active;
 *             Company company;
 *             List<Skill> skills;
 */
@Entity
@Table(name = "jobs")
@SQLDelete(sql = "UPDATE jobs SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
@Getter
@Setter
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TO DO @NotBlank
    private String name;
    private String location;
    private double salary;
    private int quantity;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean active;

    // FK
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Resume> resumes;

    // TO DO: use set instead of list => service check CRUD same skill id
    /*
     * TO DO: Soft delete:
     * - @SQLRestriction("status <> 'DELETED'")
     * - @SQLJoinTableRestriction("status = 'ACTIVE'")
     */
    /*
     * Jobs is master (owner) in many to many relationship => Suy ra:
     * - it define @JoinTable
     * - khi xóa Jobs => sẽ dẫn đến xóa record trong JoinTable
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = { "jobs" })
    private List<Skill> skills;

    // log
    @CreationTimestamp
    private Instant createdAt;

    private String createdBy;

    @UpdateTimestamp
    private Instant updatedAt;

    private String updatedBy;

    private boolean deleted = Boolean.FALSE;

    // hooks
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
