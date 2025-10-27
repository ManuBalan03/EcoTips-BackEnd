package com.example.demo.models;

import com.example.demo.models.Enum.TipoVoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "Votos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVotos;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVoto voto;

    @Column(nullable = false)
    private LocalDateTime fechaVoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicationsModel publicacion;

    private Long idUsuario;
}
