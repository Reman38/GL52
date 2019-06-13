package fr.utbm.gl52.droneSimulator.repository;

public class AbstractDao<E> implements DaoInterface {
    protected Class<E> clazz;

    public AbstractDao(Class<E> clazz) {
        this.clazz = clazz;
    }
}
