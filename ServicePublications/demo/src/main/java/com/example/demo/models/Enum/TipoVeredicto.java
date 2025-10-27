package com.example.demo.models.Enum;

public enum TipoVeredicto {

    MODIFICACION("MODIFICACION"),
    RECHAZADA("RECHAZADA"),
    APROBADA("APROBADA");

    private final String value;

    TipoVeredicto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoVeredicto fromString(String text) {
        for (TipoVeredicto tipo : TipoVeredicto.values()) {
            if (tipo.value.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de reacción no válido: " + text);
    }
}
