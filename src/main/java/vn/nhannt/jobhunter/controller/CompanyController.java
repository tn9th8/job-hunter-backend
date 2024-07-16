package vn.nhannt.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.nhannt.jobhunter.domain.Company;
import vn.nhannt.jobhunter.domain.dto.ReqCompanyDTO;
import vn.nhannt.jobhunter.domain.dto.ResPaginationDTO;
import vn.nhannt.jobhunter.service.CompanyService;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createUser(@Valid @RequestBody ReqCompanyDTO reqCompanyDTO) {
        final Company company = this.companyService.save(reqCompanyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PatchMapping("/companies")
    public ResponseEntity<Company> updateUser(@Valid @RequestBody Company company) {
        final Company updatedCompany = this.companyService.update(company);
        return ResponseEntity.ok(updatedCompany);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResPaginationDTO> fetchAllUsers(
            @Filter Specification<Company> spec,
            Pageable pageable) {

        return ResponseEntity.ok(this.companyService.findAll(spec, pageable));
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> fetchOneUser(@PathVariable("id") String id) {
        // TO DO: đưa logic vào service
        try {
            Long validId = Long.valueOf(id);
            Company company = this.companyService.findOne(validId);
            return ResponseEntity.ok(company);

        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Can not convert a ID PathVariable from String to Long");
        }

    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        // TO DO: đưa logic vào service
        try {
            Long validId = Long.valueOf(id);
            this.companyService.delete(validId);
            return ResponseEntity.noContent().build();

        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Can not convert a ID PathVariable from String to Long");
        }

    }
}
