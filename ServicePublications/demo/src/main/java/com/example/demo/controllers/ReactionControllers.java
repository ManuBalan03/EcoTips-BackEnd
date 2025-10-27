package com.example.demo.controllers;

import com.example.demo.DTO.ComentarioDTO;
import com.example.demo.DTO.ReactionsDTO;
import com.example.demo.Service.ComentariosService.ComentarioService;
import com.example.demo.Service.ReaccionService.ReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publicaciones/reacciones")
@RequiredArgsConstructor
public class ReactionControllers {

    private final ReactionsService reactionsService;

    @PostMapping
    public ResponseEntity<ReactionsDTO> crearReaccion(@RequestBody ReactionsDTO dto) {
        ReactionsDTO creada = reactionsService.crearReaccion(dto);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<List<ReactionsDTO>> obtenerReaccionesPorPublicacion(@PathVariable Long id) {
        return ResponseEntity.ok(reactionsService.listarReaccionesPorPublicacion(id));
    }

    @GetMapping("/conteo/{id}")
    public ResponseEntity<Map<String, Long>> contarReaccionesPorTipo(@PathVariable Long id) {
        return ResponseEntity.ok(reactionsService.contarReaccionesPorTipo(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReaccion(@PathVariable Long id) {
        reactionsService.eliminarReaccion(id);
        return ResponseEntity.noContent().build();
    }
}
