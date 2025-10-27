package com.example.demo.Repository;

import com.example.demo.models.ComentariosModel;
import com.example.demo.models.EvaluacionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluacionRepository extends JpaRepository<EvaluacionModel, Long> {
    List<EvaluacionModel> findByPublicacionIdPublicacion(Long idPublicacion);

    @Query("SELECT e FROM EvaluacionModel e WHERE e.publicacion.idPublicacion = :publicacionId ORDER BY e.fecha_evaluacion DESC")
    Page<EvaluacionModel> findByPublicacionIdPublicacionPaginado(@Param("publicacionId") Long publicacionId,
                                                                 Pageable pageable);

    @Query("SELECT COUNT(e) FROM EvaluacionModel e WHERE e.publicacion.idPublicacion = :publicacionId")
    Long countByPublicacionId(@Param("publicacionId") Long publicacionId);
}
