package mx.uv.fei.logic.domain.enums;

public enum ComputerStatus {
    ACTIVE("Activo"), 
    ON_MAINTENANCE("En Mantenimiento"),
    OUT_OF_SERVICE("Fuera de Servicio");
    
    private final String status;
    
    ComputerStatus(String status){
        this.status = status;
    }
    
    public String getValue(){
        return status;
    }
}
