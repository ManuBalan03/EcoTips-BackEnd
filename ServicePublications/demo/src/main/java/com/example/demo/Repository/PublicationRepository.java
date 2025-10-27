package com.example.demo.Repository;
import com.example.demo.models.PublicationsModel;
import com.example.demo.models.VotosModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;
import java.util.Set;

public interface PublicationRepository extends JpaRepository<PublicationsModel, Long> {

    // ✅ Nuevas consultas para usuario + estado
    @Query("SELECT p FROM PublicationsModel p WHERE p.idUsuario = :idUsuario AND p.estado = :estado ORDER BY p.fechaCreacion DESC")
    Page<PublicationsModel> findByUsuarioAndEstadoPaginado(@Param("idUsuario") Long idUsuario,
                                                           @Param("estado") String estado,
                                                           Pageable pageable);

    @Query("SELECT p FROM PublicationsModel p WHERE p.idUsuario = :idUsuario AND p.estado = :estado ORDER BY p.fechaCreacion DESC")
    List<PublicationsModel> findByUsuarioAndEstado(@Param("idUsuario") Long idUsuario,
                                                   @Param("estado") String estado);

    // ✅ Consultas para estadísticas
    @Query("SELECT COUNT(p) FROM PublicationsModel p WHERE p.idUsuario = :idUsuario")
    Long countByUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM PublicationsModel p WHERE p.idUsuario = :idUsuario AND p.estado = 'APROBADA'")
    Long countAprobadasByUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM PublicationsModel p WHERE p.idUsuario = :idUsuario AND p.estado = 'PENDIENTE'")
    Long countPendientesByUsuario(@Param("idUsuario") Long idUsuario);

    @Query("SELECT COUNT(p) FROM PublicationsModel p WHERE p.idUsuario = :idUsuario AND p.estado = 'RECHAZADA'")
    Long countRechazadasByUsuario(@Param("idUsuario") Long idUsuario);


    @Query("SELECT p.estado FROM PublicationsModel p WHERE p.id = :idPublicacion")
    String findEstadoById(@Param("idPublicacion") Long idPublicacion);







    // ✅ Consulta corregida: usar VotosModel en lugar de VoteModel
    @Query("SELECT p FROM PublicationsModel p WHERE p.estado = :estado AND " +
            "(SELECT COUNT(v) FROM VotosModel v WHERE v.publicacion.idPublicacion = p.idPublicacion) >= :minVotos " +
            "ORDER BY p.fechaCreacion DESC")
    List<PublicationsModel> findByEstadoWithMinVotes(@Param("estado") String estado, @Param("minVotos") int minVotos);

    @Query("SELECT p FROM PublicationsModel p WHERE p.estado = :estado AND " +
            "(SELECT COUNT(v) FROM VotosModel v WHERE v.publicacion.idPublicacion = p.idPublicacion) >= :minVotos " +
            "ORDER BY p.fechaCreacion DESC")
    Page<PublicationsModel> findByEstadoWithMinVotesPaginado(@Param("estado") String estado,
                                                             @Param("minVotos") int minVotos,
                                                             Pageable pageable);

    @Query("SELECT p.idUsuario FROM PublicationsModel p WHERE p.idPublicacion = :publicacionId")
    Long findUserIdByPublicationId(@Param("publicacionId") Long publicacionId);

    // ✅ Métodos existentes que deben mantenerse
    List<PublicationsModel> findByEstadoOrderByFechaCreacionDesc(String estado);
    List<PublicationsModel> findByIdUsuario(Long idUsuario);

    @Query("SELECT p FROM PublicationsModel p WHERE p.estado = :estado ORDER BY p.fechaCreacion DESC")
    Page<PublicationsModel> findByEstadoPaginado(@Param("estado") String estado, Pageable pageable);

    @Query("SELECT p FROM PublicationsModel p WHERE p.idUsuario = :idUsuario ORDER BY p.fechaCreacion DESC")
    Page<PublicationsModel> findByIdUsuarioPaginado(@Param("idUsuario") Long idUsuario, Pageable pageable);

}

