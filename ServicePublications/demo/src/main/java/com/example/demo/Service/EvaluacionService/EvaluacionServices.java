package com.example.demo.Service.EvaluacionService;

import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Repository.EvaluacionRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.VoteRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.PublicacionService.PublicationService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.Enum.TipoVeredicto;
import com.example.demo.models.EvaluacionModel;
import com.example.demo.models.PublicationsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluacionServices implements EvaluacionService {
    private final PublicationRepository publicationsRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final UsuarioService usuarioService;
    private final NotificationsService notificationsService;
    private final PublicationService publicationService;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public EvaluacionDTO crearEvaluacion(EvaluacionDTO dto) {
        // ✅ Validación temprana
        if (dto.getIdpublicacion() == null) {
            throw new RuntimeException("ID de publicación es requerido");
        }

        // ✅ Obtener publicación con validación
        PublicationsModel publicacion = publicationsRepository.findById(dto.getIdpublicacion())
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada con ID: " + dto.getIdpublicacion()));

        // ✅ Convertir veredicto
        TipoVeredicto tipo = TipoVeredicto.fromString(dto.getVeredicto());

        // ✅ Guardar evaluación
        EvaluacionModel evaluacion = EvaluacionModel.builder()
                .veredicto(tipo)
                .comentario_final(dto.getComentario_final())
                .fecha_evaluacion(LocalDateTime.now())
                .publicacion(publicacion)
                .idUsuario(dto.getIdUsuario())
                .build();

        EvaluacionModel evaluacionGuardada = evaluacionRepository.save(evaluacion);

        // ✅ Procesamiento asíncrono para mejor performance
        procesarEvaluacionAsync(evaluacionGuardada, dto.getVeredicto());

        // ✅ Obtener datos del usuario evaluador
        String[] datosUsuario = usuarioService.obtenerNombrePorId(evaluacionGuardada.getIdUsuario());

        return EvaluacionDTO.builder()
                .idEvaluacion(evaluacionGuardada.getIdEvaluacion())
                .veredicto(dto.getVeredicto())
                .comentario_final(dto.getComentario_final())
                .fecha_evaluacion(evaluacionGuardada.getFecha_evaluacion())
                .idpublicacion(dto.getIdpublicacion())
                .idUsuario(dto.getIdUsuario())
                .nombreAutor(datosUsuario[0])
                .fotoPerfil(datosUsuario[1])
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicacionDTO> listarTodas(String estado, Long idUsuarioActual) {
        // ✅ Consulta optimizada: obtener publicaciones con mínimo de votos directamente desde BD
        List<PublicationsModel> publicaciones = publicationsRepository.findByEstadoWithMinVotes(estado, 3);

        // ✅ Batch processing para datos de usuario
        Map<Long, String[]> datosUsuarios = obtenerDatosUsuariosBatch(
                publicaciones.stream()
                        .map(PublicationsModel::getIdUsuario)
                        .distinct()
                        .collect(Collectors.toList())
        );

        // ✅ Mapeo eficiente
        return publicaciones.stream()
                .map(pub -> {
                    PublicacionDTO dto = publicationService.mapToDTO(pub);
                    String[] datos = datosUsuarios.get(pub.getIdUsuario());
                    if (datos != null) {
                        dto.setNombreAutor(datos[0]);
                        dto.setFotoPerfil(datos[1]);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ✅ Métodos privados optimizados
    @Async
    protected void procesarEvaluacionAsync(EvaluacionModel evaluacion, String veredicto) {
        try {
            // ✅ Obtener ID del autor de la publicación
            Long autorId = publicationsRepository.findUserIdByPublicationId(evaluacion.getPublicacion().getIdPublicacion());

            // ✅ Enviar notificación
            String tipoNotificacion = "MODIFICACION".equals(veredicto)
                    ? "Publicacion Modificaciones"
                    : "Publicacion " + capitalizar(veredicto);


            String mensaje = "MODIFICACION".equals(veredicto) ?
                    "Tu publicación requiere Modificaciones" :
                    "Tu publicación fue " + veredicto;

            notificationsService.enviarNotificacion(
                    new NotificationsDTO(
                            tipoNotificacion,
                            mensaje,
                            LocalDateTime.now(),
                            autorId,
                            evaluacion.getPublicacion().getIdPublicacion()
                    )
            );

            // ✅ Actualizar estado de la publicación
            publicationService.actualizarEstadoPublicacion(
                    evaluacion.getPublicacion().getIdPublicacion(),
                    veredicto
            );

        } catch (Exception e) {
            // ✅ Log del error sin afectar la operación principal
            System.err.println("Error en procesamiento asíncrono: " + e.getMessage());
        }
    }

    private String capitalizar(String palabra) {
        return palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
    }


    private Map<Long, String[]> obtenerDatosUsuariosBatch(List<Long> idsUsuarios) {
        Map<Long, String[]> resultados = new HashMap<>();
        for (Long id : idsUsuarios) {
            try {
                resultados.put(id, usuarioService.obtenerNombrePorId(id));
            } catch (Exception e) {
                resultados.put(id, new String[]{"Usuario", "default_avatar.jpg"});
            }
        }
        return resultados;
    }

    // ✅ Métodos adicionales para mejor funcionalidad
    @Override
    @Transactional(readOnly = true)
    public Page<PublicacionDTO> listarTodasPaginadas(String estado, Long idUsuarioActual, Pageable pageable) {
        Page<PublicationsModel> publicacionesPage = publicationsRepository.findByEstadoWithMinVotesPaginado(estado, 3, pageable);

        // ✅ Batch processing para datos de usuario
        Map<Long, String[]> datosUsuarios = obtenerDatosUsuariosBatch(
                publicacionesPage.getContent().stream()
                        .map(PublicationsModel::getIdUsuario)
                        .distinct()
                        .collect(Collectors.toList())
        );

        return publicacionesPage.map(pub -> {
            PublicacionDTO dto = publicationService.mapToDTO(pub);
            String[] datos = datosUsuarios.get(pub.getIdUsuario());
            if (datos != null) {
                dto.setNombreAutor(datos[0]);
                dto.setFotoPerfil(datos[1]);
            }
            return dto;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> obtenerEvaluacionesPorPublicacion(Long idPublicacion) {
        return evaluacionRepository.findByPublicacionIdPublicacion(idPublicacion).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private EvaluacionDTO mapToDTO(EvaluacionModel evaluacion) {
        String[] datosUsuario = usuarioService.obtenerNombrePorId(evaluacion.getIdUsuario());
        return EvaluacionDTO.builder()
                .idEvaluacion(evaluacion.getIdEvaluacion())
                .veredicto(evaluacion.getVeredicto().toString())
                .comentario_final(evaluacion.getComentario_final())
                .fecha_evaluacion(evaluacion.getFecha_evaluacion())
                .idpublicacion(evaluacion.getPublicacion().getIdPublicacion())
                .idUsuario(evaluacion.getIdUsuario())
                .nombreAutor(datosUsuario[0])
                .fotoPerfil(datosUsuario[1])
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<EvaluacionDTO> obtenerEvaluacionMasRecientePorPublicacion(Long idPublicacion) {
        Pageable pageable = PageRequest.of(0, 1); // Primera página, 1 resultado
        Page<EvaluacionModel> page = evaluacionRepository.findByPublicacionIdPublicacionPaginado(idPublicacion, pageable);

        return page.getContent().stream()
                .findFirst()
                .map(this::mapToDTO);
    }
}
