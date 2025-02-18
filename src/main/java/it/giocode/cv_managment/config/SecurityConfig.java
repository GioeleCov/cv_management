package it.giocode.cv_managment.config;

import it.giocode.cv_managment.filter.JwtAuthorizationFilter;
import it.giocode.cv_managment.repository.UserRepository;
import it.giocode.cv_managment.service.impl.CustomUserDetailsService;
import it.giocode.cv_managment.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/cv/create/{candidateId}").hasRole("USER")
                        .requestMatchers("/api/cv/update/{cvId}").hasRole("USER")
                        .requestMatchers("/api/cv/delete/{candidateId}/{cvId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/cv/all").hasRole("ADMIN")
                        .requestMatchers("/api/cv/download/{fileName}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/cv/upload/{candidateId}").hasRole("ADMIN")
                        .requestMatchers("/api/candidate/create/{userId}").hasRole("USER")
                        .requestMatchers("/api/candidate/update/{candidateId}").hasRole("USER")
                        .requestMatchers("/api/candidate/delete/{candidateId}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/candidates").hasRole("ADMIN")
                        .requestMatchers("/api/candidates/experiences").hasRole("ADMIN")
                        .requestMatchers("/api/candidates/skills").hasRole("ADMIN")
                        .requestMatchers("/api/candidates/all").hasRole("ADMIN")
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll())
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
