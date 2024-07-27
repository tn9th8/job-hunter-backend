package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Skill;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.SkillRepository;
import vn.nhannt.jobhunter.util.error.UniqueException;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createSkill(Skill reqSkill) throws UniqueException {
        // check name is unique
        this.isNameUnique(reqSkill.getName());
        // save
        return this.skillRepository.save(reqSkill);
    }

    public Skill updateSkill(Skill reqSkill) throws UniqueException {
        // check name is unique
        this.isNameUnique(reqSkill.getName());
        // check id is existing
        final Skill currentSkill = this.findOneSkill(reqSkill.getId());
        // save
        currentSkill.setName(reqSkill.getName());
        return this.skillRepository.save(currentSkill);
    }

    public ResPaginationDTO findAllSkills(Specification<Skill> spec, Pageable pageable) {
        final Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageSkill.getNumber() + 1);
        meta.setPageSize(pageSkill.getSize());
        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        final ResPaginationDTO resPagination = new ResPaginationDTO();
        resPagination.setMeta(meta);
        resPagination.setResult(pageSkill.getContent());

        return resPagination;
    }

    public Skill findOneSkill(Long id) {
        final Optional<Skill> optionalSkill = this.skillRepository.findById(id);
        if (optionalSkill.isPresent() == false) {
            throw new IllegalArgumentException("Không tồn tại Skill có id là " + id);
        }
        return optionalSkill.get();
    }

    public void deleteSkill(Long id) {
        // TO DO: delete relevant records in skill_job table

        // check exist
        final Skill currentSkill = this.findOneSkill(id);
        // delete skill of relevant jobs
        currentSkill.getJobs().forEach(
                job -> {
                    job.getSkills().remove(currentSkill);
                });
        // delete skill
        this.skillRepository.deleteById(id);
    }

    // other
    public boolean isNameUnique(String name) throws UniqueException {
        if (this.skillRepository.existsByName(name)) {
            throw new UniqueException("Đã tồn tại a skill name này");
        }
        return true;
    }

    public List<Skill> findSkillsByIds(List<Long> ids) {
        return this.skillRepository.findAllById(ids);
    }

}
