package fr.my.home.servlet.user;

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
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion de la connexion de l'utilisateur
 * 
 * @author Jonathan
 * @version 1.5
 * @since 26/04/2018
 */
@WebServlet("/check")
public class SignInServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SignInServlet.class);

	/**
	 * Attributs
	 */
	private static final String LOG_ERROR_DB = Settings.getStringProperty("error_db");
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public SignInServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page de login
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sign In Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Test de la connection à la base de données
		boolean databaseOnline = DatabaseAccess.getInstance().testConnection();
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Test de récupération du nom d'utilisateur par cookie
		String username = usersMgr.coockieRestore(request, response);

		// Ajout dans la view du nom de l'utilisateur associé au token 'Remember Me'
		// pour simplifier la prochaine connection ('Remember Me') si présent
		view.addAttributeToList(new ViewAttribut("userName", username));

		// Récupère et ajoute dans la view l'attribut erreur si il existe et surcharge si problème de base de données
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		if (!databaseOnline) {
			error = LOG_ERROR_DB;
		}
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToSignInJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire d'authentification
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sign In Servlet [POST] -->");
		logger.info("Authentification en cours ..");

		// Récupération des informations saisies dans le formulaire
		String username = new String(request.getParameter("user_name").trim().getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(request.getParameter("user_pass").trim().getBytes("ISO-8859-1"), "UTF-8");
		String rememberMe = request.getParameter("remember");

		// Récupération de l'utilisateur
		try {
			// Tentative de récupération de l'utilisateur via login/password (et gestion de la fonction 'RememberMe' (cookie))
			User user = usersMgr.checkSignIn(request, response, username, password, rememberMe);
			logger.info("Utilisateur  < " + user.getName() + " > connecté");

			// Ajout de l'utilisateur en session
			request.getSession().setAttribute("user", user);

			// Redirection
			redirectToHome(request, response);
		} catch (FonctionnalException fex) {
			// Les identifiants sont incorrects
			logger.debug(fex.getMessage());
			request.getSession().setAttribute("error", fex.getMessage());

			// Redirection GET
			doGet(request, response);
		} catch (TechnicalException tex) {
			logger.error(LOG_ERROR_DB);
			request.getSession().setAttribute("error", LOG_ERROR_DB);

			// Redirection GET
			doGet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Sign In
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToSignInJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Sign In JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/sign_in.jsp");
		dispatcher.forward(request, response);
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
		response.sendRedirect(request.getContextPath().toString() + "/home");
	}

}
