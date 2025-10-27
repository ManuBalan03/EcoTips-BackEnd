package com.example.demo.DTO;


import com.example.demo.models.Enums.TipoResiduo;
import com.example.demo.models.PuntoVerdeModel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PuntoVerdeDTO {
    @NotBlank
    private String nombre;
    private String descripcion;
    @NonNull
    private BigDecimal latitud;
    @NonNull
    private BigDecimal longitud;
    @NotBlank
    private String direccion;
    @NotBlank
    private TipoResiduo tipo_residuo;
    private String imagen_url;


    public PuntoVerdeDTO(PuntoVerdeModel punto) {

        this.nombre = punto.getNombre();
        this.descripcion = punto.getDescripcion();
        this.latitud = punto.getLatitud();
        this.longitud = punto.getLongitud();
        this.direccion = punto.getDireccion();
        this.tipo_residuo = punto.getTipo_residuo();
        this.imagen_url = punto.getImagen_url();
    }


}

