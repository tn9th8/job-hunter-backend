package vn.nhannt.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nhannt.jobhunter.domain.dto.ResHelloDTO;

@RestController
public class HelloController {

    @GetMapping("/")
    // @CrossOrigin
    public ResponseEntity<ResHelloDTO> getHelloWorld() {
        ResHelloDTO resHelloDTO = new ResHelloDTO();
        resHelloDTO.setMessage("Hello World (Hỏi Dân IT & Eric)");
        return ResponseEntity.ok().body(resHelloDTO);
    }
}
