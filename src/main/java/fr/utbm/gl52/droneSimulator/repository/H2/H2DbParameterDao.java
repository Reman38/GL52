package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;

public class H2DbParameterDao extends AbstractH2Dao<DbParameter> {
    public H2DbParameterDao() {
        super(DbParameter.class);
    }


}