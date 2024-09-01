package vn.nhannt.jobhunter.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.service.JobService;
import vn.nhannt.jobhunter.service.MailService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    private final MailService mailService;
    private final JobService jobService;

    public MailController(MailService mailService, JobService jobService) {
        this.mailService = mailService;
        this.jobService = jobService;
    }

    /**
     * Send email to subscribers about jobs matching skills of ones
     * Schedule: every Saturday, 8pm
     * Transactional
     * - propagation: nhập vào session hiện có, nếu ko tự tạo session mới
     * - readOnly: chỉ cho đọc DB, từ đó tối ưu DB
     * - noRollbackFor: ko rollback với các lỗi thuộc lớp Exception
     */
    @ApiMessage("Send email to subscribers about jobs matching skills of ones")
    @GetMapping("/email")
    @Scheduled(cron = "0 0 20 ? * SAT")
    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public void sendEmail() {
        this.jobService.mailJobsToSubscriber();
        System.out.println(">>> CRON EMAIL");
    }

}
