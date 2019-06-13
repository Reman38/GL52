package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.repository.AbstractDao;
import fr.utbm.gl52.droneSimulator.repository.H2.AbstractH2Dao;
import fr.utbm.gl52.droneSimulator.service.entity.MyEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Use different Dao to persist entities
 * @param <E> MyEntity
 */
public class AbstractService<E extends MyEntity> implements ServiceInterface{
    protected AbstractDao<E> mySqlDao;
    protected AbstractDao<E> redisDao;
    protected Class<E> clazz;

    public AbstractService(Class<E> clazz) {
        this.clazz = clazz;
    }

    public AbstractH2Dao<E> getH2Dao() {
        if (mySqlDao == null){
            mySqlDao = getDao("H2");
        }
        return (AbstractH2Dao<E>) mySqlDao; // TODO éviter le cast
    }

    public AbstractDao<E> getDao(String technology) {
        Object dao = null;
        try {
            System.out.println("fr.utbm.gl52.droneSimulator.repository.H2." + technology + clazz.getSimpleName() + "Dao");
            Class daoClass = Class.forName("fr.utbm.gl52.droneSimulator.repository.H2." + technology + clazz.getSimpleName() + "Dao");
            dao = daoClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        // TODO si le dao spécifique n'est pas trouvé, retourner un DAO plus générique
        return (AbstractDao<E>) dao; // TODO éviter le cast
    }

    public E get(int id) {
        return getInDatabase(id);
    }

    public E getInDatabase(int id) {
        return getH2Dao().get(id);
    }

    public void delete(int id) {
        E entity = getInDatabase(id);
        deleteInDatabase(entity);
    }

    public void deleteInDatabase(E entity) {
        getH2Dao().delete(entity);
    }

    public void save(E entity) {
        saveInDatabase(entity);
    }

    public void saveInDatabase(E entity) {
        getH2Dao().save(entity);
    }

    public List<E> getAllFromSimulationId(int simulationId) {
        return getH2Dao().list(simulationId);
    }
}
