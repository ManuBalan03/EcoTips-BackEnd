package com.example.demo.Service.VotosService;

import com.example.demo.DTO.VotosDTO;

import java.util.List;

public interface VotesService {
    VotosDTO crearVoto(VotosDTO dto);
    List<VotosDTO> listarVotosPorPublicacion(Long idPublicacion);
    VotosDTO listarVotoPorPublicacion(Long idPublicacion);
}
