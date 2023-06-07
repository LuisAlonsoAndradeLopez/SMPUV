package mx.uv.fei.logic.daosinterfaces;

import mx.uv.fei.logic.domain.Admin;
import mx.uv.fei.logic.domain.MaintenanceMan;
import mx.uv.fei.logic.exceptions.LoginException;

public interface ILoginDAO {
    public Admin logInAdmin(String adminId, String password) throws LoginException;
    public MaintenanceMan logInMaintenanceMan(String staffNumber, String password) throws LoginException;
}
