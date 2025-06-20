package org.example.springcrazybarbershop.security.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.springcrazybarbershop.exceptions.handlers.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList(
            "X-Requested-With",
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-CSRF-TOKEN"
        ));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("X-CSRF-TOKEN"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @SneakyThrows
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(requestHandler))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint((request, response, authException) -> {
                            String accept = request.getHeader("Accept");
                            boolean isApiRequest = accept != null && accept.contains("application/json");

                            if (isApiRequest) {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}");
                            } else {
                                response.sendRedirect("/auth/signin");
                            }
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/", "/auth/**").permitAll()
                        .requestMatchers("/forgot-password").permitAll()
                        .requestMatchers("/reset-password").permitAll()
                        .requestMatchers("/auth/forgot-password").permitAll()
                        .requestMatchers("/auth/reset-password").permitAll()
                        .requestMatchers("/api/auth/password/reset").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/haircut-categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/employees/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/timeslots/**").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/appointments").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/appointments/**").authenticated()
                        .requestMatchers("/profile").hasRole("CLIENT")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/clients/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/signin")
                        .loginProcessingUrl("/auth/perform_login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/signin?error=true")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/perform_logout")
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/auth/signin")
                        .permitAll()
                );

        return http.build();
    }
}
