package vn.nhannt.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Subscriber;
import vn.nhannt.jobhunter.domain.entity.User;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.SubscriberRepository;
import vn.nhannt.jobhunter.util.SecurityUtil;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    private final UserService userService;

    public SubscriberService(
            SubscriberRepository subscriberRepository,
            SkillService skillService,
            UserService userService) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
        this.userService = userService;
    }

    public Subscriber createSubscriber(Subscriber reqSub) {
        // check user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        final User currentUser = this.userService.findByUsername(email);
        if (currentUser.getSubscriber() != null)
            throw new DuplicateKeyException("Subscriber is duplicate");
        reqSub.setUser(currentUser);
        // check skills
        List<Long> skillIds = reqSub.getSkills().stream()
                .map(skill -> skill.getId())
                .collect(Collectors.toList());
        reqSub.setSkills(this.skillService.findSkillsByIds(skillIds));
        // create
        return this.subscriberRepository.save(reqSub);
    }

    public Subscriber updateSubscriber(Subscriber reqSub) {
        // check user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        final User currentUser = this.userService.findByUsername(email);
        reqSub.setId(currentUser.getId());
        reqSub.setUser(currentUser);
        // check skills
        List<Long> skillIds = reqSub.getSkills().stream()
                .map(skill -> skill.getId())
                .collect(Collectors.toList());
        reqSub.setSkills(this.skillService.findSkillsByIds(skillIds));
        // update
        return this.subscriberRepository.save(reqSub);
    }

    public void deleteSubscriber() {
        // check user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        final User currentUser = this.userService.findByUsername(email);

        if (currentUser.getSubscriber() == null)
            throw new DuplicateKeyException("Subscriber is not existed");
        // delete
        this.subscriberRepository.deleteById(currentUser.getId());
    }

    public Subscriber findSubscriberById(Long id) {
        final Optional<Subscriber> optSubscriber = this.subscriberRepository.findById(id);

        if (optSubscriber.isEmpty())
            throw new IllegalArgumentException("Subscriber is not found with " + id);

        return optSubscriber.get();
    }

    public ResPaginationDTO findSubscribers(Specification<Subscriber> spec, Pageable pageable) {
        final Page<Subscriber> page = this.subscriberRepository.findAll(spec, pageable);

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

    // other
    public Subscriber findSubscriberByUser() {
        // check user
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        final User currentUser = this.userService.findByUsername(email);

        if (currentUser.getSubscriber() == null)
            throw new IllegalArgumentException("Subscriber is not found with email " + email);

        return currentUser.getSubscriber();
    }

}
