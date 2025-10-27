package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ReactionsDTO {
    @JsonProperty("Tipo")
    private String Tipo;

    @JsonProperty("idReaccion")
    private Long idReaccion;

    @JsonProperty("idUsuario")
    private Long idUsuario;

    @JsonProperty("idPublicacion")
    private Long idPublicacion;

    @JsonProperty("fechaCreacion")
    private LocalDateTime fechaCreacion;
    private String nombreAutor;

    public ReactionsDTO(){}

    public ReactionsDTO(Long idReaccion, long idPublicacion, String Tipo, Long idUsuario, LocalDateTime fechaCreacion) {
        this.idReaccion = idReaccion;
        this.idPublicacion= idPublicacion;
        this.Tipo = Tipo;
        this.idUsuario = idUsuario;
        this.fechaCreacion = fechaCreacion;
    }
}
