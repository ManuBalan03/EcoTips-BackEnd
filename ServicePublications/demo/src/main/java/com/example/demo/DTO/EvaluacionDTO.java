package com.example.demo.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EvaluacionDTO {

    private Long idEvaluacion;
    private String veredicto;
    private String comentario_final;
    private LocalDateTime fecha_evaluacion;
    private Long idpublicacion;
    private Long idUsuario;
    private String nombreAutor;
    private String fotoPerfil;

    public EvaluacionDTO(){}

    public EvaluacionDTO(
            Long idEvaluacion,
            String veredicto,
            String comentario_final,
            LocalDateTime fecha_evaluacion,
            Long idpublicacion,
            Long idUsuario
    ){
        this.idEvaluacion=idEvaluacion;
        this.veredicto=veredicto;
        this.comentario_final=comentario_final;
        this.fecha_evaluacion=fecha_evaluacion;
        this.idpublicacion=idpublicacion;
        this.idUsuario=idUsuario;
    }

    public EvaluacionDTO(Long idEvaluacion, String veredicto, String comentario_final, LocalDateTime fecha_evaluacion, Long idpublicacion, Long idUsuario, String nombreAutor, String fotoPerfil) {
        this.idEvaluacion = idEvaluacion;
        this.veredicto = veredicto;
        this.comentario_final = comentario_final;
        this.fecha_evaluacion = fecha_evaluacion;
        this.idpublicacion = idpublicacion;
        this.idUsuario = idUsuario;
        this.nombreAutor = nombreAutor;
        this.fotoPerfil = fotoPerfil;
    }
}
