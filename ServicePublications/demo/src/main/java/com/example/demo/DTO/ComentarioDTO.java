package com.example.demo.DTO;


import com.example.demo.models.PublicationsModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ComentarioDTO {
    private Long idcomentario;
    private Long idPublicacion;
    private String contenido;
    private Long idUsuario;
    private LocalDateTime fechaCreacion;
    private String nombreAutor;
    private String fotoPerfil;


    public ComentarioDTO() {}

    public ComentarioDTO(Long idcomentario, Long idPublicacion, String contenido,
                         Long idUsuario, LocalDateTime fechaCreacion) {
        this(idcomentario, idPublicacion, contenido, idUsuario, fechaCreacion, null, null);
    }

    public ComentarioDTO(Long idcomentario, Long idPublicacion, String contenido,
                         Long idUsuario, LocalDateTime fechaCreacion, String nombreAutor,String fotoPerfil) {
        this.idcomentario = idcomentario;
        this.idPublicacion = idPublicacion;
        this.contenido = contenido;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
        this.nombreAutor = nombreAutor;
        this.fotoPerfil=fotoPerfil;
    }
}
