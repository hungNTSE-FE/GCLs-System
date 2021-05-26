package com.gcl.crm.service;

import com.gcl.crm.entity.Company;
import com.gcl.crm.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company findCompanyById(Long id) {
        return companyRepository.findById(id).get();
    }
}
