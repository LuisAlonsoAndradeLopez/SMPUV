package mx.uv.fei.logic.domain.enums;

public enum RefactionType {
    CPU("Procesador"),
    RAM("Memoria"),
    HDD("Disco"),
    GPU("Tarjeta Gráfica"),
    MOTHERBOARD("Tarjeta Madre"),
    POWERSOURCE("Fuente de Poder");

    private final String refactionType;

    RefactionType(String refactionType) {
        this.refactionType = refactionType;
    }

    public String getValue() {
        return refactionType;
    }
}
