package com.example.demo.controllers;

import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.JWT.JwtUltis;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Service.VotosService.VotesService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publicaciones")
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationsService service;
    private final JwtUltis jwtUtils;
    private final VotesService votesService;

    @PostMapping
    public ResponseEntity<PublicacionDTO> crear(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PublicacionDTO dto) {

        Long idUsuario = jwtUtils.getUserIdFromToken(authHeader.replace("Bearer ", ""));
        dto.setIdUsuario(idUsuario);
        System.out.println(dto.getContenido_key());
        System.out.println(dto.getUrl_key());
        System.out.println("datos");
        return ResponseEntity.ok(service.crear(dto));
    }

    // ✅ Endpoint principal con paginación y filtros
    @GetMapping
    public ResponseEntity<Page<PublicacionDTO>> listarTodas(
            @RequestParam(defaultValue = "APROBADA") String estado,
            @RequestParam(required = false) Long usuarioActual,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            HttpServletRequest request) {

        // Obtener ID del usuario actual si no se proporciona
        if (usuarioActual == null) {
            usuarioActual = obtenerIdUsuarioDeJWT(request);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<PublicacionDTO> publicaciones = service.listarTodasPaginadas(estado, usuarioActual, pageable);

        return ResponseEntity.ok(publicaciones);
    }

    // ✅ Endpoint para compatibilidad (mantiene el comportamiento antiguo)
    @GetMapping("/todas")
    public ResponseEntity<List<PublicacionDTO>> listarTodasSinPaginacion(
            @RequestParam(defaultValue = "APROBADA") String estado,
            HttpServletRequest request) {

        Long usuarioActual = obtenerIdUsuarioDeJWT(request);
        List<PublicacionDTO> publicaciones = service.listarTodas(estado, usuarioActual);

        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> obtenerPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPublicacionPorId(id));
    }

    // ✅ Endpoint paginado para publicaciones pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<Page<PublicacionDTO>> listarPendientesPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        Long idUsuarioActual = obtenerIdUsuarioDeJWT(request);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<PublicacionDTO> publicaciones = service.listarTodasPaginadas("PENDIENTE", idUsuarioActual, pageable);

        return ResponseEntity.ok(publicaciones);
    }

    // ✅ Mantener para compatibilidad
    @GetMapping("/pendiente")
    public ResponseEntity<List<PublicacionDTO>> listarPendientes(HttpServletRequest request) {
        Long idUsuarioActual = obtenerIdUsuarioDeJWT(request);
        List<PublicacionDTO> publicaciones = service.listarTodas("PENDIENTE", idUsuarioActual);

        return ResponseEntity.ok(publicaciones);
    }

    // ✅ Endpoint paginado para publicaciones de usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<Page<PublicacionDTO>> listarPorUsuarioPaginadas(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String estado) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<PublicacionDTO> publicaciones;

        if (estado != null) {
            publicaciones = service.listarPorUsuarioYEstadoPaginado(idUsuario, estado, pageable);
        } else {
            publicaciones = service.listarPorUsuarioPaginado(idUsuario, pageable);
        }

        return ResponseEntity.ok(publicaciones);
    }

    // ✅ Mantener para compatibilidad
    @GetMapping("/usuario/{idUsuario}/todas")
    public ResponseEntity<List<PublicacionDTO>> listarPorUsuario(
            @PathVariable Long idUsuario,
            @RequestParam(required = false) String estado) {

        List<PublicacionDTO> publicaciones;
        if (estado != null) {
            publicaciones = service.listarPorUsuarioYEstado(idUsuario, estado);
        } else {
            publicaciones = service.listarPorUsuario(idUsuario);
        }

        return ResponseEntity.ok(publicaciones);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Nuevo endpoint para actualizar estado
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PublicacionDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {

        PublicacionDTO actualizada = service.actualizarEstadoPublicacion(id, nuevoEstado);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ Nuevo endpoint para actualizar publicación
    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDTO> actualizarPublicacion(
            @PathVariable Long id,
            @RequestBody PublicacionDTO dto) {

        PublicacionDTO actualizada = service.actualizarPublicacion(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // ✅ Nuevo endpoint para estadísticas
    @GetMapping("/usuario/{idUsuario}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(@PathVariable Long idUsuario) {
        Map<String, Object> estadisticas = service.obtenerEstadisticasUsuario(idUsuario);
        return ResponseEntity.ok(estadisticas);
    }

    // ✅ Método optimizado para obtener ID de usuario
    private Long obtenerIdUsuarioDeJWT(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                return jwtUtils.getUserIdFromToken(token);
            } catch (Exception e) {
                // Si falla el JwtUtils, retornar null para que el service maneje publicaciones sin filtro de usuario
                return null;
            }
        }
        return null;
    }

    @GetMapping("/estado/{idPublicacion}/")
    private ResponseEntity<String> obtenerTiporIdPublicacion(@PathVariable long idPublicacion){
        String estado = service.obtenerTipoporIdPublicacion(idPublicacion);
        return  ResponseEntity.ok(estado);
    }


}

