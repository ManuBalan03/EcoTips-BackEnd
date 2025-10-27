package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class VotosDTO {

    @JsonProperty("idVotos")
    private Long idVotos;

    @JsonProperty("Comentario")
    private String Comentario;

    @JsonProperty("Voto")
    private String voto;

    @JsonProperty("FechaVoto")
    private LocalDateTime fechaVoto;

    @JsonProperty("idUsuario")
    private Long idUsuario;

    @JsonProperty("idPublicacion")
    private Long idPublicacion;


    private String nombreAutor;
    private String fotoPerfil;

    public VotosDTO() {
    }

    public VotosDTO(Long idVotos, String comentario, String voto, LocalDateTime fechaVoto, Long idUsuario, Long idPublicacion) {
        this.idVotos = idVotos;
        Comentario = comentario;
        this.voto = voto;
        this.fechaVoto = fechaVoto;
        this.idUsuario = idUsuario;
        this.idPublicacion = idPublicacion;
    }

    public VotosDTO(Long idVotos, String comentario, String voto, LocalDateTime fechaVoto, Long idUsuario, Long idPublicacion, String nombreAutor, String fotoPerfil) {
        this.idVotos = idVotos;
        Comentario = comentario;
        this.voto = voto;
        this.fechaVoto = fechaVoto;
        this.idUsuario = idUsuario;
        this.idPublicacion = idPublicacion;
        this.nombreAutor = nombreAutor;
        this.fotoPerfil = fotoPerfil;
    }
}
