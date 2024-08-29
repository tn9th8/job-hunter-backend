package vn.nhannt.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.service.MailService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @ApiMessage("Send email")
    @GetMapping("/email")
    public String sendEmail() {
        // this.mailService.sendSimpleMail();
        // this.mailService.sendEmailSync(
        // "21110266@student.hcmute.edu.vn",
        // "Send mail",
        // "<h1> <b> Hello world </b> </h1>",
        // false,
        // true);
        this.mailService.sendEmailFromTemplateSync("21110266@student.hcmute.edu.vn", "Send mail", "job");
        return "ok";
    }

}
