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

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.entity.Skill;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.service.SkillService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * create a new skill
     * 
     * @throws UniqueException
     */
    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill reqSkill) throws UniqueException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.skillService.createSkill(reqSkill));
    }

    @PatchMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@RequestBody Skill reqSkill) throws UniqueException {
        return ResponseEntity
                .ok()
                .body(this.skillService.updateSkill(reqSkill));
    }

    @DeleteMapping("skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delele(@PathVariable("id") Long id) {
        this.skillService.deleteSkill(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Skill> spec,
            Pageable pageable) {
        return ResponseEntity
                .ok()
                .body(this.skillService.findAllSkills(spec, pageable));
    }

    @GetMapping("/skills/{id}")
    @ApiMessage("Fetch one skill")
    public ResponseEntity<Skill> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity
                .ok()
                .body(this.skillService.findOneSkill(id));
    }

}
