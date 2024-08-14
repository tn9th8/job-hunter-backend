package vn.nhannt.jobhunter.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.entity.Resume;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.response.ResResumeDTO;
import vn.nhannt.jobhunter.repository.ResumeRepository;
import vn.nhannt.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserService userService;
    private final JobService jobService;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeService(
            ResumeRepository resumeRepository,
            UserService userService,
            JobService jobService) {
        this.resumeRepository = resumeRepository;
        this.userService = userService;
        this.jobService = jobService;
    }

    public Resume createResume(Resume reqResume) {
        // check user
        if (reqResume.getUser().getId() != null) {
            final User fkUser = this.userService.findOne(
                    reqResume.getUser().getId());
            reqResume.setUser(fkUser);
        } else {
            throw new IllegalArgumentException("User ID is not null");
        }
        // check job
        if (reqResume.getJob().getId() != null) {
            final Job fkJob = this.jobService.findJobOrException(
                    reqResume.getJob().getId());
            reqResume.setJob(fkJob);
        } else {
            throw new IllegalArgumentException("Job ID is not null");
        }
        // create
        return this.resumeRepository.save(reqResume);
    }

    public Resume updateResume(Resume reqResume) {
        // check id
        final Resume currentResume = this.findResume(reqResume.getId());
        // update partial
        currentResume.setStatus(reqResume.getStatus());
        return this.resumeRepository.save(currentResume);
    }

    public void deleteResume(Long id) {
        // check id & delete
        this.findResume(id);
        this.resumeRepository.deleteById(id);
    }

    public Resume findResume(Long id) {
        final Optional<Resume> optResume = this.resumeRepository.findById(id);
        if (!optResume.isPresent()) {
            throw new IllegalArgumentException("Resume is not found with id " + id);
        }
        return optResume.get();
    }

    public ResPaginationDTO findResumes(Specification<Resume> spec, Pageable pageable) {
        final Page<Resume> page = this.resumeRepository.findAll(spec, pageable);

        final ResPaginationDTO resPagination = new ResPaginationDTO();
        resPagination.setResult(page.getContent().stream()
                .map(resume -> ResResumeDTO.mapFrom(resume))
                .collect(Collectors.toList()));

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(page.getNumber() + 1);
        meta.setPageSize(page.getSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        resPagination.setMeta(meta);

        return resPagination;
    }

    public ResPaginationDTO findResumesByUser(Pageable pageable) {
        // get the login user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        // build query string
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        // fetch
        return this.findResumes(spec, pageable);
    }

}
