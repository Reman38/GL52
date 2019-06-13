package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;

public class DroneService extends service.AbstractService<DbDrone> {
    public DroneService(Class<DbDrone> clazz) {
        super(clazz);
    }

    public void register(String lastName, String firstName, String address, String phone, String email) {
//        DbDrone newDrone = new DbDrone(lastName, firstName, address, phone, email);
//        save(newDrone);
    }

//    public void modify(long id, String firstName) {
//        modifyInDatabase(id, firstName);
//    }
//
//    private void modifyInDatabase(long id, String firstName) {
//        modify(id, firstName);
//    }

//    private void migrateDroneFromDatabaseToConsole() {
//        ConsoleDroneDao consoleDroneDao = new ConsoleDroneDao();
//
//        List<Drone> drones = mySqlDroneDao.list();
//        for(Drone drone: drones){
//            consoleDroneDao.save(drone);
//        }
//    }
}