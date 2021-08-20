package com.gcl.crm.security;

import com.gcl.crm.entity.Privilege;
import com.gcl.crm.entity.User;
import com.gcl.crm.repository.UserRepository;
import com.gcl.crm.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PrivilegeService privilegeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserName(username);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUserName(), user.getEncryptedPassword(), this.getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user){
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<Privilege> privileges = privilegeService.getUserPrivileges(user);
        for (Privilege privilege : privileges){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + privilege.getCode()));
        }
        return authorities;
    }
}

