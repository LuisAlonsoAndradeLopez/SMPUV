package mx.uv.fei.logic.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.uv.fei.dataaccess.DataBaseManager;
import mx.uv.fei.logic.daosinterfaces.IRefactionsDAO;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.domain.Refaction;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public class RefactionDAO implements IRefactionsDAO {
    private static final Logger LOGGER = Logger.getLogger(RefactionDAO.class.getName());
    private final DataBaseManager dataBaseManager;

    public RefactionDAO() {
        dataBaseManager = new DataBaseManager();
    }

    @Override
    public void addRefactionToDatabase(Refaction refaction) throws DataWritingException, ConstraintViolationException {
        try {
            String query = "INSERT INTO Refacciones (NumSerie, nombre, IdMarca, tipo, precio, stock) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, refaction.getCode());
            preparedStatement.setString(2, refaction.getName());
            preparedStatement.setInt(3, refaction.getBrand().getIdMark());
            preparedStatement.setString(4, refaction.getType());
            preparedStatement.setDouble(5, refaction.getPrice());
            preparedStatement.setInt(6, refaction.getQuantity());

            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new ConstraintViolationException("Código ya utilizado, por favor introduzca otro");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataWritingException("Error al agregar refacción. Verifique su conexión e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
    }

    @Override
    public void modifyRefactionToDatabase(Refaction newRefactionData, Refaction originalRefactionData)
            throws DataWritingException, ConstraintViolationException {
        try {
            String query = "UPDATE Refacciones SET nombre = ?, IdMarca = ?, tipo = ?, precio = ?, stock = ? WHERE NumSerie = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);

            preparedStatement.setString(1, newRefactionData.getName());
            preparedStatement.setInt(2, newRefactionData.getBrand().getIdMark());
            preparedStatement.setString(3, newRefactionData.getType());
            preparedStatement.setDouble(4, newRefactionData.getPrice());
            preparedStatement.setInt(5, newRefactionData.getQuantity());

            preparedStatement.setString(6, originalRefactionData.getCode());

            preparedStatement.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new ConstraintViolationException("Código ya utilizado, por favor introduzca otro");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataWritingException("Error al modificar. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
    }

    @Override
    public ArrayList<Refaction> getSpecifiedRefactionFromDatabase(String code) throws DataRetrievalException {
        ArrayList<Refaction> refacciones = new ArrayList<>();

        try {
            String query = "SELECT * FROM Refacciones WHERE NumSerie LIKE ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, code + '%');
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Refaction refaccion = new Refaction();
                refaccion.setCode(resultSet.getString("NumSerie"));
                refaccion.setName(resultSet.getString("nombre"));
                refaccion.setType(resultSet.getString("tipo"));
                refaccion.setPrice(resultSet.getDouble("precio"));
                refaccion.setQuantity(resultSet.getInt("stock"));

                MarkDAO markDAO = new MarkDAO();
                Mark brand = markDAO.getMarkFromDatabase(resultSet.getInt("IdMarca"));
                refaccion.setBrand(brand);
                refacciones.add(refaccion);
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataRetrievalException(
                    "Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }

        return refacciones;
    }

    @Override
    public ArrayList<Refaction> getRefactionsFromDatabase() throws DataRetrievalException {
        ArrayList<Refaction> refactions = new ArrayList<>();

        try {
            Statement statement = dataBaseManager.getConnection().createStatement();
            String query = "SELECT * FROM Refacciones";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Refaction refaccion = new Refaction();
                refaccion.setCode(resultSet.getString("NumSerie"));
                refaccion.setName(resultSet.getString("nombre"));
                refaccion.setPrice(resultSet.getDouble("precio"));
                refaccion.setQuantity(resultSet.getInt("stock"));
                refaccion.setType(resultSet.getString("tipo"));
                MarkDAO markDAO = new MarkDAO();
                Mark brand = markDAO.getMarkFromDatabase(resultSet.getInt("IdMarca"));
                refaccion.setBrand(brand);
                refactions.add(refaccion);
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataRetrievalException(
                    "Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
        return refactions;
    }

    @Override
    public void deleteRefactionsToDatabase(String code) throws DataWritingException {
        try {
            String query = "DELETE FROM Refacciones WHERE NumSerie = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setString(1, code);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataWritingException("Error al eliminar. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
    }

    public ArrayList<String> getBrandsFromDatabase() throws DataRetrievalException {
        ArrayList<String> marcas = new ArrayList<>();

        try {
            Statement statement = dataBaseManager.getConnection().createStatement();
            String query = "SELECT nombre FROM Marcas";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String marca = resultSet.getString("nombre");
                marcas.add(marca);
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataRetrievalException(
                    "Fallo al recuperar las Marcas. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
        return marcas;
    }

    @Override
    public Mark getSpecifiedBrandFromDatabase(String name) throws DataRetrievalException {
        Mark marca = new Mark();

        try {
            String query = "SELECT FROM Marcas WHERE nombre = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                marca.setName(resultSet.getString("nombre"));
            }
            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Something went wrong", e);
            throw new DataRetrievalException(
                    "Fallo al recuperar las Marcas. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }
        return marca;
    }

}