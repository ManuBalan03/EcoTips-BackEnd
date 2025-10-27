package com.example.demo.controllers;
import com.example.demo.DTO.NotificationsDTO;
import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.DTO.UserDTO;
import com.example.demo.Service.NotificationsService.NotificacionService;
import com.example.demo.Service.UserService;
import com.example.demo.models.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class userscontroller {
        private final NotificacionService notificacionService;
        private final UserService userService;

        // ✅ Crear usuario
        @PostMapping
        public ResponseEntity<UserModel> crearUsuario(@RequestBody UserModel usuario) {
                return ResponseEntity.ok(userService.crearUsuario(usuario));
        }

        // ✅ Obtener todos los usuarios (paginado - RECOMENDADO)
        @GetMapping("/paginados")
        public ResponseEntity<Page<UserDTO>> listarUsuariosPaginados(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "20") int size,
                @RequestParam(defaultValue = "nombre") String sort) {

                Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
                Page<UserDTO> usuarios = userService.obtenerTodosPaginados(pageable);
                return ResponseEntity.ok(usuarios);
        }

        // ✅ Mantener endpoint antiguo para compatibilidad (con límite)
        @GetMapping
        public ResponseEntity<List<UserDTO>> listarUsuarios() {
                return ResponseEntity.ok(userService.obtenerTodos());
        }

        // ✅ Obtener usuario por ID
        @GetMapping("/{id}")
        public ResponseEntity<UserDTO> obtenerUsuario(@PathVariable Long id) {
                return userService.obtenerPorId(id)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }

        // ✅ Eliminar usuario
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminar(@PathVariable Long id) {
                userService.eliminarUsuario(id);
                return ResponseEntity.noContent().build();
        }

        // ✅ Sumar puntos (MEJORADO - ahora retorna el usuario actualizado)
        @PutMapping("/{id}/puntos")
        public ResponseEntity<UserDTO> actualizarPuntos(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
                System.out.println("ID: " + id + ", Puntos a sumar: " + dto.getPuntosTotales());

                UserDTO usuarioActualizado = userService.sumarPuntosUsuario(id, dto);
                return ResponseEntity.ok(usuarioActualizado);
        }

        // ✅ Obtener puntos específicos (CORREGIDO)
        @GetMapping("/{id}/puntos")
        public ResponseEntity<Integer> obtenerPuntos(@PathVariable Long id) {
                Integer puntos = userService.obtenerPuntos(id);
                return ResponseEntity.ok(puntos);
        }

        // ✅ Actualizar usuario completo (MEJORADO - ahora retorna el usuario actualizado)
        @PutMapping("/{id}")
        public ResponseEntity<UserDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UserDTO dto) {
                UserDTO usuarioActualizado = userService.actualizarUsuario(id, dto);
                return ResponseEntity.ok(usuarioActualizado);
        }

        // ✅ NUEVO: Actualización parcial (solo puntos y nivel)
        @PatchMapping("/{id}")
        public ResponseEntity<UserDTO> actualizarParcial(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
                UserDTO usuarioActualizado = userService.actualizarParcial(id, dto);
                return ResponseEntity.ok(usuarioActualizado);
        }

        // ✅ NUEVO: Verificar si usuario existe
        @GetMapping("/{id}/existe")
        public ResponseEntity<Boolean> existeUsuario(@PathVariable Long id) {
                boolean existe = userService.existeUsuario(id);
                return ResponseEntity.ok(existe);
        }

        // ✅ NUEVO: Obtener usuario por email
        @GetMapping("/email/{email}")
        public ResponseEntity<UserDTO> obtenerUsuarioPorEmail(@PathVariable String email) {
                return userService.obtenerPorEmail(email)
                        .map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        }

//        // ✅ NUEVO: Buscar usuarios por nombre o email
//        @GetMapping("/buscar")
//        public ResponseEntity<Page<UserDTO>> buscarUsuarios(
//                @RequestParam String query,
//                @RequestParam(defaultValue = "0") int page,
//                @RequestParam(defaultValue = "10") int size) {
//
//                Pageable pageable = PageRequest.of(page, size);
//                Page<UserDTO> resultados = userService.busa(query, pageable);
//                return ResponseEntity.ok(resultados);
//        }

}
