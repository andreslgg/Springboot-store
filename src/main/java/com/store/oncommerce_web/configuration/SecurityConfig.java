package com.store.oncommerce_web.configuration;

import com.paypal.base.rest.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(String.valueOf(HttpMethod.POST), "/cart/remove").permitAll()
                        .requestMatchers("/", "/login", "/public/**", "/products/**").permitAll() // Rutas públicas
                        .requestMatchers("/cart/**").permitAll() // Permitir el acceso a todas las rutas del carrito
                        .requestMatchers("/checkout").authenticated() // Solo /checkout requiere autenticación
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Página de login personalizada (opcional)
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/") // Redirigir a la página principal después de cerrar sesión
                );

        return http.build();
    }
}