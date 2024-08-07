package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Company;
import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.entity.Skill;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillService skillService;
    private final CompanyService companyService;

    public JobService(
            JobRepository jobRepository,
            SkillService skillService,
            CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
        this.companyService = companyService;
    }

    public Job createJob(Job reqJob) {
        // check skills
        if (reqJob.getSkills() != null) {
            final List<Long> skillIds = reqJob.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            final List<Skill> dbSkills = this.skillService.findSkillsByIds(skillIds);
            reqJob.setSkills(dbSkills);
        }
        // check company
        if (reqJob.getCompany() != null) {
            final Company dbCompany = this.companyService.findCompanyById(
                    reqJob.getCompany().getId());
            reqJob.setCompany(dbCompany);
        }
        // insert
        return this.jobRepository.save(reqJob);
    }

    public Job updateJob(Job reqJob) {
        // TO DO: nếu FK của req và current giống id thì có nên check ko ?
        // vì check sẽ làm giảm hiệu năng

        // check id
        final Job currentJob = this.findJobOrException(reqJob.getId());
        // check skills
        if (reqJob.getSkills() != null) {
            final List<Long> skillIds = reqJob.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            final List<Skill> dbSkills = this.skillService.findSkillsByIds(skillIds);
            reqJob.setSkills(dbSkills);
        }
        // check company
        if (reqJob.getCompany() != null) {
            final Company dbCompany = this.companyService.findCompanyById(
                    reqJob.getCompany().getId());
            reqJob.setCompany(dbCompany);
        }
        // handle the put method
        reqJob.setCreatedAt(currentJob.getCreatedAt());
        reqJob.setCreatedBy(currentJob.getCreatedBy());
        // update
        return this.jobRepository.save(reqJob);
    }

    public Job findJobOrException(Long id) {
        final Optional<Job> optionalJob = this.jobRepository.findById(id); // check not null
        if (!optionalJob.isPresent()) {
            throw new IllegalArgumentException("Job is not found with " + id);
        }
        return optionalJob.get();
    }

    public void deleteJob(Long id) {
        // check id
        this.findJobOrException(id);
        // delete
        this.jobRepository.deleteById(id);
    }

    public ResPaginationDTO findJobs(Specification<Job> spec, Pageable pageable) {
        final Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);

        final ResPaginationDTO resPagination = new ResPaginationDTO();
        resPagination.setResult(pageJob.getContent());

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageJob.getNumber() + 1);
        meta.setPageSize(pageJob.getSize());
        meta.setPages(pageJob.getTotalPages());
        meta.setTotal(pageJob.getTotalElements());
        resPagination.setMeta(meta);

        return resPagination;
    }

}
