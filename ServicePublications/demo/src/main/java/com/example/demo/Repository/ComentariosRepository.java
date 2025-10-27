package com.example.demo.Repository;

import com.example.demo.models.ComentariosModel;
import com.example.demo.models.PublicationsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ComentariosRepository extends JpaRepository<ComentariosModel, Long> {
    List<ComentariosModel> findByPublicacionIdPublicacion(Long idPublicacion);

    // ✅ Consulta paginada
    @Query("SELECT c FROM ComentariosModel c WHERE c.publicacion.idPublicacion = :idPublicacion ORDER BY c.fechaCreacion DESC")
    Page<ComentariosModel> findByPublicacionIdPublicacion(@Param("idPublicacion") Long idPublicacion, Pageable pageable);

    // ✅ Contador de comentarios
    @Query("SELECT COUNT(c) FROM ComentariosModel c WHERE c.publicacion.idPublicacion = :idPublicacion")
    Long countByPublicacionIdPublicacion(@Param("idPublicacion") Long idPublicacion);

    // ✅ Último comentario
    @Query("SELECT c FROM ComentariosModel c WHERE c.publicacion.idPublicacion = :idPublicacion ORDER BY c.fechaCreacion DESC")
    Optional<ComentariosModel> findTopByPublicacionIdPublicacionOrderByFechaCreacionDesc(@Param("idPublicacion") Long idPublicacion);

    // ✅ Comentarios recientes de un usuario
    @Query("SELECT c FROM ComentariosModel c WHERE c.idUsuario = :idUsuario ORDER BY c.fechaCreacion DESC")
    Page<ComentariosModel> findByUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);
}
