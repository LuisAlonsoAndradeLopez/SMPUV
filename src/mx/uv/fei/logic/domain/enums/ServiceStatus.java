package mx.uv.fei.logic.domain.enums;

public enum ServiceStatus {
    ACTIVE("Activo"),
    CONCLUDED("Concluido");

    private final String status;

    ServiceStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return status;
    }
}
