package com.example.demo.controllers;


import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.EvaluacionDTO;
import com.example.demo.DTO.PublicacionDTO;
import com.example.demo.Service.ComentariosService.ComentarioService;
import com.example.demo.Service.EvaluacionService.EvaluacionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Evaluacion")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDTO> crearEvaluacion(@RequestBody EvaluacionDTO dto) {
        EvaluacionDTO creado = evaluacionService.crearEvaluacion(dto);
        return ResponseEntity.ok(creado);
    }

    @GetMapping
    public ResponseEntity<Page<PublicacionDTO>> listarTodasEvaluacion(
            @RequestParam(defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
        Page<PublicacionDTO> publicaciones = evaluacionService.listarTodasPaginadas(estado, null, pageable);

        return ResponseEntity.ok(publicaciones);
    }

    // ✅ Nuevo endpoint para evaluaciones de una publicación
    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<List<EvaluacionDTO>> obtenerEvaluacionesPorPublicacion(
            @PathVariable Long idPublicacion) {

        List<EvaluacionDTO> evaluaciones = evaluacionService.obtenerEvaluacionesPorPublicacion(idPublicacion);
        return ResponseEntity.ok(evaluaciones);
    }


    @GetMapping("/publicacion/evaluate/{idPublicacion}")
    public ResponseEntity<Optional<EvaluacionDTO>> obtenerEvaluacionPorPublicacion(
            @PathVariable Long idPublicacion) {

        Optional<EvaluacionDTO> evaluaciones = evaluacionService.obtenerEvaluacionMasRecientePorPublicacion(idPublicacion);
        return ResponseEntity.ok(evaluaciones);
    }


    // ✅ Mantener compatibilidad con endpoint antiguo
    @GetMapping("/todas")
    public ResponseEntity<List<PublicacionDTO>> listarTodasEvaluacionSinPaginacion(
            @RequestParam(defaultValue = "PENDIENTE") String estado) {

        List<PublicacionDTO> publicaciones = evaluacionService.listarTodas(estado, null);
        return ResponseEntity.ok(publicaciones);
    }
}
