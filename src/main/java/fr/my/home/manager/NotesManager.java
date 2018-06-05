package fr.my.home.manager;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import fr.my.home.bean.Note;
import fr.my.home.dao.NoteDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.GlobalTools;

/**
 * Manager qui prends en charge la gestion des notes
 * 
 * @author Jonathan
 * @version 1.1
 * @since 25/04/2018
 */
public class NotesManager {
	private static final Logger logger = LogManager.getLogger(NotesManager.class);

	/**
	 * Attributs
	 */
	private NoteDAO noteDAO;

	/**
	 * Constructeur
	 */
	public NotesManager() {
		noteDAO = new NoteDAO();
	};

	/**
	 * Méthodes
	 */

	/**
	 * Récupère la liste des notes pour l'utilisateur connecté, ou erreur fonctionnelle si aucune note
	 * 
	 * @param userId
	 * @return List<Note>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<Note> getNotes(int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération des notes en cours ..");
		List<Note> listNote = null;
		try {
			listNote = noteDAO.getAllNotes(userId);
			// Enlève le code HTML du contenu des messages pour affichage plus propre
			for (Note note : listNote) {
				note.setMessage(Jsoup.parse(note.getMessage()).text());
			}
			logger.debug("Récupération des notes enregistrées");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listNote;
	}

	/**
	 * Récupère la note à partir de son ID et de l'ID de l'utilisateur, ou erreur fonctionnelle si aucune note
	 * 
	 * @param noteId
	 * @param userId
	 * @return Note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public Note getNote(int noteId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de récupération d'une note en cours ..");
		Note note = null;
		try {
			note = noteDAO.getOneNote(noteId, userId);
			logger.debug("Récupération d'une note enregistrée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return note;
	}

	/**
	 * Vérifie les champs du formulaire, génère le timestamp puis ajoute la note en base, ou renvoi exception fonctionnelle / technique
	 * 
	 * @param user
	 * @param day
	 * @param hour
	 * @param minute
	 * @param title
	 * @param message
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addNote(int userId, String day, String hour, String minute, String title, String message)
			throws FonctionnalException, TechnicalException {
		// Vérifie si les champs sont bien tous valides
		verifParamsNote(day, hour, minute, title);

		try {
			// Transforme la date de string à timestamp
			Timestamp date = GlobalTools.getTimestampFromString(day, hour, minute);

			// Ajout de la nouvelle note
			Note note = new Note(userId, title.trim(), message.trim(), date);
			noteDAO.add(note);
			logger.debug("Ajout d'une nouvelle note réussi");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie les paramètres d'ajout d'une note et renvoi une exception fonctionnelle si problème
	 * 
	 * @param day
	 * @param hour
	 * @param minute
	 * @param title
	 * @param message
	 * @throws FonctionnalException
	 */
	private void verifParamsNote(String day, String hour, String minute, String title) throws FonctionnalException {
		if (day == null || hour == null || minute == null) {
			throw new FonctionnalException("La date / heure n'est pas correcte");
		} else if (title == null || title.trim().length() < 1 || title.trim().length() > 100) {
			throw new FonctionnalException("Le titre n'est pas renseigné");
		}
	}

	/**
	 * Supprime la note selon l'utilisateur et l'ID de la note, ou message d'erreur dans la view si besoin
	 * 
	 * @param note
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteNote(Note note) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une note en cours ..");
		try {
			// Supprime le fichier
			noteDAO.delete(note);
			logger.debug("Suppression de la note {" + String.valueOf(note.getId()) + "} de la base");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Organise la liste en fonction des paramètres
	 * 
	 * @param listNote
	 * @param orderBy
	 * @param dir
	 * @return List<Note>
	 */
	public List<Note> orderBy(List<Note> listNote, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty() && (orderBy.equals("date") | orderBy.equals("title") | orderBy.equals("message"))
				&& dir != null && !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("date")) {
				// Si ordre par date
				listNote.sort(Comparator.comparing(Note::getSaveDate).thenComparingInt(Note::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
			} else if (orderBy.equals("title")) {
				// Si ordre par titre
				listNote.sort(Comparator.comparing(Note::getTitle).thenComparing(Note::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
			} else {
				// Si ordre par message
				listNote.sort(Comparator.comparing(Note::getMessage).thenComparing(Note::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listNote);
				}
			}
		} else {
			// Ordre par défaut
			listNote.sort(Comparator.comparing(Note::getSaveDate).thenComparingInt(Note::getId));
			Collections.reverse(listNote);
		}
		return listNote;
	}

}
