package fr.utbm.gl52.droneSimulator;

import fr.utbm.gl52.droneSimulator.repository.H2.H2Dao;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;
import fr.utbm.gl52.droneSimulator.service.entity.DbParcel;

public class Main {

    public static void main(String[] args) {
        var dbDrone = new DbDrone();
        var dbParcel = new DbParcel();
        var dbParameter = new DbParameter();

        var dbDroneDao = new H2Dao<DbDrone>();
        var dbParcelDao = new H2Dao<DbParcel>();
        var dbParameterDao = new H2Dao<DbParameter>();

        dbDroneDao.save(dbDrone);
        dbParcelDao.save(dbParcel);
        dbParameterDao.save(dbParameter);

        // TODO quelque part...
//        session.close();
//        sessionFactory.close();

//        GuiMain.main(args);
    }


}


