package com.edu.hcmute.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http.cors(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(request -> request
                .requestMatchers( "/home","auth/**", "users/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/positions/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/fields/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/companies/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/salary-ranges/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/majors/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/work-modes/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/experience-ranges/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/locations/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/cv/**").permitAll()
                .anyRequest().authenticated()
        ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

}
