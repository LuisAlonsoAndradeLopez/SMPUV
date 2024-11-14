package mx.uv.fei.logic.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import mx.uv.fei.dataaccess.DataBaseManager;
import mx.uv.fei.logic.domain.Service;
import mx.uv.fei.logic.exceptions.DataInsertionException;

public class ServiceDAO {
    private final DataBaseManager dataBaseManager;

    public ServiceDAO() {
        dataBaseManager = new DataBaseManager();
    }

    public int addService(Service service) throws DataInsertionException {
        PreparedStatement serviceStatement;
        String serviceQuery = "INSERT INTO Servicios(NumSerie, fechaInicio, fechaFin, tipo, costo, descripción, estado) VALUES(?,?,?,?,?,?,?)";

        int generatedId = 0;

        try {
            serviceStatement = dataBaseManager.getConnection().prepareStatement(serviceQuery,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            serviceStatement.setString(1, service.getComputer().getSerialNumber());
            serviceStatement.setDate(2, service.getStartDate());
            serviceStatement.setDate(3, service.getEndDate());
            serviceStatement.setString(4, service.getType());
            serviceStatement.setBigDecimal(5, service.getPrice());
            serviceStatement.setString(6, service.getDiagnosis());
            serviceStatement.setString(7, service.getStatus());
            serviceStatement.executeUpdate();

            ResultSet generatedKeys = serviceStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }

            generatedKeys.close();
            serviceStatement.close();
        } catch (SQLException exception) {
            throw new DataInsertionException(
                    "Error al conectar con la base de datos, verifique su conexión e inténtelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }

        return generatedId;
    }

    public boolean isValidDate(Service service) {
        return service.getStartDate().compareTo(service.getEndDate()) < 0;
    }
}
