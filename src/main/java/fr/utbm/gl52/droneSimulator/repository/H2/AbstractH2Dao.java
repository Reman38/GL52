package fr.utbm.gl52.droneSimulator.repository.H2;

import fr.utbm.gl52.droneSimulator.repository.AbstractDao;
import org.hibernate.Session;
import repository.HibernateHelper;

import java.util.List;

/**
 * @param <E> MyEntity
 */
public abstract class AbstractH2Dao<E> extends AbstractDao {
    private Class<E> clazz;

    public E get(long id) {
        Session session = HibernateHelper.getSessionFactory().openSession();

        E entity = session.get(clazz, id);

        session.close();
        return entity;
    }

    public void delete(E entity) {
        Session session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.delete(entity);

        session.getTransaction().commit();
        session.close();
    }

    public List<E> list() {
        Session session = HibernateHelper.getSessionFactory().openSession();

        List entities = session.createQuery("FROM " + clazz).list();

        return entities;
    }

    public void save(E entity) {
        Session session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.save(entity);

        session.getTransaction().commit();
        session.close();
    }

    public void merge(E entity) {
        Session session = HibernateHelper.getSessionFactory().openSession();
        session.beginTransaction();

        session.merge(entity);

        session.getTransaction().commit();
        session.close();
    }

}