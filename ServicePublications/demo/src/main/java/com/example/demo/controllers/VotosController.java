package com.example.demo.controllers;

import com.example.demo.DTO.VotosDTO;
import com.example.demo.Service.ReaccionService.ReactionsService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.Service.VotosService.VotesService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publicaciones/votos")
@RequiredArgsConstructor
public class VotosController {

    private final UsuarioService usuarioService;
    private final VotesService VotesService;

    @PostMapping
    public ResponseEntity<VotosDTO> crearVoto(@RequestBody VotosDTO dto) {
            VotosDTO creada = VotesService.crearVoto(dto);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/publicaciones/{id}")
    public ResponseEntity<List<VotosDTO>> obtenerVotosPorPublicacion(@PathVariable Long id) {
        return  ResponseEntity.ok(VotesService.listarVotosPorPublicacion(id));
    }

    @GetMapping("/publicacion/{id}")
    public ResponseEntity<VotosDTO> obtenerVotosPorPublicaciones(@PathVariable Long id) {
        return  ResponseEntity.ok(VotesService.listarVotoPorPublicacion(id));
    }

}
