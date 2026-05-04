package ch.zhaw.freelancer4u.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.CompanyCreateDTO;
import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.security.Roles;
import ch.zhaw.freelancer4u.service.UserService;

@RestController
@RequestMapping("/api")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserService userService;

    @PostMapping("/company")
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateDTO fDTO) {
        if (!userService.userHasRole(Roles.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Company fDAO = new Company(fDTO.getName(), fDTO.getEmail());
        Company f = companyRepository.save(fDAO);
        return new ResponseEntity<>(f, HttpStatus.CREATED);
    }

    @GetMapping("/company")
    public ResponseEntity<Page<Company>> getAllCompanies(
            @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "4") Integer pageSize) {
        if (!userService.userHasRole(Roles.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Page<Company> allCompanies = companyRepository.findAll(PageRequest.of(pageNumber - 1, pageSize));
        return new ResponseEntity<>(allCompanies, HttpStatus.OK);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        if (!userService.userHasRole(Roles.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Company> optCompany = companyRepository.findById(id);
        if (optCompany.isPresent()) {
            return new ResponseEntity<>(optCompany.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
