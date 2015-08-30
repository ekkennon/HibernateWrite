/**
 * 
 */
package com.bjc.ekk.ccowhib;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author ekk9418
 *
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	
	static {
        try {
        	Configuration config = new Configuration().addAnnotatedClass(CcowUser.class).configure();
        	
			serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
			sessionFactory = config.buildSessionFactory(serviceRegistry);
			
        } catch (HibernateException ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
        }
    }
	
	public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}