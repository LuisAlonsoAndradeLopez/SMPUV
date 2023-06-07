package mx.uv.fei.logic.daos;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import mx.uv.fei.dataaccess.DataBaseManager;
import mx.uv.fei.logic.daosinterfaces.ILoginDAO;
import mx.uv.fei.logic.domain.Admin;
import mx.uv.fei.logic.domain.MaintenanceMan;
import mx.uv.fei.logic.exceptions.LoginException;

public class LoginDAO implements ILoginDAO{
    private final DataBaseManager dataBaseManager;
    
    public LoginDAO(){
        dataBaseManager = new DataBaseManager();
    }
    
    @Override
    public MaintenanceMan logInMaintenanceMan(String staffNumber, String password) throws LoginException{
        PreparedStatement statement;
        MaintenanceMan maintenanceMan = new MaintenanceMan();
        String query = "SELECT nombre, apellidoPaterno, apellidoMaterno, NumPersonal FROM Técnicos "
                + "WHERE NumPersonal = ? AND contraseña = SHA2(?, 256)";
        
        try{
            statement = dataBaseManager.getConnection().prepareStatement(query);
            statement.setString(1, staffNumber);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                maintenanceMan.setName(resultSet.getString("nombre"));
                maintenanceMan.setFirstSurname(resultSet.getString("apellidoPaterno"));
                maintenanceMan.setSecondSurname(resultSet.getString("apellidoMaterno"));
                maintenanceMan.setStaffNumber(resultSet.getInt("NumPersonal"));
            }
        }catch(SQLException exception){
            throw new LoginException("Error de conexión. Favor de verificar su conexión e inténtelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }
        
        return maintenanceMan;
    }
    
    @Override
    public Admin logInAdmin(String adminId, String password) throws LoginException {
        PreparedStatement statement;
        String query = "SELECT nombre, apellidoPaterno, apellidoMaterno FROM Administradores "
        + "WHERE IdAdministrador = ? AND contraseña = SHA2(?, 256)";
        Admin degreeBoss = new Admin();
        
        try{
            statement = dataBaseManager.getConnection().prepareStatement(query);
            
            statement.setString(1, adminId);
            statement.setString(2, password);
            
            ResultSet resultSet = statement.executeQuery();
            
            if(resultSet.next()){
                degreeBoss.setName(resultSet.getString("nombre"));
                degreeBoss.setFirstSurname(resultSet.getString("apellidoPaterno"));
                degreeBoss.setSecondSurname(resultSet.getString("apellidoMaterno"));
            }
        }catch(SQLException exception){
            throw new LoginException("Error de conexion. Favor de verificar su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }
        
        return degreeBoss;
    } 
}