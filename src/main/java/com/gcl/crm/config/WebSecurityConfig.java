package com.gcl.crm.config;

import com.gcl.crm.entity.Privilege;
import com.gcl.crm.security.UserDetailsServiceImpl;
import com.gcl.crm.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PrivilegeService privilegeService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        List<Privilege> privileges = privilegeService.getAllPrivileges();
        for (Privilege privilege : privileges){
            http.authorizeRequests()
                    .antMatchers(privilege.getUrl())
                    .hasAuthority("ROLE_" + privilege.getCode());
        }

        http.authorizeRequests().antMatchers("/css/**", "/js/**", "/image/**").permitAll();

        // Các trang không yêu cầu login
        http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();

        // Khi người dùng đã login, với vai trò XX.
        // Nhưng truy cập vào trang yêu cầu vai trò YY,
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.exceptionHandling().accessDeniedPage("/403");

        // Cấu hình cho Login form.
        http.formLogin()//
                .loginPage("/login")//
                .defaultSuccessUrl("/welcome", true);

        // Cấu hình cho Logout Page.
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();

        // Cấu hình Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

        // Tất cả các url chưa được config phải yêu cầu đăng nhập
        http.authorizeRequests().anyRequest().authenticated();
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
