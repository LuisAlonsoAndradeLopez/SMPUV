package mx.uv.fei.logic.daosinterfaces;

import java.util.ArrayList;

import mx.uv.fei.logic.domain.Computer;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public interface IComputerDAO {
    public void addComputerToDatabase(Computer computer) throws DataWritingException, ConstraintViolationException;
    public void modifyComputerDataFromDatabase(Computer newComputerData, Computer originalComputerData) throws DataWritingException, ConstraintViolationException;
    public ArrayList<Computer> getComputersFromDatabase() throws DataRetrievalException;
    public ArrayList<Computer> getSpecifiedComputersFromDatabase(String serialNumber) throws DataRetrievalException;
    public Computer getComputerFromDatabase(String serialNumber) throws DataRetrievalException;
}
