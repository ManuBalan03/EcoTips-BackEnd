package com.example.demo.DTO;

import com.example.demo.models.UserModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationsDTO {
    private Long idNotificacion;
    private String tipo;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    @JsonProperty("usuario")
    private Long usuario;
    private boolean leido;
    private Long idPublicacion;
}
