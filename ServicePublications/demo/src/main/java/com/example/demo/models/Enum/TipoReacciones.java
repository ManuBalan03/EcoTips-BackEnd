package com.example.demo.models.Enum;

public enum TipoReacciones {
    LIKE("LIKE"),
    DISLIKE("DISLIKE"),
    ME_ENCANTA("ME_ENCANTA"),
    INTERESANTE("INTERESANTE"),
    TRISTE("TRISTE");

    private final String value;

    TipoReacciones(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoReacciones fromString(String text) {
        for (TipoReacciones tipo : TipoReacciones.values()) {
            if (tipo.value.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de reacción no válido: " + text);
    }
}