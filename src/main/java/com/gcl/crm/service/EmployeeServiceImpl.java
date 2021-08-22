package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.EmployeeStatus;
import com.gcl.crm.enums.Status;
import com.gcl.crm.form.CreateEmployeeForm;
import com.gcl.crm.repository.EmployeeRepository;
import com.gcl.crm.repository.UserRepository;
import com.gcl.crm.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    MarketingGroupService marketingGroupService;

    @Autowired
    PositionService positionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getAllWorkingEmployees() {
        return employeeRepository.findAllByStatusNot(EmployeeStatus.OFF_WORKING);
    }

    @Override
    public List<Employee> getWorkingEmployeeByDepartment(Department department) {
        return employeeRepository.getEmployeesByStatusAndDepartmentId(EmployeeStatus.WORKING, department.getId());
    }

    @Override
    public List<Employee> getEmployeesByIdList(List<Long> aidList) {
        if (aidList == null) {
            return null;
        }
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < aidList.size(); i++) {
            Optional<Employee> employee = employeeRepository.findByIdAndStatusNot(aidList.get(i), EmployeeStatus.OFF_WORKING);
            if (employee.isPresent()) {
                employees.add(employee.get());
            }
        }
        return employees;
    }

    @Override
    @Transactional
    public boolean createEmployee(CreateEmployeeForm employeeForm, User currentUser, MultipartFile avatar)
            throws DuplicateKeyException {
        Position position = positionService.findPositionById(employeeForm.getPositionId());
        Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId().toString());

        // Init employee info
        Employee employee = new Employee();
        employee.setDepartment(department);
        employee.setPosition(position);
        employee.setCodename(employeeForm.getEmployeeCode());
        employee.setCompanyEmail(employeeForm.getEmail());
        employee.setPhone(employeeForm.getPhone());
        employee.setNote(employeeForm.getNote());
        employee.setStartDate(employeeForm.getStartDate());
        employee.setAddress(employeeForm.getAddress());
        employee.setBirthDate(employeeForm.getBirthDate());
        employee.setName(employeeForm.getName());
        employee.setStatus(EmployeeStatus.WORKING);
        // Init user account info
        User user = new User();
        if (userService.checkUsername(user.getUserName())){
            throw new DuplicateKeyException("Tên đăng nhập đã được sử dụng");
        }
        user.setUserName(employeeForm.getUsername());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setEncryptedPassword(bCryptPasswordEncoder.encode(employeeForm.getRawPassword()));
        user.setEnabled(true);
        user.setEmployee(employee);
        //
        employee.setUser(user);
        employee = employeeRepository.save(employee);
        if (avatar != null && !avatar.isEmpty()){
            this.saveAvatar(avatar, employee);
        }
        return employee != null;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()){
            return employee.get();
        }
        return null;
    }

    @Override
    public boolean updateEmployee(CreateEmployeeForm employeeForm, MultipartFile avatar) {
        Employee employee = this.getEmployeeById(employeeForm.getEmployeeId());
        if (employee == null){
            return false;
        }
        User user = userService.getUserByEmployeeId(employeeForm.getEmployeeId());
        if (employeeForm.getRawPassword() != null && !employeeForm.getRawPassword().isEmpty()){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setEncryptedPassword(encoder.encode(employeeForm.getRawPassword()));
            user = userRepository.save(user);

        }
        Position position = positionService.findPositionById(employeeForm.getPositionId());
        Department department = departmentService.findDepartmentById(employeeForm.getDepartmentId().toString());
        employee.setPosition(position);
        employee.setDepartment(department);
        employee.setAddress(employeeForm.getAddress());
        employee.setBirthDate(employeeForm.getBirthDate());
        employee.setStartDate(employeeForm.getStartDate());
        employee.setName(employeeForm.getName());
        employee.setNote(employeeForm.getNote());
        employee.setPhone(employeeForm.getPhone());
        employee.setCompanyEmail(employeeForm.getEmail());
        employee.setUser(user);
        employee = employeeRepository.save(employee);
        if (avatar != null && !avatar.isEmpty()){
            this.saveAvatar(avatar, employee);
        }
        return true;
    }

    @Override
    public boolean deleteEmployee(Long eid) {
        if (eid == null){
            return false;
        }
        Employee employee = this.getEmployeeById(eid);
        if (employee == null || employee.getStatus().equals(EmployeeStatus.OFF_WORKING)){
            return false;
        }
        employee.setStatus(EmployeeStatus.OFF_WORKING);
        userService.disableUserByEmployeeId(eid);
        employeeRepository.save(employee);
        return true;
    }

    @Override
    public List<Employee> getAllNotGroupedEmployees() {
        List<Employee> employees = this.getAllWorkingEmployees();
        return employees.stream().filter(emp -> Objects.isNull(emp.getMarketingGroup())).collect(Collectors.toList());
    }

    @Override
    public List<Employee> getAllGroupedEmployees(String id) {
        Long empGroupId = Long.parseLong(id);
        return employeeRepository.findAllById(empGroupId);
    }

    @Override
    public List<Employee> getAllWorkingEmployeesWithUserNotNull() {
        List<Employee> employees = employeeRepository.findAllByStatusNot(EmployeeStatus.OFF_WORKING);
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees){
            if (employee.getUser() == null){
                continue;
            }
            if (!employee.getUser().isEnabled()){
                continue;
            }
            result.add(employee);
        }
        return result;
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findEmployeeByCompanyEmail(email);
        if ( employee != null && employee.getStatus().equals(EmployeeStatus.WORKING)){
            return employee;
        }
        return null;
    }

    @Override
    public boolean isPhoneExisted(String phone, Long id) {
        Employee employee = (id != null) ? employeeRepository.findEmployeeByPhoneAndIdNot(phone, id)
                : employeeRepository.findEmployeeByPhone(phone);
        return employee != null;
    }

    @Override
    public boolean isEmailExisted(String email, Long id) {
        Employee employee = (id != null) ? employeeRepository.findEmployeeByCompanyEmailAndIdNot(email, id)
                : employeeRepository.findEmployeeByCompanyEmail(email);
        return employee != null;
    }

    private void saveAvatar(MultipartFile multipartFile, Employee employee){
        String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = "avatar/" + employee.getId();
        try {
            FileUploadUtils.saveFile(uploadDir, filename, multipartFile);
            employee.setAvatar(filename);
            employeeRepository.save(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
