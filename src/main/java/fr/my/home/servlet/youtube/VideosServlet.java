package fr.my.home.servlet.youtube;

import java.io.IOException;

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
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion des vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.2
 * @since 04/05/2018
 */
@WebServlet("/youtube_videos")
public class VideosServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(VideosServlet.class);

	/**
	 * Attributs
	 */
	private static final String VIDEO_ERROR_DB = Settings.getStringProperty("error_db");
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public VideosServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	/**
	 * Redirection vers l'ajout, la modification ou la suppression de vidéo
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [GET] -->");

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
		String idVideo = request.getParameter("idVideo");
		int videoId;
		try {
			videoId = Integer.parseInt(idVideo);
		} catch (NumberFormatException e) {
			videoId = 0;
		}

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					// Redirige vers la JSP d'ajout d'une vidéo
					addVideo(request, response, view, playlistId, userId);
					break;
				// Si action modifier
				case "update":
					// Redirige vers la JSP de modification d'une vidéo
					updateVideo(request, response, view, videoId, playlistId, userId);
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer la vidéo
					deleteFunction(request, videoId, playlistId, userId);

					// Redirection
					redirectToPlaylistServletWithUpdateAfterPost(request, response, playlistId);
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
	 * Traitement d'ajout/modification d'un formulaire d'une vidéo
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Videos Servlet [POST] -->");

		// Récupération de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupère du paramètre action
		String action = request.getParameter("action");
		String urlVideo = new String(request.getParameter("urlVideo").trim().getBytes("ISO-8859-1"), "UTF-8");
		String artist = new String(request.getParameter("artistVideo").trim().getBytes("ISO-8859-1"), "UTF-8");
		String title = new String(request.getParameter("titleVideo").trim().getBytes("ISO-8859-1"), "UTF-8");
		String duration = new String(request.getParameter("durationVideo").trim().getBytes("ISO-8859-1"), "UTF-8");
		String idPlaylist = request.getParameter("idPlaylist");
		int playlistId;
		try {
			playlistId = Integer.parseInt(idPlaylist);
		} catch (NumberFormatException e) {
			playlistId = 0;
		}
		String idVideo = request.getParameter("idVideo");
		int videoId;
		try {
			videoId = Integer.parseInt(idVideo);
		} catch (NumberFormatException e) {
			videoId = 0;
		}

		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action ajouter
				case "add":
					logger.info("Tentative d'ajout d'une vidéo en cours ..");

					// Essaye d'ajouter la vidéo
					addFunction(request, playlistId, user.getId(), urlVideo, artist, title, duration);

					// Redirige vers la page de liste des vidéos
					redirectToPlaylistServletWithUpdateAfterPost(request, response, playlistId);
					break;
				// Si action modifier
				case "update":
					logger.info("Tentative de modification d'une playlist en cours ..");

					// Essaye de modifier la vidéo
					updateFunction(request, videoId, playlistId, user.getId(), urlVideo, artist, title, duration);

					// Redirige vers la page de liste des vidéos
					redirectToPlaylistServletWithUpdateAfterPost(request, response, playlistId);
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
	 * Ajout la vidéo à la playlist, charge le message succes/erreur
	 * 
	 * @param request
	 * @param playlistId
	 * @param userId
	 * @param urlVideo
	 * @param artist
	 * @param title
	 * @param duration
	 */
	private void addFunction(HttpServletRequest request, int playlistId, int userId, String urlVideo, String artist, String title, String duration) {
		try {
			// Vérifie si la playlist contenant la vidéo appartient bien à l'utilisateur
			boolean valid = ytMgr.checkPlaylist(playlistId, userId);

			// Si la playlist appartient à l'utilisateur
			if (valid) {
				// Essaye d'ajouter la vidéo
				ytMgr.addVideo(playlistId, urlVideo, artist, title, duration);

				// Ajoute le message d'ajout avec succès
				request.getSession().setAttribute("success", "La vidéo a été correctement ajoutée");
			} else {
				request.getSession().setAttribute("error", "La playlist n'existe pas pour cette vidéo");
			}
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", VIDEO_ERROR_DB);
		}
	}

	/**
	 * Met à jour la vidéo d'une playlist, charge le message succes/erreur
	 * 
	 * @param request
	 * @param videoId
	 * @param playlistId
	 * @param userId
	 * @param urlVideo
	 * @param artist
	 * @param title
	 * @param duration
	 */
	private void updateFunction(HttpServletRequest request, int videoId, int playlistId, int userId, String urlVideo, String artist, String title,
			String duration) {
		try {
			// Vérifie si la playlist contenant la vidéo appartient bien à l'utilisateur
			boolean valid = ytMgr.checkPlaylist(playlistId, userId);

			// Si la playlist appartient à l'utilisateur
			if (valid) {
				// Essaye de modifier la vidéo
				ytMgr.updateVideo(videoId, playlistId, urlVideo, artist, title, duration);

				// Ajoute le message de modification avec succès
				request.getSession().setAttribute("success", "La vidéo a été correctement modifiée");
			} else {
				// Charge l'erreur dans la view
				request.getSession().setAttribute("error", "La playlist n'existe pas pour cette vidéo");
			}
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", VIDEO_ERROR_DB);
		}
	}

	/**
	 * Vérifie si la playlist appartient bien à l'utilisateur et renvoi vers la JSP d'ajout ou vers servlet playlist avec message d'erreur
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param playlistId
	 * @param userId
	 * @throws IOException
	 * @throws ServletException
	 */
	private void addVideo(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int playlistId, int userId)
			throws IOException, ServletException {
		try {
			// Vérifie si la playlist contenant la vidéo appartient bien à l'utilisateur
			boolean valid = ytMgr.checkPlaylist(playlistId, userId);

			// Si la playlist appartient à l'utilisateur
			if (valid) {
				// Charge l'ID de la playlist dans la view
				view.addAttributeToList(new ViewAttribut("idPlaylist", playlistId));

				// Redirection vers la JSP New
				redirectToNewYouTubeVideoJSP(request, response, view);
			} else {
				// Charge l'erreur dans la session
				request.setAttribute("error", "La playlist n'existe pas pour cette vidéo");

				// Redirection vers la Servlet pour retour list
				redirectToListPlaylistServlet(request, response, view);
			}
		} catch (TechnicalException tex) {
			// Charge l'erreur dans la view
			view.addAttributeToList(new ViewAttribut("error", VIDEO_ERROR_DB));

			// Redirection vers la Servlet pour retour list
			redirectToListPlaylistServlet(request, response, view);
		}
	}

	/**
	 * Vérifie si la playlist appartient bien à l'utilisateur et renvoi vers la JSP de modification ou vers servlet playlist avec message d'erreur
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param videoId
	 * @param playlistId
	 * @param userId
	 * @throws IOException
	 * @throws ServletException
	 */
	private void updateVideo(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int videoId, int playlistId, int userId)
			throws IOException, ServletException {
		try {
			// Vérifie si la playlist contenant la vidéo appartient bien à l'utilisateur
			boolean valid = ytMgr.checkPlaylist(playlistId, userId);

			// Si la playlist appartient à l'utilisateur
			if (valid) {
				// Récupère la video
				YouTubeVideo video = ytMgr.getVideo(videoId, playlistId);

				// Charge l'ID de la playlist dans la view
				view.addAttributeToList(new ViewAttribut("idPlaylist", playlistId));

				// Charge la vidéo dans la view
				view.addAttributeToList(new ViewAttribut("video", video));

				// Redirection vers la JSP New
				redirectToUpdateYouTubeVideoJSP(request, response, view);
			} else {
				// Charge l'erreur dans la session
				request.setAttribute("error", "La playlist n'existe pas pour cette vidéo");

				// Redirection vers la Servlet pour retour list
				redirectToListPlaylistServlet(request, response, view);
			}
		} catch (FonctionnalException fex) {
			// Charge l'erreur dans la view
			view.addAttributeToList(new ViewAttribut("error", "La vidéo n'existe pas"));

			// Redirection vers la Servlet pour retour list
			redirectToListPlaylistServlet(request, response, view);
		} catch (TechnicalException tex) {
			// Charge l'erreur dans la view
			view.addAttributeToList(new ViewAttribut("error", VIDEO_ERROR_DB));

			// Redirection vers la Servlet pour retour list
			redirectToListPlaylistServlet(request, response, view);
		}
	}

	/**
	 * Vérifie si la playlist appartient bien à l'utilisateur et charge succes/erreur en session
	 * 
	 * @param request
	 * @param videoId
	 * @param playlistId
	 * @param userId
	 */
	private void deleteFunction(HttpServletRequest request, int videoId, int playlistId, int userId) {
		try {
			// Vérifie si la playlist contenant la vidéo appartient bien à l'utilisateur
			boolean valid = ytMgr.checkPlaylist(playlistId, userId);

			// Si la playlist appartient à l'utilisateur
			if (valid) {
				// Essaye de supprimer la vidéo
				ytMgr.deleteVideo(videoId, playlistId);

				// Ajoute le message de suppression avec succès
				request.getSession().setAttribute("success", "La vidéo a été correctement supprimée");
			} else {
				// Charge l'erreur dans la view
				request.getSession().setAttribute("error", "La playlist n'existe pas pour cette vidéo");
			}
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", VIDEO_ERROR_DB);
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
	 * Redirige la requête vers la JSP New Youtube Video
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToNewYouTubeVideoJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> New YouTube Video JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/video_new.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Update Youtube Video
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToUpdateYouTubeVideoJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Update YouTube Video JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/video_update.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la servlet Playlist
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToListPlaylistServlet(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getContextPath().toString() + "/youtube_playlists?action=list&order-by=date&dir=desc");
	}

	/**
	 * Redirige la requête vers la servlet Playlist
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToPlaylistServletWithUpdateAfterPost(HttpServletRequest request, HttpServletResponse response, int idPlaylist)
			throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getContextPath().toString() + "/youtube_playlists?action=update&idPlaylist=" + String.valueOf(idPlaylist)
				+ "&order-by=date&dir=desc");
	}

}
