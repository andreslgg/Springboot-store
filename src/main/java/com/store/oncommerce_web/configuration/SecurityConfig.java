package com.store.oncommerce_web.configuration;

import com.store.oncommerce_web.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF si es necesario
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login**", "/register", "/static/**", "/js/**", "/images/**", "/qr-codes/**", "/templates/**").permitAll() // Rutas públicas
                        .requestMatchers("/checkout", "/profile", "/orders").authenticated() // Rutas que requieren autenticación
                        .anyRequest().permitAll() // Permitir todas las demás rutas
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login") // Página personalizada de inicio de sesión
                        .permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Página de inicio de sesión para Google OAuth2
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirección después del cierre de sesión
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}