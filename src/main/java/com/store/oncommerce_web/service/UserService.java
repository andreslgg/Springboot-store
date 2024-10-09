package com.store.oncommerce_web.service;

import com.store.oncommerce_web.model.User;
import com.store.oncommerce_web.model.UserDTO;
import com.store.oncommerce_web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar contraseña
        return userRepository.save(user);
    }

    public User createUserIfNotExist(String email, String firstName, String lastName) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPassword("");

            userRepository.save(newUser);
            return newUser;
        }
        // Retorna el usuario existente
        return optionalUser.get();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);

    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void registerNewUser(UserDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }


    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // Aquí puedes obtener los detalles del usuario desde la base de datos
                // usando userDetails.getUsername() (que es el email)
                // y retornar el objeto User
                return userRepository.findByEmail(userDetails.getUsername())
                        .orElse(null);
            } else if (principal instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) principal;
                String email = oauth2User.getAttribute("email");
                // Similarmente, busca el usuario en la base de datos
                return userRepository.findByEmail(email)
                        .orElse(null);
            }
        }
        return null;
    }
}

