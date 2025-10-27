package com.example.demo.controllers;


import com.example.demo.DTO.PuntoVerdeDTO;
import com.example.demo.Service.PuntoVerdeService.PuntoVerdeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/PuntoVerde")
@RequiredArgsConstructor
public class PuntoVerdeController {

    @Autowired
     private PuntoVerdeService puntoVerdeService;

    @GetMapping
    public List<PuntoVerdeDTO> getPuntoVerde(){
        return puntoVerdeService.getPuntoVerdeList();
    }

    @PostMapping("insertPV")
     public ResponseEntity<Long> insertPuntoVerde(@RequestBody  PuntoVerdeDTO puntoVerde){
        Long id = puntoVerdeService.createPuntoVerde(puntoVerde);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("{puntoId}")
    public ResponseEntity<Void> deletePuntoVerde(@PathVariable Long puntoId){
        puntoVerdeService.deletePuntoVerde(puntoId);
        return ResponseEntity.noContent().build();
    }
}
