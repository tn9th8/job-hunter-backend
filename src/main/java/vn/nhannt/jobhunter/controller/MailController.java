package vn.nhannt.jobhunter.controller;

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

    @ApiMessage("Send email")
    @GetMapping("/email")
    public String sendEmail() {
        this.jobService.mailJobsToSubscriber();
        return "oke";
    }

}
