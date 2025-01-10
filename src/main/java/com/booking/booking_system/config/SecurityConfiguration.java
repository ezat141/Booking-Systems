package com.booking.booking_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public access for authentication endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll() // Allow public access for auth endpoints
                        // Public access to view schedules by service ID and date
                        .requestMatchers("/api/v1/schedules/{serviceId}/{date}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/services").authenticated() // Allow authenticated users to view services
                        .requestMatchers(HttpMethod.GET, "/api/v1/services/{serviceId}/schedules").authenticated() // Allow authenticated users to view available time slots
                        // Restrict all other schedule operations to admins
                        .requestMatchers("/api/v1/schedules/**").hasAuthority("ADMIN")
                        // Restrict service modifications to admins
                        .requestMatchers(HttpMethod.POST, "/api/v1/services").hasAuthority("ADMIN") // Only admins can add services
                        .requestMatchers(HttpMethod.PUT, "/api/v1/services").hasAuthority("ADMIN") // Only admins can edit services
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/services/**").hasAuthority("ADMIN") // Only admins can delete services
//                        // Restrict all /api/bookings endpoints to authenticate users
                        .requestMatchers("/api/v1/bookings/**").authenticated()
                        // Only ADMIN can confirm bookings
                        .requestMatchers(HttpMethod.PUT, "/api/v1/bookings/*/confirm").hasAuthority("ADMIN") // The pattern "/api/v1/bookings/*/confirm" matches paths like /api/v1/bookings/1/confirm, where 1 is the booking ID.

                        // Any other requests must be authenticated
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

