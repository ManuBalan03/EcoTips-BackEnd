package com.example.demo.Service;
import com.example.demo.DTO.urlPerfilDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class S3Service implements S3ServiceImpl {

    private final RestTemplate restTemplate;

    @Value("${publicaciones.service.url}")
    private String UrlPublicaciones;

    public S3Service(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Ahora el método recibe el token explícitamente desde el controlador o servicio que lo llama.
     */
    @Override
    public urlPerfilDTO ObtenerUrlPerfil(@Valid urlPerfilDTO dto, String token) {
        try {
            if (dto == null || dto.getUrlkey() == null) {
                throw new IllegalArgumentException("DTO o campos requeridos no pueden ser nulos");
            }

            if (token == null || token.isEmpty()) {
                throw new SecurityException("Token JWT no encontrado");
            }

            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }

            // 🟢 Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", token);

            HttpEntity<urlPerfilDTO> entity = new HttpEntity<>(dto, headers);

            // 🟢 Llamar al microservicio de publicaciones
            String url = UrlPublicaciones + "s3/download/presigned";

            System.out.println("Enviando a URL: " + url);
            System.out.println("Cuerpo: " + dto);
            System.out.println("Headers: " + headers);

            ResponseEntity<urlPerfilDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    urlPerfilDTO.class
            );

            System.out.println("Respuesta recibida - Status: " + response.getStatusCode());
            System.out.println("Cuerpo respuesta: " + response.getBody());

            // ✅ Si la respuesta fue exitosa, actualiza el DTO con la URL recibida
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                dto.setUrlkey(response.getBody().getUrlkey());
            }

        } catch (Exception e) {
            if (e instanceof HttpClientErrorException ex) {
                System.err.println("Error HTTP " + ex.getStatusCode() + ": " + ex.getResponseBodyAsString());
            } else {
                System.err.println("Error inesperado: " + e.getMessage());
            }
            e.printStackTrace();
        }

        return dto;
    }
}
