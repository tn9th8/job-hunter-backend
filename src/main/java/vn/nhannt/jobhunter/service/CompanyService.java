package vn.nhannt.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.nhannt.jobhunter.domain.entity.Company;
import vn.nhannt.jobhunter.domain.request.ReqCompanyDTO;
import vn.nhannt.jobhunter.domain.response.ResPaginationDTO;
import vn.nhannt.jobhunter.repository.CompanyRepository;
import vn.nhannt.jobhunter.util.error.UniqueException;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * create a new company
     */
    public Company create(ReqCompanyDTO reqCompanyDTO) {
        final Company company = new Company();
        company.setName(reqCompanyDTO.getName());
        company.setDescription(reqCompanyDTO.getDescription());
        company.setAddress(reqCompanyDTO.getAddress());
        company.setLogo(reqCompanyDTO.getLogo());
        return this.companyRepository.save(company);
    }

    /**
     * update a existing company
     */
    @SuppressWarnings("null")
    public Company update(Company company) throws UniqueException {
        Company existingCompany = this.findOne(company.getId());

        if (existingCompany == null) {
            return null;
        }

        existingCompany.setDescription(company.getDescription());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setLogo(company.getLogo());
        existingCompany.setName(company.getName());

        return this.companyRepository.save(existingCompany);
    }

    /**
     * find all companies
     *
     * @return List<Company>
     */
    public ResPaginationDTO findAll(Specification<Company> spec, Pageable pageable) {
        final Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);

        final ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        // meta.setPage(pCompany.getNumber() + 1);
        // meta.setPageSize(pCompany.getSize());
        meta.setPages(pCompany.getTotalPages());
        meta.setTotal(pCompany.getTotalElements());

        final ResPaginationDTO presPaginationDTO = new ResPaginationDTO();
        presPaginationDTO.setMeta(meta);
        presPaginationDTO.setResult(pCompany.getContent()); // Returns the page content as {@link List}

        return presPaginationDTO;
    }

    /**
     * find one company
     */
    public Company findOne(Long id) throws UniqueException {
        final Optional<Company> optionalCompany = this.companyRepository.findById(id);
        if (optionalCompany.isPresent() == false) {
            throw new UniqueException("Không tồn tại Company với Id là " + id);
        }
        return optionalCompany.get();
    }

    public void delete(Long id) throws UniqueException {
        Company existingCompany = this.findOne(id);
        if (existingCompany != null) {
            this.companyRepository.deleteById(id);
        }
        // TO DO: null exception
    }

}
