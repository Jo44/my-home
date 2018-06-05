package fr.my.home.hibernate;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.my.home.bean.CustomFile;

public class HibernateTest {
	private static final Logger logger = LogManager.getLogger(HibernateTest.class);
	private static SessionFactory sessionFactory;

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.info("Initialisation de HibernateTest");
		initializeSessionFactory();
	}

	@Test
	public void testHibernate() {
		logger.info("Ajout d'un fichier ..");
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		CustomFile newFile = new CustomFile(999, 100, "filename", null);
		session.save(newFile);
		session.getTransaction().commit();
		session.close();

		logger.info("Récupération des fichiers ..");
		session = sessionFactory.openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<CustomFile> result = session.createQuery("from CustomFile").list();
		for (CustomFile file : result) {
			System.out.println("Fichier : (" + file.toString() + ")");
		}
		session.getTransaction().commit();
		session.close();

		logger.info("Suppression d'un fichier ..");
		session = sessionFactory.openSession();
		session.beginTransaction();
		session.remove(newFile);
		session.getTransaction().commit();
		session.close();
	}

	/**
	 * Méthode d'initialisation
	 */
	private static void initializeSessionFactory() {
		// Configuration de la SessionFactory
		sessionFactory = new Configuration().configure() // configure à partir des paramètres hibernate.cfg.xml
				.buildSessionFactory();
	}

}
