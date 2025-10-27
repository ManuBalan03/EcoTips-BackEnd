package com.example.demo.Service;
import com.example.demo.DTO.CreateUserDTO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.JTW.JwtUtils;
import com.example.demo.Repository.UserRepository;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(CreateUserDTO userDTO) {
        // Verificar existencia de manera optimizada
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado");
        }

        try {
            UserModel newUser = UserModel.builder()
                    .nombre(userDTO.getNombre())
                    .email(userDTO.getEmail())
                    .telefono(userDTO.getTelefono())
                    .fechaRegistro(LocalDateTime.now())
                    .contrasenia(passwordEncoder.encode(userDTO.getContraseña()))
                    .nivel("nivel 0")
                    .puntosTotales(0)
                    .build();

            userRepository.save(newUser);

            return ResponseEntity.ok("Usuario registrado exitosamente");

        } catch (DataIntegrityViolationException e) {
            // Manejar caso de concurrencia donde otro hilo registró el mismo email
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado");
        }
    }


    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    public UserModel obtenerUsuarioPorEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }
    public class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String message) {
            super(message);
        }
    }
}

