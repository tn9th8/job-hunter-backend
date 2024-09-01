package vn.nhannt.jobhunter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Company;
import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.entity.Skill;
import vn.nhannt.jobhunter.domain.entity.Subscriber;
import vn.nhannt.jobhunter.domain.response.ResEmailJob;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.JobRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillService skillService;
    private final CompanyService companyService;
    private final SubscriberService subscriberService;
    private final MailService mailService;

    public JobService(
            JobRepository jobRepository,
            SkillService skillService,
            CompanyService companyService, SubscriberService subscriberService, MailService mailService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
        this.companyService = companyService;
        this.subscriberService = subscriberService;
        this.mailService = mailService;
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
        final Job currentJob = this.findJobWithNotNull(reqJob.getId());

        // check skills
        if (reqJob.getSkills() != null) {
            final List<Long> skillIds = reqJob.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            final List<Skill> dbSkills = this.skillService.findSkillsByIds(skillIds);
            currentJob.setSkills(dbSkills);
        }
        // check company
        if (reqJob.getCompany() != null) {
            final Company dbCompany = this.companyService.findCompanyById(
                    reqJob.getCompany().getId());
            currentJob.setCompany(dbCompany);
        }
        // update partial
        currentJob.setName(reqJob.getName());
        currentJob.setSalary(reqJob.getSalary());
        currentJob.setQuantity(reqJob.getQuantity());
        currentJob.setLocation(reqJob.getLocation());
        currentJob.setLevel(reqJob.getLevel());
        currentJob.setStartDate(reqJob.getStartDate());
        currentJob.setEndDate(reqJob.getEndDate());
        currentJob.setActive(reqJob.isActive());
        return this.jobRepository.save(currentJob);
    }

    public void deleteJob(Long id) {
        // check id
        this.findJobWithNotNull(id);
        // delete
        this.jobRepository.deleteById(id);
    }

    public Job findJobWithNotNull(Long id) {
        final Optional<Job> optionalJob = this.jobRepository.findById(id); // check not null
        if (optionalJob.isEmpty()) {
            throw new IllegalArgumentException("Job is not found with " + id);
        }
        return optionalJob.get();
    }

    /**
     * find all Jobs with pagination
     * @param spec
     * @param pageable
     * @return
     */
    public ResPaginationDTO findJobs(Specification<Job> spec, Pageable pageable) {
        final Page<Job> page = this.jobRepository.findAll(spec, pageable);

        final ResPaginationDTO resPagination = new ResPaginationDTO();
        resPagination.setResult(page.getContent());

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        resPagination.setMeta(meta);

        return resPagination;
    }

    public void mailJobsToSubscriber() {
        // TO DO ko nên lấy tất mà phân trang dữ liệu
        List<Subscriber> subscribers = this.subscriberService.findSubscribers();

        if (!subscribers.isEmpty()) {
            subscribers.stream()
                    .filter(sub -> !sub.getSkills().isEmpty())
                    .forEach(sub -> {
                        List<Job> jobs = this.jobRepository.findBySkillsIn(sub.getSkills());

                        if (!jobs.isEmpty()) {
                            Map<String, Object> variables = new HashMap<>();

                            // dto job in front
                            // because not share data/session amount threads
                            List<ResEmailJob> resEmailJobs = jobs.stream()
                                            .map(job -> ResEmailJob.mapFrom(job))
                                            .toList();

                            variables.put("name", sub.getUser().getName());
                            variables.put("jobs", resEmailJobs);

                            this.mailService.sendEmailFromTemplateSync(
                                    sub.getUser().getEmail(),
                                    "Cơ hội công việc tốt đang chờ đón bạn, khám phá ngay !",
                                    "job",
                                    variables
                            );
                        }
                    });
        }
    }
}
