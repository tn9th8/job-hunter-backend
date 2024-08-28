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
import vn.nhannt.jobhunter.domain.entity.Subscriber;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.service.SubscriberService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @ApiMessage("Create a subscriber")
    @PostMapping("/subscribers")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber reqSub) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.subscriberService.createSubscriber(reqSub));
    }

    @ApiMessage("Update a subscriber")
    @PatchMapping("/subscribers")
    public ResponseEntity<Subscriber> update(@Valid @RequestBody Subscriber reqSub) {
        return ResponseEntity.ok()
                .body(this.subscriberService.updateSubscriber(reqSub));
    }

    @ApiMessage("Delete a subscriber")
    @DeleteMapping("/subscribers")
    public ResponseEntity<Void> delete() {
        this.subscriberService.deleteSubscriber();
        return ResponseEntity.noContent().build();
    }

    @ApiMessage("Fetch one subscriber")
    @GetMapping("/subscribers/{id}")
    public ResponseEntity<Subscriber> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(this.subscriberService.findSubscriberById(id));
    }

    @ApiMessage("Fetch all subscribers")
    @GetMapping("/subscribers")
    public ResponseEntity<ResPaginationDTO> fetchAll(
            @Filter Specification<Subscriber> spec,
            Pageable pageable) {
        return ResponseEntity.ok(
                this.subscriberService.findSubscribers(spec, pageable));
    }

    @ApiMessage("Fetch subscriber by user")
    @GetMapping("/subscribers/by-user")
    public ResponseEntity<Subscriber> fetchOneByUser() {
        return ResponseEntity.ok()
                .body(this.subscriberService.findSubscriberByUser());
    }

}
