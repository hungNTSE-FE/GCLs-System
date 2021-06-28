package com.gcl.crm;

import com.gcl.crm.utils.EncryptedPasswordUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class CrmApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        String password = "123";
        String encryptedPassword = EncryptedPasswordUtils.encryptPassword(password);

        System.out.println("Encryted Password: " + encryptedPassword);
        SpringApplication.run(CrmApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CrmApplication.class);
    }
}
