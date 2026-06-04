package com.chanuka.cash_flow.config;

import com.chanuka.cash_flow.security.JwtRequestFilter;
import com.chanuka.cash_flow.service.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    // access to api endpoints
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)                  // Cross Site Request Forgery
                .authorizeHttpRequests(auth -> auth.requestMatchers("/status", "/health", "/register", "/activate", "/login", "/error").permitAll()               // anyone can access these endpoints
                .anyRequest().authenticated())                          // everything else requires authentication
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))            // do not create sessions. each request sends token and validates every time
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    // encode password using BCrypt hashing algorithm
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration  configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));                    // allowed frontend origin(http://localhost:3000)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));            // allowed methods
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));            // allowed headers
        configuration.setAllowCredentials(true);                                                        // allow credentials(cookies, authorization headers)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();                 // create CORS source
        source.registerCorsConfiguration("/**", configuration);                                 // apply rules to every endpoint
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(appUserDetailsService);               // use AppUserDetailsService to find users

        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }
}
