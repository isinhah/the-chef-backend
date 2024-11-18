package com.api.the_chef_backend.security;

import com.api.the_chef_backend.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private RestaurantService restaurantService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, BasicAuthFilter basicAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/{restaurantId}/orders/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants/{restaurantId}/orders/{orderId}/items/**").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurants/{restaurantId}/orders/**").permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/restaurants/{restaurantId}/categories/**",
                                "/api/v1/restaurants/{restaurantId}/products/**",
                                "/api/v1/restaurants/{restaurantId}/complements/**",
                                "/api/v1/restaurants/{restaurantId}/tables/**"
                        ).authenticated()

                        .requestMatchers(HttpMethod.PUT, "/api/v1/restaurants/{restaurantId}/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/restaurants/{restaurantId}/**").authenticated()

                )
                .addFilterBefore(basicAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> restaurantService.getRestaurantByEmail(username)
                .map(restaurant -> User.builder()
                        .username(restaurant.getEmail())
                        .password(restaurant.getPassword())
                        .roles("ADMIN")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Restaurant não encontrado"));
    }
}
