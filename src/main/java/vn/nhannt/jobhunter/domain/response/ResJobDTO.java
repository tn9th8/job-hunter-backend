package vn.nhannt.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.domain.entity.Job;
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

    /**
     * map Job Entity to ResJobDTO
     * 
     * @param job
     * @return ResJobDTO with the skills field is a name list
     */
    public static ResJobDTO mappedBy(Job dbJob) {
        final ResJobDTO resJobDTO = new ResJobDTO(
                dbJob.getId(),
                dbJob.getName(),
                dbJob.getLocation(),
                dbJob.getSalary(),
                dbJob.getQuantity(),
                dbJob.getLevel(),
                dbJob.getDescription(),
                dbJob.getStartDate(),
                dbJob.getEndDate(),
                dbJob.isActive(),
                null,
                null,
                dbJob.getCreatedAt(),
                dbJob.getCreatedBy(),
                dbJob.getUpdatedAt(),
                dbJob.getUpdatedBy(),
                dbJob.isDeleted());
        // ckeck company
        if (dbJob.getCompany() != null) {
            ResJobDTO.CompanyFK company = new ResJobDTO.CompanyFK(
                    dbJob.getCompany().getId(),
                    dbJob.getCompany().getName());
            resJobDTO.setCompany(company);
        }
        // check skills
        if (dbJob.getSkills() != null) {
            List<String> strSKills = dbJob.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList());
            resJobDTO.setSkills(strSKills);
        }
        return resJobDTO;
    }
}
