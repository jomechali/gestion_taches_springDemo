package com.diginamic.gt.security;

import com.diginamic.gt.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.POST;

@AllArgsConstructor
@Configuration
@EnableWebSecurity // enable security config, can not be excluded anymore
@EnableGlobalMethodSecurity(prePostEnabled = true) // to let it deal with all requests
public class SecurityConfiguration {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AccountService accountService;

    private JWTTokenUtils jwtTokenUtils;

    private JWTEntryPoint jwtEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // to find the users, need to access DB
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(accountService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }

    // on autorise les bonnes url, les autres url doivent deja avoir leur token

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests(requests ->
                requests
                        //.antMatchers(POST,"task").hasAnyAuthority("MANAGER")
                        .anyRequest().authenticated())
                .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
                .and()
                .httpBasic(Customizer.withDefaults());
        // autoriser les autres url pour un employe qui a un token valide
        JWTFilter jwtFilter = new JWTFilter(accountService, jwtTokenUtils, "authorisation"); // creer la classe filtre
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);// class sur laquelle s applique le filter
        return httpSecurity.build();
    }

    // double security?
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .antMatchers(POST, "/signin")
                .antMatchers(POST, "/signup")
                .antMatchers(POST, "/activate"));
    }
}
