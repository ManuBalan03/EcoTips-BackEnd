package com.example.demo.Service.PuntoVerdeService;


import com.example.demo.DTO.PuntoVerdeDTO;
import com.example.demo.Repository.PuntoVerdeRepository;
import com.example.demo.models.PuntoVerdeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PuntoVerdeService {

    @Autowired
    private PuntoVerdeRepository puntoVerdeRepository;

    public List<PuntoVerdeDTO> getPuntoVerdeList() {
        return puntoVerdeRepository.findAll().stream()
                .map(PuntoVerdeDTO::new)
                .toList();
    }
    public Long createPuntoVerde(PuntoVerdeDTO puntoVerde) {
        PuntoVerdeModel entity = new PuntoVerdeModel(puntoVerde);
        puntoVerdeRepository.save(entity);
        return entity.getId();
    }

    public void deletePuntoVerde(Long id) {
        puntoVerdeRepository.deleteById(id);
    }

}
