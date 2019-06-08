package fr.utbm.gl52.droneSimulator.tools;

// TODO refactor

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHelper {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
//        try {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
//
//            ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//
//            return configuration.buildSessionFactory(serviceRegistryObj);
        return configuration.buildSessionFactory();
//            return new Configuration().configure().buildSessionFactory();
//        } catch (Throwable e) {
//            System.err.println("Initial SessionFactory creation failed." + e);
//            throw new ExceptionInInitializerError(e);
//        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
