package com.example.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationsDTO {

    private String tipo;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private Long usuario;
    private Long idPublicacion;
}
