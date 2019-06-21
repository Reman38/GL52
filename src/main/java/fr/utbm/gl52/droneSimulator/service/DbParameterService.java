package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;

public class DbParameterService extends AbstractService<DbParameter> {

    public DbParameterService() {
        super(DbParameter.class);
    }

    public DbParameter save(Integer duration, Integer nbIteration, Integer disappearingTimeMin, Integer disappearingTimeMax) {
        DbParameter newDbParameter = new DbParameter(duration, nbIteration, disappearingTimeMin, disappearingTimeMax);
        save(newDbParameter);
        return newDbParameter;
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