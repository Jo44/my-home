package fr.my.home.servlet.note;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.Note;
import fr.my.home.bean.User;
import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.NotesManager;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion des notes
 * 
 * @author Jonathan
 * @version 1.6
 * @since 02/05/2018
 */
@WebServlet("/notes")
public class NotesServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(NotesServlet.class);

	/**
	 * Attributs
	 */
	private static final String NOTE_ERROR_DB = Settings.getStringProperty("error_db");
	private NotesManager noteMgr;

	/**
	 * Constructeur
	 */
	public NotesServlet() {
		super();
		// Initialisation du manager
		noteMgr = new NotesManager();
	}

	/**
	 * Redirection vers l'ajout, la liste, le détail et la suppression de notes
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Notes Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère l'attribut success si il existe
		String success = (String) request.getSession().getAttribute("success");
		request.getSession().removeAttribute("success");
		view.addAttributeToList(new ViewAttribut("success", success));

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		int noteId;
		try {
			noteId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			noteId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					// Récupère l'heure actuelle pour redirection vers la page d'ajout d'une note
					addFunction(view);

					// Puis renvoi à l'ajout d'une note
					redirectToNoteNewJSP(request, response, view);
					break;
				// Si action details
				case "details":
					// Récupère la note
					boolean exist = getFunction(view, noteId, userId);
					if (exist) {
						// Si la note existe pour l'utilisateur, redirection vers la page de détails
						redirectToNoteDetailsJSP(request, response, view, noteId);
					} else {
						// Si la note n'existe pas, renvoi à la liste des notes
						redirectToNotesList(request, response, view, userId, null, null);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la note
					deleteFunction(view, noteId, userId);

					// Puis renvoi à la liste des notes
					redirectToNotesList(request, response, view, userId, null, null);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des notes
					redirectToNotesList(request, response, view, userId, orderBy, dir);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des notes
					redirectToNotesList(request, response, view, userId, null, null);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des notes
			redirectToNotesList(request, response, view, userId, null, null);
		}
	}

	/**
	 * Traitement du formulaire d'ajout d'une note
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Notes Servlet [POST] -->");
		logger.info("Tentative d'ajout d'une note en cours ..");

		// Récupération de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupération des informations saisies dans le formulaire
		String date = request.getParameter("date");
		String hour = request.getParameter("hour");
		String minute = request.getParameter("minute");
		String title = new String(request.getParameter("title").trim().getBytes("ISO-8859-1"), "UTF-8");
		String message = new String(request.getParameter("message").trim().getBytes("ISO-8859-1"), "UTF-8");

		try {
			// Ajoute la nouvelle note
			noteMgr.addNote(user.getId(), date, hour, minute, title, message);

			// Ajoute le message de succès en session
			request.getSession().setAttribute("success", "La note a été correctement ajoutée");
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", NOTE_ERROR_DB);
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPost(request, response);
		}
	}

	/**
	 * Récupère la note selon son ID et la charge ainsi que l'erreur dans la view si besoin et charge la view dans la requête
	 * 
	 * @param view
	 * @param noteId
	 * @param userId
	 * @return boolean
	 */
	private boolean getFunction(ViewJSP view, int noteId, int userId) {
		boolean valid = false;
		Note note = null;
		try {
			// Récupère la note selon son ID et l'ID de l'utilisateur
			note = noteMgr.getNote(noteId, userId);

			// Ajoute la note dans la view
			view.addAttributeToList(new ViewAttribut("note", note));

			// La note existe
			valid = true;
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", NOTE_ERROR_DB));
		}
		return valid;
	}

	/**
	 * Récupère l'heure actuelle et la charge dans la view
	 * 
	 * @param view
	 */
	private void addFunction(ViewJSP view) {
		// Récupère la date / heure actuelle en string
		String today = GlobalTools.getDate();

		// Charge la date dans la view
		view.addAttributeToList(new ViewAttribut("today", today));
	}

	/**
	 * Récupère la note selon son ID et essaye de la supprimer de la base de donnée et charge erreur dans la view si besoin
	 * 
	 * @param view
	 * @param noteId
	 * @param userId
	 */
	private void deleteFunction(ViewJSP view, int noteId, int userId) {
		Note note = null;
		try {
			// Récupère la note selon son ID et l'ID de l'utilisateur
			note = noteMgr.getNote(noteId, userId);

			// Supprime la note
			noteMgr.deleteNote(note);

			// Ajoute le message de succès dans la view
			view.addAttributeToList(new ViewAttribut("success", "La note a été correctement supprimée"));
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", NOTE_ERROR_DB));
		}
	}

	/**
	 * Redirige la requête vers la JSP New
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteNewJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Ajout d'une note JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Details
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param noteId
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteDetailsJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int noteId)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Détails de la note {" + noteId + "} JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/details.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Récupère la liste des notes de l'utilisateur et renvoi vers la JSP notes avec message d'erreur si besoin
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToNotesList(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String orderBy, String dir)
			throws ServletException, IOException {
		List<Note> listNote = null;
		try {
			// Récupère la liste des fichiers de l'utilisateur
			listNote = noteMgr.getNotes(userId);

			// Tri la liste en fonction des paramètres
			listNote = noteMgr.orderBy(listNote, orderBy, dir);

		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", NOTE_ERROR_DB));
		}
		view.addAttributeToList(new ViewAttribut("listNote", listNote));
		redirectToNoteListJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP List
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNoteListJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Liste des notes JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/notes/list.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletAfterPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString());
	}

}
