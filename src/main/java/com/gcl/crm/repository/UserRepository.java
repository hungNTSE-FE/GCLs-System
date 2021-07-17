package com.gcl.crm.repository;

import com.gcl.crm.entity.User;
import com.gcl.crm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmployee(Employee employee);
    boolean existsUserByUserName(String userName);
    User findUserByUserName(String userName);
    List<User> findAllByEnabled(boolean enabled);
    Optional<User> findByUserIdAndAndEnabled(Long id, boolean enabled);

}
