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
 * Servlet qui prends en charge la gestion de modification du mot de passe d'un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 28/04/2018
 */
@WebServlet("/reinit")
public class ReinitialisationServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ReinitialisationServlet.class);

	/**
	 * Attributs
	 */
	private static final String RECUPERATION_ERROR_DB = Settings.getStringProperty("error_db");
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public ReinitialisationServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page de modification du mot de passe de l'utilisateur
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Reinitialisation Servlet [GET] -->");

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

		// Récupère le paramètre token et le charge dans la vue
		String reInitToken = request.getParameter("token");
		view.addAttributeToList(new ViewAttribut("token", reInitToken));

		// Redirection
		redirectToReInitPassJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de modification du mot de passe
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Reinitialisation Servlet [POST] -->");
		logger.info("Reinitialisation en cours ..");

		// Récupération des informations saisies dans le formulaire
		String newPassword = new String(request.getParameter("newpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPasswordConfirm = new String(request.getParameter("newpassword-confirm").trim().getBytes("ISO-8859-1"), "UTF-8");
		String reInitToken = request.getParameter("token");

		// Test la validation du formulaire
		try {
			// Vérification du mot de passe
			String hash = usersMgr.checkPass(newPassword, newPasswordConfirm);

			// Si mots de passe différents/invalides ou token vide
			if (hash == null || hash.trim().isEmpty() || reInitToken == null || reInitToken.trim().isEmpty()) {
				String error;
				if (reInitToken == null || reInitToken.trim().isEmpty()) {
					error = "Le token de ré-initialisation est invalide";
				} else {
					error = "Les mots de passe sont différents";
				}
				throw new FonctionnalException(error);
			}

			// Tentative de modification du mot de passe de l'utilisateur
			try {
				usersMgr.checkReInitialisation(hash, reInitToken);

				// Redirection OK
				redirectToReInitPassOKJSP(request, response);
			} catch (FonctionnalException fex) {
				logger.error(fex.getMessage());

				// Redirection Error
				redirectToTryLaterJSP(request, response);
			}
		} catch (FonctionnalException | TechnicalException ex) {
			logger.error(ex.getMessage());
			request.getSession().setAttribute("error", ex.getMessage());

			// Redirection GET
			doGet(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP ReInit Pass
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToReInitPassJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> ReInit Pass JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_pass.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP ReInit Pass OK
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToReInitPassOKJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> ReInit Pass OK JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_pass_ok.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP ReInit Try Later
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToTryLaterJSP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info(" --> ReInit Try Later JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/reinit/reinit_try_later.jsp");
		dispatcher.forward(request, response);
	}

}
