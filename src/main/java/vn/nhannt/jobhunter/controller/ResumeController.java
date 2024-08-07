package vn.nhannt.jobhunter.controller;

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

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Resume;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.domain.response.ResResumeDTO;
import vn.nhannt.jobhunter.service.ResumeService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;

@Controller
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
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

    @ApiMessage("find one resume")
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResResumeDTO> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                ResResumeDTO.mapFrom(this.resumeService.findResume(id)));
    }

    @ApiMessage("find all resume")
    @GetMapping("/resumes")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Resume> spec,
            Pageable pageable) {
        return ResponseEntity.ok(
                this.resumeService.findResumes(spec, pageable));
    }

}
