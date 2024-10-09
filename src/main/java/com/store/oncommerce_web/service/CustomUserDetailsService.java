package com.store.oncommerce_web.service;

import com.store.oncommerce_web.model.UserDTO;
import com.store.oncommerce_web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Repositorio para acceder a la base de datos

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar el usuario por email en la base de datos
        com.store.oncommerce_web.model.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Construir y devolver un objeto UserDetails para la autenticación
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // Contraseña encriptada en la base de datos
                .roles("USER") // Aquí puedes definir roles según tu lógica
                .build();
    }
}