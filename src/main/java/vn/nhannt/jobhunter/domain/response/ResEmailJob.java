package vn.nhannt.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.nhannt.jobhunter.domain.entity.Job;

import java.util.List;

@Getter
@Setter
public class ResEmailJob {
    private String name;
    private Double  salary;
    private Company company;
    private List<Skill> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Company {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Skill {
        private String name;
    }

    public static ResEmailJob mapFrom(Job job) {
        ResEmailJob resEmailJob = new ResEmailJob();
        resEmailJob.setName(job.getName());
        resEmailJob.setSalary(job.getSalary());
        resEmailJob.setCompany(new ResEmailJob.Company(job.getCompany().getName()));

        List<ResEmailJob.Skill> jobSkills = job.getSkills().stream()
                .map(skill -> new ResEmailJob.Skill(skill.getName()))
                .toList();
        resEmailJob.setSkills(jobSkills);

        return resEmailJob;
    }

}
