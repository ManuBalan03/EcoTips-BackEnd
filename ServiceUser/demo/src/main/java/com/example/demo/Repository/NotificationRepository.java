package com.example.demo.Repository;

import com.example.demo.models.NotificacionesModel;
import com.example.demo.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificacionesModel, Long> {

//    List<NotificacionesModel> findByUsuarioIdUsuario(Long idUsuario);


    // ✅ Método existente (mantener para compatibilidad)
    List<NotificacionesModel> findByUsuarioIdUsuario(Long idUsuario);

    // ✅ NUEVO: Método paginado
    Page<NotificacionesModel> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    // ✅ NUEVO: Notificaciones no leídas paginadas
    Page<NotificacionesModel> findByUsuarioIdUsuarioAndLeidoFalse(Long idUsuario, Pageable pageable);

    // ✅ NUEVO: Contador de no leídas
    long countByUsuarioIdUsuarioAndLeidoFalse(Long idUsuario);

    // ✅ NUEVO: Notificaciones recientes no leídas (para badge)
    @Query("SELECT n FROM NotificacionesModel n WHERE n.usuario.idUsuario = :idUsuario AND n.leido = false ORDER BY n.fechaEnvio DESC")
    List<NotificacionesModel> findRecentUnread(@Param("idUsuario") Long idUsuario, Pageable pageable);

    // ✅ NUEVO: Marcar todas como leídas
    @Modifying
    @Query("UPDATE NotificacionesModel n SET n.leido = true WHERE n.usuario.idUsuario = :idUsuario AND n.leido = false")
    void marcarTodasComoLeidas(@Param("idUsuario") Long idUsuario);

    // ✅ NUEVO: Para scroll infinito - notificaciones más antiguas que una fecha
    @Query("SELECT n FROM NotificacionesModel n WHERE n.usuario.idUsuario = :idUsuario AND n.fechaEnvio < :beforeDate ORDER BY n.fechaEnvio DESC")
    Page<NotificacionesModel> findOlderNotifications(@Param("idUsuario") Long idUsuario,
                                                     @Param("beforeDate") LocalDateTime beforeDate,
                                                     Pageable pageable);

}
