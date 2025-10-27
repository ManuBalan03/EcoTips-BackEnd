package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPublicacion;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT", name = "contenido_key")
    private String contenido_key;

    @Column(nullable = false)
    private String estado; // Ej: "PENDIENTE", "APROBADA", "RECHAZADA"

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    private Long idUsuario; // Este lo extraeremos del token


    @Builder.Default
    @Column(nullable = false)
    private Integer puntos = 0;
}
