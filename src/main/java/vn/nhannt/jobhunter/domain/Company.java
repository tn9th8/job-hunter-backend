package vn.nhannt.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "companies")
@Getter
@Setter
// @Data // auto create toString, constructor => unsafe
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description; // MEDIUM_TEXT of MySQL with 16 MB

    private String address;

    private String logo;

    private Instant createdAt; // Datetime

    private Instant updatedAt;

    private Instant createdBy;

    private Instant updatedBy;
}
