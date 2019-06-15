package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.repository.HibernateHelper;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import org.hibernate.Session;

public class H2DbDroneDao extends AbstractH2Dao<DbDrone> {
    public H2DbDroneDao() {
        super(DbDrone.class);
    }

    public void modify(long id, String firstName) {
        Session session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        // TODO déplacer get dans service ?
        DbDrone client = get(id);
//        client.setFirstName(firstName);

        session.merge(client);
        session.getTransaction().commit();
    }

    //TODO si besoin
    //session.createQuery("from Cat where cat.name = ?");
    //session.setString(0, name);

    //TODO catch exception (ex : au delete, modify) et faire remonter jusque dans la vue
}