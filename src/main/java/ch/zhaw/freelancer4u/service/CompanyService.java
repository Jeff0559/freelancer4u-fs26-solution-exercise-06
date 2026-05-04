package ch.zhaw.freelancer4u.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.repository.CompanyRepository;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    public boolean companyExists(String companyId) {
        return companyRepository.existsById(companyId);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public void createCompany(String name, String email) {
        Company company = new Company(name, email);
        companyRepository.save(company);
    }
}
