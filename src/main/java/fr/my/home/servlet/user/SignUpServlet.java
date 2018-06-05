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
 * Servlet qui prends en charge la gestion de l'inscription d'un nouvel utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 26/04/2018
 */
@WebServlet("/register")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(SignUpServlet.class);

	/**
	 * Attributs
	 */
	private static final String INSCRIPTION_ERROR_DB = Settings.getStringProperty("error_db");
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public SignUpServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page d'inscription
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sign Up Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Test de la connection à la base de données
		boolean databaseOnline = DatabaseAccess.getInstance().testConnection();
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Récupère et ajoute dans la view l'attribut erreur si il existe et surcharge si problème de base de données
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		if (!databaseOnline) {
			error = INSCRIPTION_ERROR_DB;
		}
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToRegisterJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire d'inscription
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Sign Up Servlet [POST] -->");
		logger.info("Inscription en cours ..");

		// Récupération des informations saisies dans le formulaire
		String username = new String(request.getParameter("user_name").trim().getBytes("ISO-8859-1"), "UTF-8");
		String password = new String(request.getParameter("user_pass").trim().getBytes("ISO-8859-1"), "UTF-8");
		String confirmPassword = new String(request.getParameter("user_confirm_pass").trim().getBytes("ISO-8859-1"), "UTF-8");
		String email = request.getParameter("user_email");
		String reCaptcha = request.getParameter("g-recaptcha-response");

		// Test la validation du formulaire
		try {
			// Tentative d'inscription de l'utilisateur via login/password/confirm_password/email/reCaptcha
			User user = usersMgr.checkSignUp(username, password, confirmPassword, email, reCaptcha);
			logger.info("Utilisateur < " + user.getName() + " > inscrit");

			// Redirection vers la page de confirmation d'inscription
			redirectToRegisterOKJSP(request, response);
		} catch (FonctionnalException | TechnicalException ex) {
			// Si inscription impossible
			logger.error(ex.getMessage());
			request.getSession().setAttribute("error", ex.getMessage());

			// Redirection GET
			doGet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Register
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRegisterJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Register JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/register.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Confirmation
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToRegisterOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la JSP
		logger.info(" --> Register OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/register/register_ok.jsp");
		dispatcher.forward(request, response);
	}

}
