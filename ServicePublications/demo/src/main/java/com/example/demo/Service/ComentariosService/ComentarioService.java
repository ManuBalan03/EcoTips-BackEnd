package com.example.demo.Service.ComentariosService;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Repository.ComentariosRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.ComentariosModel;
import com.example.demo.models.PublicationsModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class ComentarioService implements ComentariosService {
    private final ComentariosRepository comentariosRepository;
    private final PublicationRepository publicationsRepository;
    private final UsuarioService usuarioService;
    private final NotificationsService notificationsService;
    private final TaskExecutor taskExecutor;

    @Override
    @Transactional
    public ComentarioDTO crearComentario(ComentarioDTO dto) {
        // ✅ Validación temprana
        validateComentarioDTO(dto);

        PublicationsModel publicacion = obtenerPublicacionValidada(dto.getIdPublicacion());

        ComentariosModel comentario = construirComentario(dto, publicacion);
        ComentariosModel comentarioGuardado = comentariosRepository.save(comentario);

        // ✅ Procesamiento asíncrono
        procesarNotificacionAsync(comentarioGuardado, publicacion);

        return mapearADTOConDatosUsuario(comentarioGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComentarioDTO> listarComentariosPorPublicacion(Long idPublicacion) {
        // ✅ Validar que la publicación existe
        if (!publicationsRepository.existsById(idPublicacion)) {
            throw new EntityNotFoundException("Publicación no encontrada con ID: " + idPublicacion);
        }

        List<ComentariosModel> comentarios = comentariosRepository.findByPublicacionIdPublicacion(idPublicacion);

        // ✅ Batch processing para datos de usuario
        return mapearComentariosConDatosUsuario(comentarios);
    }

    // ✅ Nuevo método para paginación
    @Override
    @Transactional(readOnly = true)
    public Page<ComentarioDTO> listarComentariosPorPublicacionPaginado(Long idPublicacion, SpringDataWebProperties.Pageable pageable) {
        if (!publicationsRepository.existsById(idPublicacion)) {
            throw new EntityNotFoundException("Publicación no encontrada con ID: " + idPublicacion);
        }

        Page<ComentariosModel> comentariosPage = comentariosRepository.findByPublicacionIdPublicacion(idPublicacion, (Pageable) pageable);

        return comentariosPage.map(this::mapearADTOConDatosUsuario);
    }

    // ✅ Nuevo método para estadísticas
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticasComentarios(Long idPublicacion) {
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalComentarios", comentariosRepository.countByPublicacionIdPublicacion(idPublicacion));
        estadisticas.put("ultimoComentario", comentariosRepository.findTopByPublicacionIdPublicacionOrderByFechaCreacionDesc(idPublicacion)
                .map(this::mapearADTOConDatosUsuario)
                .orElse(null));
        return estadisticas;
    }

    // ✅ Métodos privados optimizados
    private void validateComentarioDTO(ComentarioDTO dto) {
        if (dto.getIdPublicacion() == null) {
            throw new IllegalArgumentException("ID de publicación es requerido");
        }
        if (dto.getContenido() == null || dto.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("Contenido del comentario es requerido");
        }
        if (dto.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID de usuario es requerido");
        }
    }

    private PublicationsModel obtenerPublicacionValidada(Long idPublicacion) {
        return publicationsRepository.findById(idPublicacion)
                .orElseThrow(() -> new EntityNotFoundException("Publicación no encontrada con ID: " + idPublicacion));
    }

    private ComentariosModel construirComentario(ComentarioDTO dto, PublicationsModel publicacion) {
        return ComentariosModel.builder()
                .contenido(dto.getContenido().trim())
                .fechaCreacion(LocalDateTime.now())
                .publicacion(publicacion)
                .idUsuario(dto.getIdUsuario())
                .build();
    }

    @Async
    public void procesarNotificacionAsync(ComentariosModel comentario, PublicationsModel publicacion) {
        taskExecutor.execute(() -> {
            try {
                String[] datosUsuario = usuarioService.obtenerNombrePorId(comentario.getIdUsuario());

                notificationsService.enviarNotificacion(
                        new NotificationsDTO(
                                "Comentario",
                                datosUsuario[0] + " comentó en tu publicación",
                                LocalDateTime.now(),
                                publicacion.getIdUsuario(),
                                publicacion.getIdPublicacion()
                        )
                );
            } catch (Exception e) {
//                log.error("Error enviando notificación de comentario: {}", e.getMessage(), e);
            }
        });
    }

    private ComentarioDTO mapearADTOConDatosUsuario(ComentariosModel comentario) {
        String[] datosUsuario = usuarioService.obtenerNombrePorId(comentario.getIdUsuario());

        return ComentarioDTO.builder()
                .idcomentario(comentario.getIdcomentario())
                .idPublicacion(comentario.getPublicacion().getIdPublicacion())
                .contenido(comentario.getContenido())
                .idUsuario(comentario.getIdUsuario())
                .fechaCreacion(comentario.getFechaCreacion())
                .nombreAutor(datosUsuario[0])
                .fotoPerfil(datosUsuario[1])
                .build();
    }

    private List<ComentarioDTO> mapearComentariosConDatosUsuario(List<ComentariosModel> comentarios) {
        // ✅ Batch processing para reducir llamadas al servicio
        Map<Long, String[]> datosUsuarios = obtenerDatosUsuariosBatch(
                comentarios.stream()
                        .map(ComentariosModel::getIdUsuario)
                        .distinct()
                        .collect(Collectors.toList())
        );

        return comentarios.stream()
                .map(comentario -> {
                    ComentarioDTO dto = mapearComentarioBasico(comentario);
                    String[] datos = datosUsuarios.get(comentario.getIdUsuario());
                    if (datos != null) {
                        dto.setNombreAutor(datos[0]);
                        dto.setFotoPerfil(datos[1]);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private ComentarioDTO mapearComentarioBasico(ComentariosModel comentario) {
        return ComentarioDTO.builder()
                .idcomentario(comentario.getIdcomentario())
                .idPublicacion(comentario.getPublicacion().getIdPublicacion())
                .contenido(comentario.getContenido())
                .idUsuario(comentario.getIdUsuario())
                .fechaCreacion(comentario.getFechaCreacion())
                .build();
    }

    private Map<Long, String[]> obtenerDatosUsuariosBatch(List<Long> idsUsuarios) {
        Map<Long, String[]> resultados = new HashMap<>();
        for (Long id : idsUsuarios) {
            try {
                resultados.put(id, usuarioService.obtenerNombrePorId(id));
            } catch (Exception e) {
                resultados.put(id, new String[]{"Usuario", "default_avatar.jpg"});
//                log.warn("Error obteniendo datos del usuario {}: {}", id, e.getMessage());
            }
        }
        return resultados;
    }
}