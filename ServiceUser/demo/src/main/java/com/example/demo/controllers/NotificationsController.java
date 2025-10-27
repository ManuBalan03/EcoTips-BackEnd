package com.example.demo.controllers;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.Service.NotificationsService.NotificacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificationsController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificationsDTO> nuevaNotificacion(@RequestBody NotificationsDTO dto) {
            System.out.println("RECIBIDO: " + dto); // Para depurar
            notificacionService.crear(dto); // lógica para guardar o procesar la notificación
            return ResponseEntity.ok().build();
        }


//    @GetMapping("/{id}")
//    public ResponseEntity<List<NotificationsDTO>> obtenerNotificacionsPorUsuario(@PathVariable Long id) {
//        List<NotificationsDTO> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(id);
//        return ResponseEntity.ok(notificaciones);
//    }
//
//
//    @PatchMapping("/{idNotificacion}/leida")
//    public ResponseEntity<Void> marcarComoLeida(
//            @PathVariable Long idNotificacion) {
//
//        notificacionService.marcarComoLeida(idNotificacion);
//        return ResponseEntity.ok().build();
//    }

    // ✅ Obtener notificaciones paginadas
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<NotificationsDTO>> obtenerNotificacionesPaginadas(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaEnvio") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        Page<NotificationsDTO> notificaciones = notificacionService.obtenerNotificacionesPorUsuarioPaginadas(idUsuario, pageable);

        return ResponseEntity.ok(notificaciones);
    }

    // ✅ Obtener solo no leídas paginadas
    @GetMapping("/usuario/{idUsuario}/no-leidas")
    public ResponseEntity<Page<NotificationsDTO>> obtenerNoLeidasPaginadas(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaEnvio").descending());
        Page<NotificationsDTO> notificaciones = notificacionService.obtenerNoLeidasPorUsuarioPaginadas(idUsuario, pageable);

        return ResponseEntity.ok(notificaciones);
    }




    // ✅ Contador de notificaciones no leídas (para badge)
    @GetMapping("/usuario/{idUsuario}/contador-no-leidas")
    public ResponseEntity<Map<String, Long>> contarNoLeidas(@PathVariable Long idUsuario) {
        long count = notificacionService.contarNotificacionesNoLeidas(idUsuario);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

    // ✅ Marcar como leída
    @PatchMapping("/{idNotificacion}/leida")
    public ResponseEntity<Void> marcarComoLeida(@PathVariable Long idNotificacion) {
        notificacionService.marcarComoLeida(idNotificacion);
        return ResponseEntity.ok().build();
    }

    // ✅ Marcar todas como leídas
    @PatchMapping("/usuario/{idUsuario}/leer-todas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@PathVariable Long idUsuario) {
        notificacionService.marcarTodasComoLeidas(idUsuario);
        return ResponseEntity.ok().build();
    }

    // ✅ Endpoint para scroll infinito
    @GetMapping("/usuario/{idUsuario}/anteriores")
    public ResponseEntity<Page<NotificationsDTO>> obtenerNotificacionesAntiguas(
            @PathVariable Long idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime antesDe,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationsDTO> notificaciones = notificacionService.obtenerNotificacionesAntiguas(idUsuario, antesDe, pageable);

        return ResponseEntity.ok(notificaciones);
    }



}
