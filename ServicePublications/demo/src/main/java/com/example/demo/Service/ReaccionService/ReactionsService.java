package com.example.demo.Service.ReaccionService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.ReactionsDTO;
import com.example.demo.models.ComentariosModel;
import com.example.demo.models.ReactionsModel;

import java.util.List;
import java.util.Map;

public interface ReactionsService {

    ReactionsDTO crearReaccion(ReactionsDTO dto);

    List<ReactionsDTO> listarReaccionesPorPublicacion(Long idPublicacion);

    Map<String, Long> contarReaccionesPorTipo(Long idPublicacion);

    void eliminarReaccion(Long idReaccion);
}
