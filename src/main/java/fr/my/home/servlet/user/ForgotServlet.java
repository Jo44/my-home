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

import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.UsersManager;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion de récupération des identifiants d'un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 27/04/2018
 */
@WebServlet("/forgot")
public class ForgotServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ForgotServlet.class);

	/**
	 * Attributs
	 */
	private static final String RECUPERATION_ERROR_DB = Settings.getStringProperty("error_db");
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public ForgotServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page de récupération des identifiants de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Forgot Servlet [GET] -->");
		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Test de la connection à la base de données
		boolean databaseOnline = DatabaseAccess.getInstance().testConnection();
		view.addAttributeToList(new ViewAttribut("databaseOnline", databaseOnline));

		// Récupère et ajoute dans la view l'attribut erreur si il existe et surcharge si problème de base de données
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		if (!databaseOnline) {
			error = RECUPERATION_ERROR_DB;
		}
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToForgotJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de l'email de récupération
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Forgot Servlet [POST] -->");
		logger.info("Récupération des identifiants en cours ..");

		// Récupération des informations saisies dans le formulaire
		String email = request.getParameter("user_email");

		// Test la validation du formulaire
		try {
			// Tentative de réinitialisation de l'utilisateur via email
			usersMgr.checkReinitRequest(email.trim());

			// Redirection
			redirectToForgotOKJSP(request, response);
		} catch (FonctionnalException | TechnicalException ex) {
			// Si récupération impossible
			logger.error(ex.getMessage());
			request.getSession().setAttribute("error", ex.getMessage());

			// Redirection GET
			doGet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Forgot
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToForgotJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Forgot JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/forgot.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Forgot OK
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToForgotOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> Forgot OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/forgot_ok.jsp");
		dispatcher.forward(request, response);
	}

}
