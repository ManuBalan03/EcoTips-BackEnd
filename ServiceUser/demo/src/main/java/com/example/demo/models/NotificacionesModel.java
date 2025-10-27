package com.example.demo.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones", indexes = {
        @Index(columnList = "id_publicacion", name = "idx_notificaciones_publicacion"),
        @Index(columnList = "id_usuario", name = "idx_notificaciones_usuario"),
        @Index(columnList = "leido", name = "idx_notificaciones_leido"), // Útil para filtrar por estado
        @Index(columnList = "fecha_envio", name = "idx_notificaciones_fecha"),
        @Index(columnList = "tipo", name = "idx_notificaciones_tipo")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    private boolean leido;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "id_publicacion")
    private Long idPublicacion; // ⚠️ Si es FK, considera relación @ManyToOne

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_notificaciones_usuario"))
    private UserModel usuario;
}