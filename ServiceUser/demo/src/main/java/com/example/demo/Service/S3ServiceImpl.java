package com.example.demo.Service;

import com.example.demo.DTO.urlPerfilDTO;
import jakarta.validation.Valid;

public interface S3ServiceImpl {
    urlPerfilDTO ObtenerUrlPerfil(@Valid urlPerfilDTO dto, String token);
}
