package com.gcl.crm.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
public class CreateEmployeeForm {
    private Long positionId;
    private Long departmentId;
    private String employeeCode;
    private String phone;
    private String username;
    private String rawPassword;
    private String name;
    private String email;
    private String address;
    private Date birthDate;
    private Date startDate;
    private String note;
}
