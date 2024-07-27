package vn.nhannt.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResJobDTO {
    private Long id;

    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private CompanyFK company;
    private List<String> skills;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private boolean deleted = Boolean.FALSE;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyFK {
        private Long id;
        private String name;
    }
}
