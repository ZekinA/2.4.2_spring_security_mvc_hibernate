package com.za.crud_mvc_hibernate.config;


import com.za.crud_mvc_hibernate.config.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final LoginSuccessHandler successHandler; // класс, в котором описана логика перенаправления пользователей по ролям

    public SecurityConfig(UserDetailsService userDetailsService, LoginSuccessHandler successHandler) {//@Qualifier("userServiceImpl")
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder()); // конфигурация для прохождения аутентификации
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/admin/**").hasAuthority("admin")
                .antMatchers("/user/**").hasAnyAuthority("user", "admin")
                .anyRequest().authenticated()
                .and()
                //работаем с формой логина
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler)
                .loginProcessingUrl("/login")
                .usernameParameter("name")
                .passwordParameter("password");
//                .and()
//                .logout().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

