package com.example.demo.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", indexes = {
        @Index(columnList = "email", unique = true, name = "idx_user_email"),
        @Index(columnList = "idusuario", unique = true, name = "idx_user_username"),
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;


    @Column(name = "telefono")
    private String telefono;

    private String contrasenia; // Cifrada

    private String fotoPerfil;

    private String nivel; // 0, 1, 2

    private Integer puntosTotales;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime fechaRegistro;
}
