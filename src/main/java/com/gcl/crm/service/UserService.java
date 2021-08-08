package com.gcl.crm.service;

import com.gcl.crm.entity.Role;
import com.gcl.crm.entity.User;

import java.util.List;

public interface UserService {
    List<User> getUsersByIdList(List<Long> userIdList);
    User getUserByEmployeeId(Long employeeId);
    void disableUserByEmployeeId(Long employeeId);
    boolean checkUsername(String userName);
    User getUserByUsername(String userName);
    boolean changePassword(User modifiedUser, String newPassword);
}
