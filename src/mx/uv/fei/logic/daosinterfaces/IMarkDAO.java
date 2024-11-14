package mx.uv.fei.logic.daosinterfaces;

import java.util.ArrayList;

import mx.uv.fei.logic.domain.Mark;
import mx.uv.fei.logic.exceptions.DataRetrievalException;

public interface IMarkDAO {
    public ArrayList<Mark> getMarksFromDatabase() throws DataRetrievalException;

    public Mark getMarkFromDatabase(int idMark) throws DataRetrievalException;
}
