package com.example.demo.Repository;
import com.example.demo.DTO.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.models.UserModel;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long>{
    Optional<UserModel> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM UserModel u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // ✅ CORREGIDO: Usa el paquete correcto DTO (con mayúsculas)
    @Query("SELECT new com.example.demo.DTO.UserDTO(u.idUsuario, u.nombre, u.email, u.telefono, " +
            "u.fotoPerfil, u.nivel, u.puntosTotales) FROM UserModel u WHERE u.idUsuario = :id")
    Optional<UserDTO> findUserDTOById(@Param("id") Long id);

    // ✅ Solo puntos
    @Query("SELECT u.puntosTotales FROM UserModel u WHERE u.idUsuario = :id")
    Optional<Integer> findPuntosTotalesById(@Param("id") Long id);

    // ✅ Actualizaciones directas
    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.puntosTotales = u.puntosTotales + :puntos WHERE u.idUsuario = :id")
    void sumarPuntos(@Param("id") Long id, @Param("puntos") int puntos);

    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.puntosTotales = u.puntosTotales - :puntos WHERE u.idUsuario = :id")
    void restarPuntos(@Param("id") Long id, @Param("puntos") int puntos);

    @Modifying
    @Transactional
    @Query("UPDATE UserModel u SET u.nivel = :nivel WHERE u.idUsuario = :id")
    void actualizarNivel(@Param("id") Long id, @Param("nivel") String nivel);

    // ✅ CORREGIDO: Consulta paginada con paquete correcto
    @Query("SELECT new com.example.demo.DTO.UserDTO(u.idUsuario, u.nombre, u.email, u.telefono, " +
            "u.fotoPerfil, u.nivel, u.puntosTotales) FROM UserModel u")
    Page<UserDTO> findAllUserDTOs(Pageable pageable);
}