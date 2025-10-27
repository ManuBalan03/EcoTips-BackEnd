package com.example.demo.Service.PublicacionService;
import com.example.demo.DTO.PublicacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface PublicationsService {
    PublicacionDTO crear(PublicacionDTO dto);
    List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual);
    Page<PublicacionDTO> listarTodasPaginadas(String estado, Long idUsuarioActual, Pageable pageable);
    Optional<PublicacionDTO> obtenerPorId(Long id);
    void eliminar(Long id);
    PublicacionDTO actualizarPublicacion(Long id, PublicacionDTO dto);
    PublicacionDTO actualizarEstadoPublicacion(Long id, String nuevoEstado);
    PublicacionDTO obtenerPublicacionPorId(Long idPublicacion);
    List<PublicacionDTO> listarPorUsuario(Long idUsuario);
    Page<PublicacionDTO> listarPorUsuarioPaginado(Long idUsuario, Pageable pageable);
    List<PublicacionDTO> listarPorUsuarioYEstado(Long idUsuario, String estado);
    Page<PublicacionDTO> listarPorUsuarioYEstadoPaginado(Long idUsuario, String estado, Pageable pageable);
    Map<String, Object> obtenerEstadisticasUsuario(Long idUsuario);

    @Transactional(readOnly = true)
    String obtenerTipoporIdPublicacion(Long idPublicacion);
}