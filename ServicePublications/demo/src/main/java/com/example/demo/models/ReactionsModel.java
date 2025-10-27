package com.example.demo.models;

import com.example.demo.models.Enum.TipoReacciones;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReaciones;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoReacciones Tipo;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicationsModel publicacion;

    private Long idUsuario;
}
