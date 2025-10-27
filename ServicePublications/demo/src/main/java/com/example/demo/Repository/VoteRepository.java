package com.example.demo.Repository;

import com.example.demo.models.PublicationsModel;
import com.example.demo.models.ReactionsModel;
import com.example.demo.models.VotosModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface VoteRepository  extends JpaRepository<VotosModel, Long> {

    List<VotosModel> findByPublicacionIdPublicacion(Long idPublicacion);

    Optional<VotosModel> findFirstByPublicacionIdPublicacion(Long idPublicacion);
    Optional<VotosModel> findByPublicacionIdPublicacionAndIdUsuario(Long idPublicacion, Long IdUsuario);
//    @Query("SELECT v.publicacion.idPublicacion FROM VotosModel v WHERE v.idUsuario = :idUsuario")
//    List<Long> findIdPublicacionesByIdUsuario(Long idUsuario);
boolean existsByPublicacionIdPublicacionAndIdUsuario(Long idPublicacion, Long idUsuario);

    @Query("SELECT COUNT(v) FROM VotosModel v WHERE v.publicacion.idPublicacion = :idPublicacion")
    long contarVotosPorPublicacion(@Param("idPublicacion") Long idPublicacion);

    // ✅ Consulta optimizada para batch
    @Query("SELECT v.publicacion.idPublicacion FROM VotosModel v WHERE v.idUsuario = :usuarioId AND v.publicacion.idPublicacion IN :publicacionIds")
    Set<Long> findVotedPublicationIds(@Param("usuarioId") Long usuarioId, @Param("publicacionIds") List<Long> publicacionIds);

    @Query("SELECT v FROM VotosModel v WHERE v.idUsuario = :usuarioId AND v.publicacion.estado = 'APROBADA'")
    List<VotosModel> findVotesByUsuario(@Param("usuarioId") Long usuarioId);


}
