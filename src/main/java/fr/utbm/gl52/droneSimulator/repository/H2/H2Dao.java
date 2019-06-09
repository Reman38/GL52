package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.repository.tool.HibernateHelper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2Dao<T> {
    private Class<T> clazz;

    public T get(long id) {
        var session = HibernateHelper.getSessionFactory().openSession();
        var dbObject = session.get(clazz, id);

        return dbObject;
    }

    public void delete(T dbObject) {
        var session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.delete(dbObject);

        session.getTransaction().commit();
    }

    public void modify(T dbObject) {
        var session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.merge(dbObject);

        session.getTransaction().commit();
    }

//    public List<T> list() {
//        var session = HibernateHelper.getSessionFactory().openSession();
//        List dbObjects = session.createQuery("FROM T ").list();
//
//        return dbObjects;
//    }

    private boolean isThereSomeResults(ResultSet resultset) throws SQLException {
        return resultset.next();
    }

    public void save(T dbObject) {
        var session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(dbObject);

        session.getTransaction().commit();
    }

    //TODO si besoin
    //session.createQuery("from Cat where cat.name = ?");
    //session.setString(0, name);

    //TODO catch exception (ex : au delete, modify) et faire remonter jusque dans la vue
}