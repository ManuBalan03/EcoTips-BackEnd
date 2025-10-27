package com.example.demo.models;


import com.example.demo.DTO.PuntoVerdeDTO;
import com.example.demo.models.Enums.TipoResiduo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "puntos_verdes")

public class PuntoVerdeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String direccion;
    @Enumerated(EnumType.STRING)
    private TipoResiduo tipo_residuo;
    private String imagen_url;


    public PuntoVerdeModel(PuntoVerdeDTO dto) {
        this.nombre = dto.getNombre();
        this.descripcion = dto.getDescripcion();
        this.direccion = dto.getDireccion();
        this.latitud = dto.getLatitud();
        this.longitud = dto.getLongitud();
        this.tipo_residuo = dto.getTipo_residuo();
        this.imagen_url  = dto.getImagen_url();
    }
}
