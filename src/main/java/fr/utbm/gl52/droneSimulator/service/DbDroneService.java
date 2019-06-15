package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;

public class DbDroneService extends AbstractService<DbDrone> {
    public DbDroneService() {
        super(DbDrone.class);
    }

    public void save(String lastName, String firstName, String address, String phone, String email) {
//        DbDrone newDbDrone = new DbDrone(lastName, firstName, address, phone, email);
//        save(newDbDrone);
    }



//    public void modify(long id, String firstName) {
//        modifyInDatabase(id, firstName);
//    }
//
//    private void modifyInDatabase(long id, String firstName) {
//        modify(id, firstName);
//    }

//    private void migrateDbDroneFromDatabaseToConsole() {
//        ConsoleDbDroneDao consoleDbDroneDao = new ConsoleDbDroneDao();
//
//        List<DbDrone> clients = mySqlDbDroneDao.list();
//        for(DbDrone client: clients){
//            consoleDbDroneDao.save(client);
//        }
//    }
}