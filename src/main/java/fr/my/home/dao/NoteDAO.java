package fr.my.home.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.Note;
import fr.my.home.dao.impl.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Classe DAO Hibernate qui prends en charge la gestion des notes
 * 
 * @author Jonathan
 * @version 1.2
 * @since 02/05/2018
 */
public class NoteDAO implements HibernateDAO<Note> {

	/**
	 * Attributs
	 */
	private static final String NOTEDAO_GET_ALL = Settings.getStringProperty("note_get_all");
	private static final String NOTEDAO_GET_ONE = Settings.getStringProperty("note_get_one");

	/**
	 * Constructeur
	 */
	public NoteDAO() {};

	/**
	 * Méthodes
	 */

	/**
	 * Récupère la liste de toutes les notes de l'utilisateur, ou exception fonctionnelle si aucune note
	 * 
	 * @param userId
	 * @return List<Note>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<Note> getAllNotes(int userId) throws FonctionnalException, TechnicalException {
		List<Note> listNote = new ArrayList<Note>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Note> query = session.createQuery(NOTEDAO_GET_ALL);
		query.setParameter("note_id_user", userId);
		try {
			listNote = query.getResultList();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucune note enregistrée");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return listNote;
	}

	/**
	 * Récupère une note selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param noteId
	 * @param userId
	 * @return Note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Note getOneNote(int noteId, int userId) throws FonctionnalException, TechnicalException {
		Note note = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<Note> query = session.createQuery(NOTEDAO_GET_ONE);
		query.setParameter("note_id", noteId);
		query.setParameter("note_id_user", userId);
		try {
			note = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("La note n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return note;
	}

	/**
	 * Ajoute une nouvelle note en base, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(Note note) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.save(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la note (Titre déjà existant ?)");
		}
	}

	/**
	 * Met à jour une note, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(Note note) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la note");
		}
	}

	/**
	 * Supprime une note, ou exception fonctionnelle si impossible
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(Note note) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(note);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la note");
		}
	}

}
