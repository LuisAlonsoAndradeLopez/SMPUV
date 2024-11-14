package mx.uv.fei.logic.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import mx.uv.fei.dataaccess.DataBaseManager;
import mx.uv.fei.logic.daosinterfaces.IMarkDAO;
import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.exceptions.DataRetrievalException;

public class MarkDAO implements IMarkDAO {
    private final DataBaseManager dataBaseManager;

    public MarkDAO() {
        dataBaseManager = new DataBaseManager();
    }

    @Override
    public ArrayList<Mark> getMarksFromDatabase() throws DataRetrievalException {
        ArrayList<Mark> marks = new ArrayList<>();

        try {
            Statement statement = dataBaseManager.getConnection().createStatement();
            String query = "SELECT * FROM Marcas";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Mark mark = new Mark();
                mark.setIdMark(resultSet.getInt("IdMarca"));
                mark.setName(resultSet.getString("nombre"));
                marks.add(mark);
            }

            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {

            throw new DataRetrievalException(
                    "Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }

        return marks;
    }

    @Override
    public Mark getMarkFromDatabase(int idMark) throws DataRetrievalException {
        Mark mark = new Mark();

        try {
            String query = "SELECT * FROM Marcas WHERE IdMarca = ?";
            PreparedStatement preparedStatement = dataBaseManager.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idMark);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                mark.setIdMark(resultSet.getInt("IdMarca"));
                mark.setName(resultSet.getString("nombre"));
            }

            resultSet.close();
            dataBaseManager.getConnection().close();
        } catch (SQLException e) {

            throw new DataRetrievalException(
                    "Fallo al recuperar la informacion. Verifique su conexion e intentelo de nuevo");
        } finally {
            dataBaseManager.closeConnection();
        }

        return mark;
    }

}
