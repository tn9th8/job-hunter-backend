package vn.nhannt.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.nhannt.jobhunter.domain.entity.Job;
import vn.nhannt.jobhunter.domain.response.ResJobDTO;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.service.JobService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResJobDTO> create(@RequestBody Job reqJob) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResJobDTO.mappedBy(
                        this.jobService.createJob(reqJob)));
    }

    @PatchMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResJobDTO> update(@RequestBody Job reqJob) {
        return ResponseEntity
                .ok()
                .body(ResJobDTO.mappedBy(
                        this.jobService.updateJob(reqJob)));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("delete a job")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        // this.jobService.deleteJob(Mapper.toLong(id));
        this.jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    // TO DO: map to ResJobDTO
    @GetMapping("/jobs")
    @ApiMessage("Fetch all jobs")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(this.jobService.findJobs(spec, pageable));
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Fetch one job")
    public ResponseEntity<ResJobDTO> getMethodName(@PathVariable("id") Long id) {
        return ResponseEntity
                .ok()
                .body(ResJobDTO.mappedBy(
                        this.jobService.findJobOrException(id)));
    }

}
