package fr.my.home.servlet.youtube;

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

import fr.my.home.bean.User;
import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.YouTubeManager;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion du player YouTube (récupère les vidéos des playlists actives de l'utilisateur)
 * 
 * @author Jonathan
 * @version 1.2
 * @since 05/05/2018
 */
@WebServlet("/youtube_player")
public class PlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(PlayerServlet.class);

	/**
	 * Attributs
	 */
	private static final String PLAYER_ERROR_DB = Settings.getStringProperty("error_db");
	private YouTubeManager ytMgr;

	/**
	 * Constructeur
	 */
	public PlayerServlet() {
		super();
		// Initialisation du manager
		ytMgr = new YouTubeManager();
	}

	/**
	 * Redirection vers la page du lecteur de la playlist
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> YouTube Player Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		request.getSession().removeAttribute("error");

		// Récupère l'utilisateur en session
		User user = (User) request.getSession().getAttribute("user");

		// Récupère la liste des vidéos des playlists actives de l'utilisateur
		getVideosFunction(view, user.getId());

		// Redirection
		redirectToYouTubePlayerJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Récupère la liste des vidéos 'actives' et les charge dans la view, ou erreur si besoin
	 * 
	 * @param view
	 * @param userId
	 */
	private void getVideosFunction(ViewJSP view, int userId) {
		try {
			// Récupère la liste des vidéos 'actives'
			List<YouTubeVideo> listVideo = ytMgr.getVideosFromPlaylists(userId);

			// Charge la liste des vidéos dans la view
			view.addAttributeToList(new ViewAttribut("listVideo", listVideo));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("erreur", PLAYER_ERROR_DB));
		}
	}

	/**
	 * Redirige la requête vers la JSP Youtube Player
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToYouTubePlayerJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> YouTube Player JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/youtube/player.jsp");
		dispatcher.forward(request, response);
	}

}
