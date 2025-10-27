package com.example.demo.Service;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.urlPerfilDTO;
import com.example.demo.models.UserModel;
import com.example.demo.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final S3ServiceImpl s3Service;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional
    public UserModel crearUsuario(UserModel usuario) {
        return repo.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> obtenerTodosPaginados(Pageable pageable) {
        return repo.findAll(pageable).map(this::mapToDTO);
    }

    // ✅ Mantener para compatibilidad (pero con límite)
    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> obtenerTodos() {
        return repo.findAll(PageRequest.of(0, 100)) // Límite de 100 registros
                .map(this::mapToDTO)
                .getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerPorId(Long id) {
        String token = request.getHeader("Authorization");
        return repo.findUserDTOById(id)
                .map(userDTO -> {
                    // Si tiene imagen, convierte la key en URL
                    if (userDTO.getFotoPerfil() != null && !userDTO.getFotoPerfil().isEmpty()) {
                        urlPerfilDTO urldto= s3Service.ObtenerUrlPerfil(toUrlimg(userDTO.getFotoPerfil()),token);
                        userDTO.setFotoPerfil(urldto.getUrlkey());
                    }
                    return userDTO;
                });
    }

    public urlPerfilDTO toUrlimg(String urlimg){
        urlPerfilDTO url= new urlPerfilDTO(
                urlimg
        );
        return url;
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        repo.deleteById(id);
    }

    @Override
    @Transactional
    public UserDTO sumarPuntosUsuario(Long idUsuario, UpdateUserDTO dto) {
        int puntosASumar = dto.getPuntosTotales();

        // ✅ Actualización directa en BD
        repo.sumarPuntos(idUsuario, puntosASumar);

        // ✅ Obtener usuario actualizado
        UserModel usuario = obtenerUsuarioEntity(idUsuario);
        int nuevosPuntos = usuario.getPuntosTotales();

        // ✅ Verificar niveles
        while (nuevosPuntos >= 100) {
            String nuevoNivel = calcularNivelSuperior(usuario.getNivel());
            repo.actualizarNivel(idUsuario, nuevoNivel);
            repo.restarPuntos(idUsuario, 100);
            nuevosPuntos -= 100;
            usuario.setNivel(nuevoNivel);
        }

        usuario.setPuntosTotales(nuevosPuntos);
        return mapToDTO(usuario);
    }

    @Override
    @Transactional
    public UserDTO actualizarUsuario(Long idUsuario, UserDTO dto) {
        UserModel usuario = obtenerUsuarioEntity(idUsuario);
        boolean needsUpdate = false;

        // ✅ Validar email único
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (repo.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("El email ya está en uso");
            }
            usuario.setEmail(dto.getEmail());
            needsUpdate = true;
        }

        // ✅ Actualizar solo campos modificados
        needsUpdate = updateIfDifferent(dto.getNombre(), usuario.getNombre(), usuario::setNombre) || needsUpdate;
        needsUpdate = updateIfDifferent(dto.getTelefono(), usuario.getTelefono(), usuario::setTelefono) || needsUpdate;
        needsUpdate = updateIfDifferent(dto.getFotoPerfil(), usuario.getFotoPerfil(), usuario::setFotoPerfil) || needsUpdate;
        needsUpdate = updateIfDifferent(dto.getNivel(), usuario.getNivel(), usuario::setNivel) || needsUpdate;
//


        // ✅ Puntos
        if (dto.getPuntosTotales() != null && !dto.getPuntosTotales().equals(usuario.getPuntosTotales())) {
            usuario.setPuntosTotales(dto.getPuntosTotales());
            needsUpdate = true;
        }

        return needsUpdate ? mapToDTO(repo.save(usuario)) : mapToDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer obtenerPuntos(Long id) {
        return repo.findPuntosTotalesById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // ✅ Métodos auxiliares optimizados
    private UserModel obtenerUsuarioEntity(Long idUsuario) {
        return repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
    }

    private String calcularNivelSuperior(String nivelActual) {
        return switch (nivelActual) {
            case "nivel 0" -> "nivel 1";
            case "nivel 1" -> "nivel 2";
            default -> nivelActual;
        };
    }

    private UserDTO mapToDTO(UserModel u) {
        return new UserDTO(
                u.getIdUsuario(),
                u.getNombre(),
                u.getEmail(),
                u.getTelefono(),
                u.getFotoPerfil(),
                u.getNivel(),
                u.getPuntosTotales());
    }

    private boolean updateIfDifferent(String newValue, String currentValue, Consumer<String> setter) {
        if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(currentValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

    // ✅ Métodos adicionales para optimización
    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuario(Long id) {
        return repo.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> obtenerPorEmail(String email) {
        return repo.findByEmail(email).map(this::mapToDTO);
    }

    @Override
    @Transactional
    public UserDTO actualizarParcial(Long id, UpdateUserDTO dto) {
        UserModel usuario = obtenerUsuarioEntity(id);

        if (dto.getPuntosTotales() != null) {
            usuario.setPuntosTotales(dto.getPuntosTotales());
        }
        if (dto.getNivel() != null) {
            usuario.setNivel(dto.getNivel());
        }

        return mapToDTO(repo.save(usuario));
    }
}
