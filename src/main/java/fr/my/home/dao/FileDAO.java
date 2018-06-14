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
	private static final String FILEDAO_GET_ONE_BY_NAME = Settings.getStringProperty("files_get_one_by_name");

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
	 * Récupère un fichier selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si il n'existe pas
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
	 * Récupère un fichier selon son nom et l'ID de l'utilisateur, ou exception fonctionnelle si il n'existe pas
	 * 
	 * @param filename
	 * @param userId
	 * @return boolean
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public boolean checkOneFileByName(String filename, int userId) throws TechnicalException {
		boolean exist;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<CustomFile> query = session.createQuery(FILEDAO_GET_ONE_BY_NAME);
		query.setParameter("file_name", filename);
		query.setParameter("file_id_user", userId);
		try {
			// Inutile de stocker le fichier récupéré
			query.getSingleResult();
			exist = true;
		} catch (NoResultException nre) {
			exist = false;
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return exist;
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
