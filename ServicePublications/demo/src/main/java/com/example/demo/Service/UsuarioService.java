package com.example.demo.Service;

import com.example.demo.DTO.UpdateUserDTO;
import com.example.demo.DTO.UserDTO;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${usuarios.service.url}")
    private String usuariosServiceUrl;

    // Necesitamos acceder al contexto de seguridad para obtener el token
    @Autowired
    private HttpServletRequest request;

    public String[] obtenerNombrePorId(Long idUsuario) {
        String[] Datos = new String[3];
        try {
            // Crear HttpHeaders y añadir el token JWT del contexto actual
            HttpHeaders headers = new HttpHeaders();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                headers.set("Authorization", authHeader);
            }

            // Crear HttpEntity con los headers
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Usar exchange en lugar de getForObject para incluir los headers
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    usuariosServiceUrl + "/usuarios/" + idUsuario,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );

            UserDTO usuario = response.getBody();

            Datos[0]=usuario != null ? usuario.getNombre() : "Desconocido";
            Datos[1]=usuario != null ? usuario.getFotoPerfil() : "";
            return Datos;

        } catch (Exception e) {
            e.printStackTrace(); // para ver detalle en consola
            Datos[0]="Desconocido";
            Datos[1]="";
            return Datos;
        }
    }

    public void actualizarUsuarioRemoto(Long idUsuario, UpdateUserDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        System.out.println("DATO "+dto.getPuntosTotales());

        // Crear headers con el token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new SecurityException("Token JWT no encontrado");
        }
        headers.set("Authorization", token);

        HttpEntity<UpdateUserDTO> entity = new HttpEntity<>(dto, headers);

        // Construir la URL del endpoint externo
        String url = usuariosServiceUrl.endsWith("/") ?
                usuariosServiceUrl + "usuarios/" + idUsuario +"/puntos":
                usuariosServiceUrl + "/usuarios/" + idUsuario+"/puntos";

        System.out.println("Enviando actualización PUT a: " + url);
        System.out.println("Payload: " + dto);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    String.class
            );

            System.out.println("Respuesta recibida: " + response.getStatusCode());
            System.out.println("Cuerpo: " + response.getBody());

        } catch (HttpClientErrorException ex) {
            System.err.println("Error HTTP: " + ex.getStatusCode());
            System.err.println("Cuerpo error: " + ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }



}