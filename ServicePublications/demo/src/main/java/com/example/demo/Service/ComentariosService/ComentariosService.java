package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.models.ComentariosModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ComentariosService {

    ComentarioDTO crearComentario(ComentarioDTO dto);
    Page<ComentarioDTO> listarComentariosPorPublicacionPaginado(Long idPublicacion, SpringDataWebProperties.Pageable pageable);
    List<ComentarioDTO> listarComentariosPorPublicacion(Long idPublicacion);
    Map<String, Object> obtenerEstadisticasComentarios(Long idPublicacion);
    }
