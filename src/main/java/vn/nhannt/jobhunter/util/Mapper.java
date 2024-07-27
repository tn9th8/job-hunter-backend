package vn.nhannt.jobhunter.util;

import java.util.List;
import java.util.stream.Collectors;

import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.response.ResJobDTO;

public class Mapper {
    /**
     * map Job Entity to ResJobDTO
     * 
     * @param job
     * @return ResJobDTO with the skills field is a name list
     */
    public static ResJobDTO toResJobDTO(Job dbJob) {
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
