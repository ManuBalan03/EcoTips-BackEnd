package com.example.demo.Service.ReaccionService;

import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.ReactionsDTO;
import com.example.demo.Repository.ComentariosRepository;
import com.example.demo.Repository.PublicationRepository;
import com.example.demo.Repository.ReactionsRepository;
import com.example.demo.Service.NotificationsService;
import com.example.demo.Service.PublicacionService.PublicationsService;
import com.example.demo.Service.UsuarioService;
import com.example.demo.models.Enum.TipoReacciones;
import com.example.demo.models.PublicationsModel;
import com.example.demo.models.ReactionsModel;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionService implements  ReactionsService{

    private final NotificationsService notificationsService;
    private final ReactionsRepository  ReactionRepository;
    private final PublicationRepository publicationsRepository;
    private final UsuarioService usuarioService;

    public ReactionsDTO crearReaccion(ReactionsDTO dto) {
        // Validación del tipo de reacción
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de reacción no puede ser nulo o vacío");
        }

        try {
            TipoReacciones tipo = TipoReacciones.fromString(dto.getTipo());

            PublicationsModel publicacion = publicationsRepository.findById(dto.getIdPublicacion())
                    .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

            Optional<ReactionsModel> existente = ReactionRepository
                    .findByPublicacionIdPublicacionAndIdUsuario(dto.getIdPublicacion(), dto.getIdUsuario());

            ReactionsModel reaccion;

            if (existente.isPresent()) {
                reaccion = existente.get();
                reaccion.setTipo(tipo);
            } else {
                reaccion = ReactionsModel.builder()
                        .Tipo(tipo)
                        .publicacion(publicacion)
                        .idUsuario(dto.getIdUsuario())
                        .build();
            }

            reaccion.setFechaCreacion(LocalDateTime.now());
            ReactionsModel reaccionGuardada = ReactionRepository.save(reaccion);

            String [] Datos = usuarioService.obtenerNombrePorId(reaccion.getIdUsuario());//obtiene datos del usuario nombre y sun foto de perfil

            Long id= publicationsRepository.findUserIdByPublicationId(dto.getIdPublicacion());
            notificationsService.enviarNotificacion(
                    new NotificationsDTO(
                            "Reaccion",
                            Datos[0]+" Reacciono a tu Publicacion",
                            LocalDateTime.now(),
                            id,
                            dto.getIdPublicacion()
                    )
            );

            return new ReactionsDTO(
                    reaccionGuardada.getIdReaciones(),
                    reaccionGuardada.getPublicacion().getIdPublicacion(),
                    reaccionGuardada.getTipo().getValue(),
                    reaccionGuardada.getIdUsuario(),
                    reaccionGuardada.getFechaCreacion()
            );

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de reacción inválido: " + dto.getTipo() +
                    ". Tipos válidos: " + Arrays.toString(TipoReacciones.values()));
        }
    }


    public List<ReactionsDTO> listarReaccionesPorPublicacion(Long idPublicacion) {
        List<ReactionsModel> Reacciones = ReactionRepository.findByPublicacionIdPublicacion(idPublicacion);
        return Reacciones.stream().map(Reaccion -> new ReactionsDTO(
                Reaccion.getIdReaciones(),
                Reaccion.getPublicacion().getIdPublicacion(),
                Reaccion.getTipo().name(),
                Reaccion.getIdUsuario(),
                Reaccion.getFechaCreacion()
        )).toList();
    }

    public Map<String, Long> contarReaccionesPorTipo(Long idPublicacion) {
        List<ReactionsModel> reacciones = ReactionRepository.findByPublicacionIdPublicacion(idPublicacion);
        return reacciones.stream()
                .collect(Collectors.groupingBy(r -> r.getTipo().name(), Collectors.counting()));
    }


    public void eliminarReaccion(Long idReaccion) {
        if (!ReactionRepository.existsById(idReaccion)) {
            throw new RuntimeException("Reacción no encontrada con ID: " + idReaccion);
        }
        ReactionRepository.deleteById(idReaccion);
    }

}
