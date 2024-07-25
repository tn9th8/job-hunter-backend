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
import vn.nhannt.jobhunter.domain.entity.Company;
import vn.nhannt.jobhunter.domain.request.ReqCompanyDTO;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.service.CompanyService;
import vn.nhannt.jobhunter.util.annotation.ApiMessage;
import vn.nhannt.jobhunter.util.error.UniqueException;

/**
 * TO DO: đưa api versioning vào property
 */
@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // TO DO: fix method name, api message
    @PostMapping("/companies")
    @ApiMessage("create a user")
    public ResponseEntity<Company> createUser(@Valid @RequestBody ReqCompanyDTO reqCompanyDTO) {
        final Company company = this.companyService.create(reqCompanyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @PatchMapping("/companies")
    @ApiMessage("update a user")
    public ResponseEntity<Company> updateUser(@Valid @RequestBody Company company) throws UniqueException {
        final Company updatedCompany = this.companyService.update(company);
        return ResponseEntity.ok(updatedCompany);
    }

    @GetMapping("/companies")
    @ApiMessage("find all users")
    public ResponseEntity<ResPaginationDTO> fetchAllUsers(
            @Filter Specification<Company> spec,
            Pageable pageable) {

        return ResponseEntity.ok(this.companyService.findAll(spec, pageable));
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<Company> fetchOneUser(@PathVariable("id") String id) throws UniqueException {
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
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) throws UniqueException {
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
