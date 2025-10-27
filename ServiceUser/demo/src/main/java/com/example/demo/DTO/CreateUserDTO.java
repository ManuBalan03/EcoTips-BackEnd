package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es requerido")
    private String email;

    private String telefono;
    private String fotoPerfil;

    @NotBlank(message = "La contraseña es requerida")
    private String contraseña;

    // Campos con valores por defecto
    private String nivel = "nivel 0";
    private Integer puntosTotales = 0;
}