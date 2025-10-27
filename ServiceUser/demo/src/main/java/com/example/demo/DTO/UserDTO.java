package com.example.demo.DTO;

import lombok.*;

@Data
@NoArgsConstructor

public class UserDTO {
    private Long idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private String fotoPerfil;
    private String nivel;
    private Integer puntosTotales;
    private String urlkey;

    public UserDTO(Long idUsuario, String nombre, String email, String telefono,
                   String fotoPerfil, String nivel, Integer puntosTotales) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fotoPerfil = fotoPerfil;
        this.nivel = nivel;
        this.puntosTotales = puntosTotales;
    }

    public UserDTO(Long idUsuario, String nombre, String email, String telefono, String fotoPerfil, String nivel, Integer puntosTotales, String urlkey) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fotoPerfil = fotoPerfil;
        this.nivel = nivel;
        this.puntosTotales = puntosTotales;
        this.urlkey = urlkey;
    }
}