package fr.my.home.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.CustomFile;
import fr.my.home.dao.impl.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Classe DAO Hibernate qui prends en charge la gestion des fichiers
 * 
 * @author Jonathan
 * @version 1.2
 * @since 02/05/2018
 */
public class FileDAO implements HibernateDAO<CustomFile> {

	/**
	 * Attributs
	 */
	private static final String FILEDAO_GET_ALL = Settings.getStringProperty("files_get_all");
	private static final String FILEDAO_GET_ONE = Settings.getStringProperty("files_get_one");

	/**
	 * Constructeur
	 */
	public FileDAO() {};

	/**
	 * Méthodes
	 */

	/**
	 * Récupère la liste de toutes les fichiers de l'utilisateur, ou exception fonctionnelle si aucun fichier
	 * 
	 * @param userId
	 * @return List<CustomFile>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<CustomFile> getAllFiles(int userId) throws FonctionnalException, TechnicalException {
		List<CustomFile> listFile = new ArrayList<CustomFile>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ALL);
		query.setParameter("file_id_user", userId);
		try {
			listFile = query.getResultList();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucun fichier enregistré");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return listFile;
	}

	/**
	 * Récupère un fichier selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param fileId
	 * @param userId
	 * @return CustomFile
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public CustomFile getOneFile(int fileId, int userId) throws FonctionnalException, TechnicalException {
		CustomFile file = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE);
		query.setParameter("file_id", fileId);
		query.setParameter("file_id_user", userId);
		try {
			file = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le fichier n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return file;
	}

	/**
	 * Ajoute un nouveau fichier en base, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.save(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter le fichier");
		}
	}

	/**
	 * Met à jour un fichier, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour le fichier");
		}
	}

	/**
	 * Supprime un fichier, ou exception fonctionnelle si impossible
	 * 
	 * @param file
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(CustomFile file) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(file);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer le fichier");
		}
	}

}
