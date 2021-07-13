package com.gcl.crm.service;

import com.gcl.crm.entity.User;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getUsersByIdList(List<Long> aidList) {
        if (aidList == null){
            return null;
        }
        List<User> users = new ArrayList<>();
        for (int i = 0; i < aidList.size(); i++) {
            Optional<User> action = userRepository.findByUserIdAndAndEnabled(aidList.get(i), true);
            if (action.isPresent()){
                users.add(action.get());
            }
        }
        return users;
    }

    @Override
    public User getUserByUsername(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    public User getUserByEmployeeId(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        User user = userRepository.findUserByEmployee(employee);
        return user;
    }

    @Override
    public void disableUserByEmployeeId(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        User user = userRepository.findUserByEmployee(employee);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    public List<User> getUserByEnabled() {
        return userRepository.findAllByEnabled(true);
    }

    @Override
    public boolean checkUsername(String userName) {
        return userRepository.existsUserByUserName(userName);
    }
}
