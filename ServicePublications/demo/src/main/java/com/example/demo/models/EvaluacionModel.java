package com.example.demo.models;


import com.example.demo.models.Enum.TipoVeredicto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Evaluacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvaluacion;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeredicto veredicto;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario_final;


    @Column(nullable = false)
    private LocalDateTime fecha_evaluacion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_publicacion", nullable = false)
    private PublicationsModel publicacion;

    private Long idUsuario;

}
