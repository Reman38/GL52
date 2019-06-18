package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.repository.HibernateHelper;
import fr.utbm.gl52.droneSimulator.service.entity.DbChargingStation;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import org.hibernate.Session;

import java.util.List;

public class H2DbChargingStationDao extends AbstractH2Dao<DbChargingStation> {

    public H2DbChargingStationDao() {
        super(DbChargingStation.class);
    }

    //TODO si besoin
    //session.createQuery("from Cat where cat.name = ?");
    //session.setString(0, name);

    //TODO catch exception (ex : au delete, modify) et faire remonter jusque dans la vue
}