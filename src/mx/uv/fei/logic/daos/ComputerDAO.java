package mx.uv.fei.logic.daos;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

import mx.uv.fei.dataaccess.DataBaseManager;
import mx.uv.fei.logic.daosinterfaces.IComputerDAO;
import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class ComputerDAO implements IComputerDAO {
    private final DataBaseManager dataBaseManager;

    public ComputerDAO(){
        dataBaseManager = new DataBaseManager();
    }

    @Override
    public void addComputerToDatabase(Computer computer) throws DataWritingException, ConstraintViolationException {
        try{
            String query = 
                "INSERT INTO EquiposComputo (cpu, ram, disco, gpu, IdMarca, fuentePoder, motherboard, NumSerie, fechaAdquisición, estado, tipo)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = 
                dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, computer.getCpu());
            preparedStatement.setString(2, computer.getRamMemory());
            preparedStatement.setString(3, computer.getDisk());
            preparedStatement.setString(4, computer.getGpu());
            preparedStatement.setInt(5, computer.getMark().getIdMark());
            preparedStatement.setString(6, computer.getPowerSource());
            preparedStatement.setString(7, computer.getMotherBoard());
            preparedStatement.setString(8, computer.getSerialNumber());
            preparedStatement.setDate(9, computer.getAdquisitionDate());
            preparedStatement.setString(10, computer.getStatus());
            preparedStatement.setString(11, computer.getType());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            throw new ConstraintViolationException("Número de serie ya utilizado, por favor introduzca otro");
        }catch(SQLException e){
            throw new DataWritingException("Error al agregar computadora. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }
    }

    @Override
    public void modifyComputerDataFromDatabase(Computer newComputerData, Computer originalComputerData) throws DataWritingException, ConstraintViolationException {
        try{
            String query = "UPDATE EquiposComputo SET cpu = ?, ram = ?, disco = ?, gpu = ?, IdMarca = ?, fuentePoder = ?, " + 
                           "motherboard = ?, NumSerie = ?, fechaAdquisición = ?, estado = ?, tipo = ? WHERE NumSerie = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, newComputerData.getCpu());
            preparedStatement.setString(2, newComputerData.getRamMemory());
            preparedStatement.setString(3, newComputerData.getDisk());
            preparedStatement.setString(4, newComputerData.getGpu());
            preparedStatement.setInt(5, newComputerData.getMark().getIdMark());
            preparedStatement.setString(6, newComputerData.getPowerSource());
            preparedStatement.setString(7, newComputerData.getMotherBoard());
            preparedStatement.setString(8, newComputerData.getSerialNumber());
            preparedStatement.setDate(9, newComputerData.getAdquisitionDate());
            preparedStatement.setString(10, newComputerData.getStatus());
            preparedStatement.setString(11, newComputerData.getType());
            preparedStatement.setString(12, originalComputerData.getSerialNumber());
            preparedStatement.executeUpdate();
        }catch(SQLIntegrityConstraintViolationException e){
            throw new ConstraintViolationException("Número de serie ya utilizado, por favor introduzca otro");
        }catch(SQLException e){
            throw new DataWritingException("Error al modificar computadora. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }
    }

    @Override
    public ArrayList<Computer> getComputersFromDatabase() throws DataRetrievalException {
        ArrayList<Computer> computers = new ArrayList<>();

        try{
            Statement statement = dataBaseManager.getConnection().createStatement();
            String query = "SELECT * FROM EquiposComputo";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                Computer computer = new Computer();
                computer.setCpu(resultSet.getString("cpu"));
                computer.setRamMemory(resultSet.getString("ram"));
                computer.setDisk(resultSet.getString("disco"));
                computer.setGpu(resultSet.getString("gpu"));
                computer.setPowerSource(resultSet.getString("fuentePoder"));
                computer.setMotherBoard(resultSet.getString("motherboard"));
                computer.setSerialNumber(resultSet.getString("NumSerie"));
                computer.setAdquisitionDate(Date.valueOf(resultSet.getString("fechaAdquisición")));
                computer.setStatus(resultSet.getString("estado"));
                computer.setType(resultSet.getString("tipo"));
                MarkDAO markDAO = new MarkDAO();
                Mark mark = markDAO.getMarkFromDatabase(resultSet.getInt("IdMarca"));
                computer.setMark(mark);
                computers.add(computer);
            }

            resultSet.close();
            dataBaseManager.getConnection().close();
        }catch(SQLException e){
            throw new DataRetrievalException("Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }

        return computers;
    }

    @Override
    public ArrayList<Computer> getSpecifiedComputersFromDatabase(String serialNumber) throws DataRetrievalException {
        ArrayList<Computer> computers = new ArrayList<>();

        try{
            String query = "SELECT * FROM EquiposComputo WHERE NumSerie LIKE ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, serialNumber + '%');
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Computer computer = new Computer();
                computer.setCpu(resultSet.getString("cpu"));
                computer.setRamMemory(resultSet.getString("ram"));
                computer.setDisk(resultSet.getString("disco"));
                computer.setGpu(resultSet.getString("gpu"));
                computer.setPowerSource(resultSet.getString("fuentePoder"));
                computer.setMotherBoard(resultSet.getString("motherboard"));
                computer.setSerialNumber(resultSet.getString("NumSerie"));
                computer.setAdquisitionDate(Date.valueOf(resultSet.getString("fechaAdquisición")));
                computer.setStatus(resultSet.getString("estado"));
                computer.setType(resultSet.getString("tipo"));
                MarkDAO markDAO = new MarkDAO();
                Mark mark = markDAO.getMarkFromDatabase(resultSet.getInt("IdMarca"));
                computer.setMark(mark);
                computers.add(computer);
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        }catch(SQLException e){
            throw new DataRetrievalException("Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }

        return computers;
    }

    @Override
    public Computer getComputerFromDatabase(String serialNumber) throws DataRetrievalException {
        Computer computer = new Computer();

        try{
            String query = "SELECT * FROM EquiposComputo WHERE NumSerie = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, serialNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                computer.setCpu(resultSet.getString("cpu"));
                computer.setRamMemory(resultSet.getString("ram"));
                computer.setDisk(resultSet.getString("disco"));
                computer.setGpu(resultSet.getString("gpu"));
                computer.setPowerSource(resultSet.getString("fuentePoder"));
                computer.setMotherBoard(resultSet.getString("motherboard"));
                computer.setSerialNumber(resultSet.getString("NumSerie"));
                computer.setAdquisitionDate(Date.valueOf(resultSet.getString("fechaAdquisición")));
                computer.setStatus(resultSet.getString("estado"));
                computer.setType(resultSet.getString("tipo"));
                MarkDAO markDAO = new MarkDAO();
                Mark mark = markDAO.getMarkFromDatabase(resultSet.getInt("IdMarca"));
                computer.setMark(mark);
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        }catch(SQLException e){
            throw new DataRetrievalException("Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }

        return computer;
    }

    public boolean theComputerIsAlreadyRegisted(Computer computer) throws DataRetrievalException {
        try{
            Statement statement = dataBaseManager.getConnection().createStatement();
            String query = "SELECT * FROM EquiposComputo";
            ResultSet resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                if(resultSet.getString("cpu").equals(computer.getCpu()) &&
                   resultSet.getString("ram").equals(computer.getRamMemory()) &&
                   resultSet.getString("disco").equals(computer.getDisk()) &&
                   resultSet.getString("gpu").equals(computer.getGpu()) &&
                   resultSet.getInt("IdMarca") == computer.getMark().getIdMark() &&
                   resultSet.getString("fuentePoder").equals(computer.getPowerSource()) &&
                   resultSet.getString("motherboard").equals(computer.getMotherBoard()) &&
                   resultSet.getString("NumSerie").equals(computer.getSerialNumber()) &&
                   resultSet.getDate("fechaAdquisición").equals(computer.getAdquisitionDate()) &&
                   resultSet.getString("estado").equals(computer.getStatus()) &&
                   resultSet.getString("tipo").equals(computer.getType())){
                    
                   resultSet.close();
                   dataBaseManager.getConnection().close();
                   return true;
                }
            }

            resultSet.close();
            dataBaseManager.getConnection().close();
        }catch(SQLException e){
            throw new DataRetrievalException("Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        }finally{
            dataBaseManager.closeConnection();
        }

        return false;
    }
}
