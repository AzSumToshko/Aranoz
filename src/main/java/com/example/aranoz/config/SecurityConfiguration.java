package com.example.aranoz.config;

import com.example.aranoz.domain.enums.Role;
import com.example.aranoz.repository.UserRepository;
import com.example.aranoz.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfiguration(UserRepository userRepository, UserRepository userRepository1) {
        this.userRepository = userRepository1;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers(PathRequest.toStaticResources().at(StaticResourceLocation.CSS)).permitAll()
                .requestMatchers(PathRequest.toStaticResources().at(StaticResourceLocation.IMAGES)).permitAll()

            .requestMatchers("/User/editProfile/**", "/User/profile/**" ).hasAnyRole(Role.USER.name(),Role.ADMIN.name())
            .requestMatchers("/Admin/**").hasRole(Role.ADMIN.name())
                .anyRequest()
                .permitAll()

                .and().formLogin()
                    .loginPage("/User/login")
                    .loginProcessingUrl("/User/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                .permitAll()

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)

                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/User/login")

                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .key("remember Me Encryption Key")
                .rememberMeCookieName("rememberMeCookie")
                .tokenValiditySeconds(10000);


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(userRepository);
    }
}
