package com.gcl.crm.service;

import com.gcl.crm.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsersByIdList(List<Long> aidList);
    User getUserByEmployeeId(Long employeeId);
    void disableUserByEmployeeId(Long employeeId);
    boolean checkUsername(String userName);
    User getUserByUsername(String userName);
    List<User> getUserByEnabled();
}
