package service;

import fr.utbm.gl52.droneSimulator.repository.AbstractDao;
import fr.utbm.gl52.droneSimulator.repository.H2.AbstractH2Dao;
import fr.utbm.gl52.droneSimulator.service.ServiceInterface;
import fr.utbm.gl52.droneSimulator.service.entity.MyEntity;

import java.lang.reflect.InvocationTargetException;

/**
 * Use different Dao to persist entities
 * @param <E> MyEntity
 */
public class AbstractService<E extends MyEntity> implements ServiceInterface {
    protected AbstractDao<E> mySqlDao;
    protected AbstractDao<E> redisDao;
    private Class<E> clazz;

    public AbstractService(Class<E> clazz) {
        this.clazz = clazz;

        System.out.println("00"+clazz);
        System.out.println("11"+clazz.getClass());
        System.out.println("22"+clazz.getDeclaredClasses());
//        System.out.println(((ParameterizedType)mySuperclass).getActualTypeArguments()[0]);
    }
    private AbstractH2Dao<E> getH2Dao() {
        if (mySqlDao == null){
            mySqlDao = getDao("H2");
        }
        return (AbstractH2Dao<E>) mySqlDao; // TODO éviter le cast
    }

    private AbstractDao<E> getDao(String technology) {
        Object dao = null;
        try {
            System.out.println(technology + clazz + "Dao");
            Class daoClass = Class.forName(technology + clazz + "Dao");
            dao = daoClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        // TODO si le dao spécifique n'est pas trouvé, retourner un DAO plus générique
        return (AbstractDao<E>) dao; // TODO éviter le cast
    }

    private E get(int id) {
        return getInDatabase(id);
    }

    private E getInDatabase(int id) {
        return getH2Dao().get(id);
    }

    public void delete(int id) {
        E entity = getInDatabase(id);
        deleteInDatabase(entity);
    }

    private void deleteInDatabase(E entity) {
        getH2Dao().delete(entity);
    }

    public void save(E entity) {
        saveInDatabase(entity);
    }

    private void saveInDatabase(E entity) {
        getH2Dao().save(entity);
    }

//    private void registerInCsv(Client client) {
//        getCsvClientDao().save(client);
//    }
//
//    private void registerInConsole(Client client) {
//        getConsoleClientDao().save(client);
//    }
//
//    public ConsoleClientDao getConsoleClientDao() {
//        if (consoleClientDao == null)
//            consoleClientDao = new ConsoleClientDao();
//
//        return consoleClientDao;
//    }
//
//    public CsvClientDao getCsvClientDao() {
//        if (csvClientDao == null)
//            csvClientDao = new CsvClientDao();
//
//        return csvClientDao;
//    }
}
