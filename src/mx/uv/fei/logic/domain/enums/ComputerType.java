package mx.uv.fei.logic.domain.enums;

public enum ComputerType {
    LAPTOP("Laptop"),
    DESKTOP("Escritorio");

    private final String computerType;

    ComputerType(String computerType) {
        this.computerType = computerType;
    }

    public String getValue() {
        return computerType;
    }
}
