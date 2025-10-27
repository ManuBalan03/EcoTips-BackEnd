package com.example.demo.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicacionDTO {
    private Long id;
    private String Contenido_key;
    private String titulo;
    private Long idUsuario;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;
    private String fotoPerfil;
    private int puntos;
    private String estado;
    private String Url_key;

    public PublicacionDTO(Long id, String estado) {
        this.id = id;
        this.estado = estado;
    }

    public PublicacionDTO() {}

    // Constructor parcial si quieres
    public PublicacionDTO(Long id, String Contenido_key, String titulo, Long idUsuario, String descripcion, LocalDateTime fechaCreacion, int puntos) {
        this.id = id;
        this.Contenido_key = Contenido_key;
        this.titulo = titulo;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.puntos= puntos;
    }

    public PublicacionDTO(String contenido_key, Long id, String titulo, Long idUsuario, String descripcion, LocalDateTime fechaCreacion, int puntos, String Url_key) {
        Contenido_key = contenido_key;
        this.id = id;
        this.titulo = titulo;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.puntos = puntos;
        this.Url_key = Url_key;
    }
}
