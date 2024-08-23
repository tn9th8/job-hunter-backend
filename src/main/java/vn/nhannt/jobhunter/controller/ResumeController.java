package vn.nhannt.jobhunter.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Company;
import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.entity.Resume;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.response.ResResumeDTO;
import vn.nhannt.jobhunter.service.ResumeService;
import vn.nhannt.jobhunter.service.UserService;
import vn.nhannt.jobhunter.util.SecurityUtil;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;

@Controller
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;
    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(
            ResumeService resumeService,
            UserService userService,
            FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @ApiMessage("create a resume")
    @PostMapping("/resumes")
    public ResponseEntity<ResResumeDTO> create(@Valid @RequestBody Resume resume) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResResumeDTO.mapFrom(this.resumeService.createResume(resume)));
    }

    @ApiMessage("update a resume")
    @PatchMapping("/resumes")
    public ResponseEntity<ResResumeDTO> update(@RequestBody Resume reqResume) {
        return ResponseEntity.ok(
                ResResumeDTO.mapFrom(this.resumeService.updateResume(reqResume)));
    }

    @ApiMessage("delete a resume")
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }

    @ApiMessage("fetch one resume")
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResResumeDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ResResumeDTO.mapFrom(this.resumeService.findResume(id)));
    }

    @ApiMessage("fetch all resumes")
    @GetMapping("/resumes")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        // get user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        User currentUser = this.userService.findByUsername(email);
        // get job ids by user
        List<Long> jobIds = null;
        if (currentUser != null) {
            Company currentCompany = currentUser.getCompany();
            if (currentCompany != null) {
                List<Job> currentJobs = currentCompany.getJobs();
                if (currentJobs != null && currentJobs.size() > 0) {
                    jobIds = currentJobs.stream()
                            .map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }
        // build query: job field in job ids
        Specification<Resume> jobSpec = filterSpecificationConverter
                .convert(filterBuilder.field("job")
                        .in(filterBuilder.input(jobIds))
                        .get());

        // build query: job field + user
        Specification<Resume> resumeSpec = jobSpec.and(spec);
        // get
        return ResponseEntity.ok(
                this.resumeService.findResumes(resumeSpec, pageable));
    }

    // TO DO FE
    @ApiMessage("fetch resumes by user")
    @PostMapping("/resumes/by-user")
    // @GetMapping("/resumes/by-user")
    public ResponseEntity<ResPaginationDTO> fetchAllByUser(@Filter Pageable pageable) {
        return ResponseEntity.ok()
                .body(this.resumeService.findResumesByUser(pageable));
    }

}
