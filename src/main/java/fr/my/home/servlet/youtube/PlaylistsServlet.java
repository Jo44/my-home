package fr.my.home.servlet.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;
import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion des playlists YouTube
 * 
 * @author Jonathan
 * @version 1.2
 * @since 03/05/2018
 */
@WebServlet("/youtube_playlists")
public class PlaylistsServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlaylistsServlet.class);

	/**
	 * Attributs
	 */
	private static final String PLAYLIST_ERROR_DB = Settings.getStringProperty("error_db");
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public PlaylistsServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	/**
	 * Redirection vers la liste, l'ajoute, la modification ou la suppression de playlist
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut error si il existe
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
		String idPlaylist = request.getParameter("idPlaylist");
		int playlistId;
		try {
			playlistId = Integer.parseInt(idPlaylist);
		} catch (NumberFormatException e) {
			playlistId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action list
				case "list":
					// Récupère la liste des playlists de l'utilisateur
					getPlaylistsFunction(view, userId, orderBy, dir);

					// Redirection
					redirectToYouTubePlaylistsJSP(request, response, view);
					break;
				// Si action ajouter
				case "add":
					// Redirection vers la JSP d'ajout
					redirectToNewYouTubePlaylistJSP(request, response, view);
					break;
				// Si action modifier
				case "update":
					// Récupère la playlist
					boolean exist = getPlaylistFunction(view, playlistId, userId, orderBy, dir);
					if (exist) {
						// Redirige vers la JSP de modification
						redirectToUpdateYouTubePlaylistJSP(request, response, view);
					} else {
						// Redirection vers la liste des fichiers
						getPlaylistsFunction(view, userId, null, null);
						redirectToYouTubePlaylistsJSP(request, response, view);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la playlist
					deleteFunction(request, view, playlistId, userId);

					// Récupère la liste des playlists de l'utilisateur
					getPlaylistsFunction(view, userId, null, null);

					// Redirection
					redirectToYouTubePlaylistsJSP(request, response, view);
					break;
				// Si action non reconnu
				default:
					// Redirection
					redirectToHome(request, response);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			redirectToHome(request, response);
		}
	}

	/**
	 * Traitement d'ajout / modification d'un formulaire d'une playlist
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Playlists Servlet [POST] -->");

		// Récupération de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère du paramètre action
		String action = request.getParameter("action");
		String title = new String(request.getParameter("titlePlaylist").trim().getBytes("ISO-8859-1"), "UTF-8");
		String type = new String(request.getParameter("typePlaylist").trim().getBytes("ISO-8859-1"), "UTF-8");
		String active = request.getParameter("checkbox_active");
		String idPlaylist = request.getParameter("idPlaylist");
		int playlistId;
		try {
			playlistId = Integer.parseInt(idPlaylist);
		} catch (NumberFormatException e) {
			playlistId = 0;
		}

		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					logger.info("Tentative d'ajout d'une playlist en cours ..");

					// Essaye d'ajouter la playlist
					addFunction(request, user.getId(), title, type);

					// Redirige vers la page de liste de playlist
					redirectToThisServletWithListAfterPost(request, response);
					break;
				// Si action amodifier
				case "update":
					logger.info("Tentative de modification d'une playlist en cours ..");

					// Essaye de modifier la playlist
					updateFunction(request, playlistId, user.getId(), active, title, type);

					// Redirige vers la page de liste de playlist
					redirectToThisServletWithUpdateAfterPost(request, response, playlistId);
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Récupère la liste des playlists de l'utilisateur et les charge dans la view, ou erreur si besoin
	 * 
	 * @param view
	 * @param userId
	 * @param orderBy
	 * @param dir
	 */
	private void getPlaylistsFunction(ViewJSP view, int userId, String orderBy, String dir) {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		try {
			// Récupère la liste des playlists de l'utilisateur
			listPlaylist = ytMgr.getPlaylists(userId);

			// Tri la liste en fonction des paramètres
			listPlaylist = ytMgr.playlistOrderBy(listPlaylist, orderBy, dir);

		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", PLAYLIST_ERROR_DB));
		} finally {
			// Ajoute la liste puis la view dans la requête
			view.addAttributeToList(new ViewAttribut("listPlaylist", listPlaylist));
		}
	}

	/**
	 * Récupère la playlist de l'utilisateur, renvoi un boolean si elle existe et la charge dans la view, ou erreur si besoin
	 * 
	 * @param view
	 * @param playlistId
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @return boolean
	 */
	private boolean getPlaylistFunction(ViewJSP view, int playlistId, int userId, String orderBy, String dir) {
		boolean exist = false;
		YouTubePlaylist playlist = null;
		List<YouTubeVideo> listVideo = null;
		try {
			// Récupère la playlist de l'utilisateur
			playlist = ytMgr.getPlaylist(playlistId, userId);
			// Récupère la liste des vidéos de la playlist
			listVideo = ytMgr.getVideos(playlistId);

			// Tri la liste en fonction des paramètres
			listVideo = ytMgr.videoOrderBy(listVideo, orderBy, dir);

			exist = true;
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", PLAYLIST_ERROR_DB));
		} finally {
			// Ajoute la liste dans la view
			view.addAttributeToList(new ViewAttribut("playlist", playlist));
			// Ajoute les vidéos dans la view
			view.addAttributeToList(new ViewAttribut("listVideo", listVideo));
		}
		return exist;
	}

	/**
	 * Ajoute la playlist à l'utilisateur, charge le message succes/erreur
	 * 
	 * @param request
	 * @param userId
	 * @param title
	 * @param type
	 */
	private void addFunction(HttpServletRequest request, int userId, String title, String type) {
		try {
			// Ajoute la playlist
			ytMgr.addPlaylist(userId, title, type);

			// Ajoute le message d'ajout avec succès
			request.getSession().setAttribute("success", "La playlist a été correctement ajoutée");
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", PLAYLIST_ERROR_DB);
		}
	}

	/**
	 * Met à jour la playlist de l'utilisateur, charge le message succes/erreur
	 * 
	 * @param request
	 * @param playlistId
	 * @param userId
	 * @param title
	 * @param type
	 */
	private void updateFunction(HttpServletRequest request, int playlistId, int userId, String activeStr, String title, String type) {
		try {
			// Détermine si la playlist doit être activée ou désactivée
			boolean active = false;
			if (activeStr != null && activeStr.equals("on")) {
				active = true;
			}

			// Met à jour la playlist
			ytMgr.updatePlaylist(playlistId, userId, active, title, type);

			// Ajoute le message d'ajout avec succès
			request.getSession().setAttribute("success", "Modifications enregistrées");
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", PLAYLIST_ERROR_DB);
		}
	}

	/**
	 * Récupère la playlist selon son ID et essaye de la supprimer de la base de donnée et charge erreur dans la view si besoin
	 * 
	 * @param request
	 * @param view
	 * @param playlistId
	 * @param userId
	 */
	private void deleteFunction(HttpServletRequest request, ViewJSP view, int playlistId, int userId) {
		try {
			// Supprime la playlist
			ytMgr.deletePlaylist(playlistId, userId);

			// Ajoute le message de suppression avec succès
			view.addAttributeToList(new ViewAttribut("success", "La playlist a été correctement supprimée"));
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", PLAYLIST_ERROR_DB));
		}
	}

	/**
	 * Redirige la requête vers la servlet Home
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/home");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Youtube Playlists
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlaylistsJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> YouTube Playlists JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlists.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP New Youtube Playlist
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNewYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> New YouTube Playlist JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlist_new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Update Youtube Playlist
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToUpdateYouTubePlaylistJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Update YouTube Playlist JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/playlist_update.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers cette même servlet avec action list en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletWithListAfterPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString() + "?action=list&order-by=date&dir=desc");
	}

	/**
	 * Redirige la requête vers cette même servlet avec action update en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisServletWithUpdateAfterPost(HttpServletRequest request, HttpServletResponse response, int playlistId)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(
				request.getRequestURL().toString() + "?action=update&idPlaylist=" + String.valueOf(playlistId) + "&order-by=date&dir=desc");
	}

}
