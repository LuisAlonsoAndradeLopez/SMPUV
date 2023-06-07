/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mx.uv.fei.logic.daosinterfaces;

import java.util.ArrayList;
import mx.uv.fei.logic.domain.Mark;

import mx.uv.fei.logic.domain.Refaction;
import mx.uv.fei.logic.exceptions.ConstraintViolationException;
import mx.uv.fei.logic.exceptions.DataRetrievalException;
import mx.uv.fei.logic.exceptions.DataWritingException;

public interface IRefactionsDAO {
    
    public void addRefactionToDatabase(Refaction refaction) throws DataWritingException, ConstraintViolationException;
    public void modifyRefactionToDatabase(Refaction newRefactionData, Refaction originalRefactionData) throws DataWritingException, ConstraintViolationException;
    public ArrayList<Refaction> getSpecifiedRefactionFromDatabase(String code) throws DataRetrievalException;
    public ArrayList<Refaction> getRefactionsFromDatabase() throws DataRetrievalException;
    public void deleteRefactionsToDatabase(String code) throws DataWritingException;
    public ArrayList<String> getBrandsFromDatabase() throws DataRetrievalException;
    public Mark getSpecifiedBrandFromDatabase(String name) throws DataRetrievalException;
}
