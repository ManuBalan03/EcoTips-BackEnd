package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {

        private Integer puntosTotales;
        private String nivel;

        // Validación
        public boolean hasPuntos() {
                return puntosTotales != null;
        }

        public boolean hasNivel() {
                return nivel != null && !nivel.trim().isEmpty();
        }

}
