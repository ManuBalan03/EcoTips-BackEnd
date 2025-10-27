package com.example.demo.Repository;

import com.example.demo.models.ReactionsModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReactionsRepository extends JpaRepository<ReactionsModel, Long> {
    List<ReactionsModel> findAllByOrderByFechaCreacionDesc();
    List<ReactionsModel> findByPublicacionIdPublicacion(Long idPublicacion);
    Optional<ReactionsModel> findByPublicacionIdPublicacionAndIdUsuario(Long idPublicacion, Long IdUsuario);
}
