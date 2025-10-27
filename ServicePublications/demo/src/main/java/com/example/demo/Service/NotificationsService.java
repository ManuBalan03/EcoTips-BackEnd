package com.example.demo.Service;


import com.example.demo.DTO.NotificationsDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Service
public class NotificationsService {

    private final RestTemplate restTemplate;

    @Value("${usuarios.service.url}")
    private String notificacionesServiceUrl;

    @Autowired
    private HttpServletRequest request;

    public NotificationsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void enviarNotificacion(@Valid NotificationsDTO dto) {
        try {
            if (dto == null || dto.getTipo() == null || dto.getMensaje() == null) {
                throw new IllegalArgumentException("DTO o campos requeridos no pueden ser nulos");
            }

            // Obtener el token del request actual
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String token = request.getHeader("Authorization");
            if (token == null || !token.startsWith("Bearer ")) {
                throw new SecurityException("Token JWT no encontrado");
            }
            headers.set("Authorization", token);

            HttpEntity<NotificationsDTO> entity = new HttpEntity<>(dto, headers);

            String url = notificacionesServiceUrl.endsWith("/")
                    ? notificacionesServiceUrl + "notificaciones"
                    : notificacionesServiceUrl + "/notificaciones";

            System.out.println("Enviando a URL: " + url);
            System.out.println("Cuerpo: " + dto);
            System.out.println("Headers: " + headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Respuesta recibida - Status: " + response.getStatusCode());
            System.out.println("Cuerpo respuesta: " + response.getBody());

        } catch (Exception e) {
            if (e instanceof HttpClientErrorException ex) {
                System.err.println("Error HTTP " + ex.getStatusCode() + ": " + ex.getResponseBodyAsString());
            } else {
                System.err.println("Error inesperado: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
}