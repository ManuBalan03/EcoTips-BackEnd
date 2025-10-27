package com.example.demo.Service;
import java.util.List;
import java.util.Optional;

import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.models.UserModel;
import com.example.demo.DTO.UserDTO;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
        UserModel crearUsuario(UserModel usuario);
        List<UserDTO> obtenerTodos();
        Page<UserDTO> obtenerTodosPaginados(Pageable pageable); // ✅ Nuevo
        Optional<UserDTO> obtenerPorId(Long id);
        void eliminarUsuario(Long id);
        UserDTO sumarPuntosUsuario(Long idUsuario, UpdateUserDTO dto);
        UserDTO actualizarUsuario(Long idUsuario, UserDTO dto);
        Integer obtenerPuntos(Long id);

        // ✅ Nuevos métodos optimizados
        boolean existeUsuario(Long id);
        Optional<UserDTO> obtenerPorEmail(String email);
        UserDTO actualizarParcial(Long id, UpdateUserDTO dto);

}
