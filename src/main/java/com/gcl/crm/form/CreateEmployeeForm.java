package com.gcl.crm.form;

import com.gcl.crm.entity.Employee;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
public class CreateEmployeeForm {
    private Long employeeId;
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
    private String avatar;

    public CreateEmployeeForm(){}

    public CreateEmployeeForm(Employee employee){
        this.employeeId = employee.getId();
        if (employee.getPosition() != null){
            this.positionId = employee.getPosition().getId();
        }
        if (employee.getDepartment() != null){
            this.departmentId = employee.getDepartment().getId();
        }
        this.phone = employee.getPhone();
        if (employee.getUser() != null){
            this.username = employee.getUser().getUserName();
        }
        this.note = employee.getNote();
        this.name = employee.getName();
        this.address = employee.getAddress();
        this.startDate = employee.getStartDate();
        this.birthDate = employee.getBirthDate();
        this.email = employee.getCompanyEmail();
        this.avatar = employee.getAvatar();
        this.employeeCode = employee.getCodename();
    }
}
