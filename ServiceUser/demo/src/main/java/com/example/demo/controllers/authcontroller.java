package com.example.demo.controllers;

import com.example.demo.DTO.*;
import com.example.demo.JTW.JtwResponse;
import com.example.demo.Service.AuthService;
import com.example.demo.Service.S3Service;
import com.example.demo.Service.S3ServiceImpl;
import com.example.demo.models.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class authcontroller {
    private final S3ServiceImpl s3Service;
    private final AuthService authService; // Servicio que maneja lógica de registro y login

    public authcontroller(AuthService authService , S3ServiceImpl s3Service) {
        this.authService = authService;
        this.s3Service= s3Service;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserDTO userDTO) {
        try {
            return authService.register(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO) {
        String token = authService.login(loginDTO);
        UserModel user = authService.obtenerUsuarioPorEmail(loginDTO.getEmail());

        urlPerfilDTO urlperfil = new urlPerfilDTO(
                user.getFotoPerfil()
        );
        urlPerfilDTO url = s3Service.ObtenerUrlPerfil(urlperfil, token);

         UserDTO userDTO = new UserDTO(
                user.getIdUsuario(),
                user.getNombre(),
                user.getEmail(),
                 user.getTelefono(),
                 url.getUrlkey(),
                user.getNivel(),
                 user.getPuntosTotales()
        );

        return ResponseEntity.ok(new AuthResponse(token, userDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, @RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.login(loginDTO);
            if (token != null) {
                // En un sistema stateless, normalmente no hacemos nada con el token
                // Pero podrías implementar una blacklist si lo necesitas
                return ResponseEntity.ok().body(Map.of(
                        "message", "Logout exitoso",
                        "success", true
                ));
            }
            return ResponseEntity.ok().body(Map.of("message", "Sesión cerrada"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error durante logout"));
        }
    }
}
