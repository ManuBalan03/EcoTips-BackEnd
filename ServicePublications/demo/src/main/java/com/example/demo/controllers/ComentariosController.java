package com.example.demo.controllers;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.Service.ComentariosService.ComentarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publicaciones/comentarios")
@RequiredArgsConstructor
@Validated
public class ComentariosController {
    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioDTO> crearComentario(@Valid @RequestBody ComentarioDTO dto) {
        ComentarioDTO creado = comentarioService.crearComentario(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentarios(
            @PathVariable Long idPublicacion) {
        return ResponseEntity.ok(comentarioService.listarComentariosPorPublicacion(idPublicacion));
    }

    // ✅ Nuevo endpoint paginado
    @GetMapping("/publicacion/{idPublicacion}/paginados")
    public ResponseEntity<Page<ComentarioDTO>> obtenerComentariosPaginados(
            @PathVariable Long idPublicacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<ComentarioDTO> comentarios = comentarioService.listarComentariosPorPublicacionPaginado(idPublicacion, (SpringDataWebProperties.Pageable) pageable);

        return ResponseEntity.ok(comentarios);
    }

    // ✅ Nuevo endpoint para estadísticas
    @GetMapping("/publicacion/{idPublicacion}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasComentarios(
            @PathVariable Long idPublicacion) {

        Map<String, Object> estadisticas = comentarioService.obtenerEstadisticasComentarios(idPublicacion);
        return ResponseEntity.ok(estadisticas);
    }

    // ✅ Nuevo endpoint para comentarios de usuario
//    @GetMapping("/usuario/{idUsuario}")
//    public ResponseEntity<Page<ComentarioDTO>> obtenerComentariosPorUsuario(
//            @PathVariable Long idUsuario,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
//        Page<ComentarioDTO> comentarios = comentarioService.listarComentariosPorUsuario(idUsuario, pageable);
//
//        return ResponseEntity.ok(comentarios);
//    }
}
