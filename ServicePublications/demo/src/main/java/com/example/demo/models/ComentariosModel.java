package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComentariosModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcomentario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicationsModel publicacion;

    private Long idUsuario; // Este ID viene del token, no se hace relación con otro modelo
}
