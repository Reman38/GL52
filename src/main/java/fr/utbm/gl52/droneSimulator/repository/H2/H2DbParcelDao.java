package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.service.entity.DbParcel;

public class H2DbParcelDao extends AbstractH2Dao<DbParcel> {
    public H2DbParcelDao() {
        super(DbParcel.class);
    }

}