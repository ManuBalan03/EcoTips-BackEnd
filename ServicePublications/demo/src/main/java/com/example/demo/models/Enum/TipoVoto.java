package com.example.demo.models.Enum;

public enum TipoVoto {
    EN_DESACUERDO("EN_DESACUERDO"),
    ACUERDO("ACUERDO");

    private final String value;

    TipoVoto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TipoVoto fromString(String text) {
        for (TipoVoto tipo : TipoVoto.values()) {
            if (tipo.value.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de reacción no válido: " + text);
    }
}
