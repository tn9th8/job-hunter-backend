package vn.nhannt.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class MailController {

    @ApiMessage("Send mail")
    @GetMapping("/mail")
    public String sendMail() {
        return "ok";
    }

}
