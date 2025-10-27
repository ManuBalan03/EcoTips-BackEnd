package com.example.demo.Service.NotificationsService;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.Repository.NotificationRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.models.NotificacionesModel;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificacionService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationsDTO crear(NotificationsDTO dto) {

        // Buscar el usuario por ID
        UserModel usuario = userRepository.findById(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + dto.getUsuario()));

        NotificacionesModel notification = NotificacionesModel.builder()
                .tipo(dto.getTipo())
                .mensaje(dto.getMensaje())
                .fechaEnvio(dto.getFechaEnvio())
                .leido(false)
                .idPublicacion(dto.getIdPublicacion())
                .usuario(usuario)
                .build();

        NotificacionesModel guardada = notificationRepository.save(notification);

        return new NotificationsDTO(
                guardada.getIdNotificacion(),
                guardada.getTipo(),
                guardada.getMensaje(),
                guardada.getFechaEnvio(),
                guardada.getUsuario().getIdUsuario(), // ← para regresar el ID
                guardada.isLeido(),
                guardada.getIdPublicacion()
        );
    }

//    public List<NotificationsDTO> obtenerNotificacionesPorUsuario(Long idUsuario) {
//        if (!userRepository.existsById(idUsuario)) {
//            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
//        }
//
//        List<NotificacionesModel> notificaciones = notificationRepository.findByUsuarioIdUsuario(idUsuario);
//
//        // Convertir a DTOs
//        return notificaciones.stream()
//                .map(n -> new NotificationsDTO(
//                        n.getIdNotificacion(),
//                        n.getTipo(),
//                        n.getMensaje(),
//                        n.getFechaEnvio(),
//                        n.getUsuario().getIdUsuario(),
//                        n.isLeido(),
//                        n.getIdPublicacion()
//                ))
//                .collect(Collectors.toList());
//    }
//
//    public void marcarComoLeida(Long idNotificacion) {
//        NotificacionesModel notificacion = notificationRepository.findById(idNotificacion)
//                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
//
//        notificacion.setLeido(true);
//        notificationRepository.save(notificacion);
//    }




    // ✅ NUEVO: Método paginado para obtener notificaciones
    @Transactional(readOnly = true)
    public Page<NotificationsDTO> obtenerNotificacionesPorUsuarioPaginadas(Long idUsuario, Pageable pageable) {
        if (!userRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        Page<NotificacionesModel> notificacionesPage = notificationRepository.findByUsuarioIdUsuario(idUsuario, pageable);

        return notificacionesPage.map(this::convertToDTO);
    }

    // ✅ NUEVO: Notificaciones no leídas paginadas
    @Transactional(readOnly = true)
    public Page<NotificationsDTO> obtenerNoLeidasPorUsuarioPaginadas(Long idUsuario, Pageable pageable) {
        if (!userRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        Page<NotificacionesModel> notificacionesPage = notificationRepository.findByUsuarioIdUsuarioAndLeidoFalse(idUsuario, pageable);

        return notificacionesPage.map(this::convertToDTO);
    }

    // ✅ NUEVO: Contador de notificaciones no leídas
    @Transactional(readOnly = true)
    public long contarNotificacionesNoLeidas(Long idUsuario) {
        return notificationRepository.countByUsuarioIdUsuarioAndLeidoFalse(idUsuario);
    }

    // ✅ Mantener método para marcar como leída
    public void marcarComoLeida(Long idNotificacion) {
        NotificacionesModel notificacion = notificationRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeido(true);
        notificationRepository.save(notificacion);
    }

    // ✅ NUEVO: Marcar todas como leídas
    @Transactional
    public void marcarTodasComoLeidas(Long idUsuario) {
        notificationRepository.marcarTodasComoLeidas(idUsuario);
    }

    // ✅ Método de conversión a DTO
    private NotificationsDTO convertToDTO(NotificacionesModel notificacion) {
        return new NotificationsDTO(
                notificacion.getIdNotificacion(),
                notificacion.getTipo(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio(),
                notificacion.getUsuario().getIdUsuario(),
                notificacion.isLeido(),
                notificacion.getIdPublicacion()
        );
    }

    // ✅ Mantener método antiguo (para compatibilidad)
    @Transactional(readOnly = true)
    public List<NotificationsDTO> obtenerNotificacionesPorUsuario(Long idUsuario) {
        if (!userRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        List<NotificacionesModel> notificaciones = notificationRepository.findByUsuarioIdUsuario(idUsuario);

        return notificaciones.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<NotificationsDTO> obtenerNotificacionesAntiguas(Long idUsuario, LocalDateTime antesDe, Pageable pageable) {
        if (!userRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        Page<NotificacionesModel> notificacionesPage = notificationRepository.findOlderNotifications(idUsuario, antesDe, pageable);

        return notificacionesPage.map(this::convertToDTO);
    }


}
