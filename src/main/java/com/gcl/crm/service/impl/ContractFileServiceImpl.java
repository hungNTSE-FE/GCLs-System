package com.gcl.crm.service.impl;

import com.gcl.crm.entity.ContractFile;
import com.gcl.crm.repository.ContractFileRepository;
import com.gcl.crm.service.ContractFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class ContractFileServiceImpl implements ContractFileService {

    @Autowired
    ContractFileRepository contractFileRepository;

    @Override
    public void download(ContractFile contractFile, HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue ="attachment; filename=" + contractFile.getName();
        response.setHeader(headerKey,headerValue);
        ServletOutputStream outputStream =response.getOutputStream();
        outputStream.write(contractFile.getContent());
        outputStream.close();
    }
}
