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
import fr.my.home.tool.Settings;

/**
 * Servlet qui prends en charge la gestion du profil de l'utilisateur
 * 
 * @author Jonathan
 * @version 1.2
 * @since 26/04/2018
 */
@WebServlet("/profil")
public class ProfilServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(ProfilServlet.class);

	/**
	 * Attributs
	 */
	private static final String PROFIL_ERROR_DB = Settings.getStringProperty("error_db");
	private UsersManager usersMgr;

	/**
	 * Constructeur
	 */
	public ProfilServlet() {
		super();
		// Initialisation du manager
		usersMgr = new UsersManager();
	}

	/**
	 * Redirection vers la page du profil
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Profil Servlet [GET] -->");

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

		// Récupère le paramètre de la requête
		String action = request.getParameter("action");

		// Redirection
		if (action != null && action.equals("update")) {
			// Si action modifier
			redirectToUpdateJSP(request, response, view);
		} else {
			// Si action profil
			redirectToProfilJSP(request, response, view);
		}
	}

	/**
	 * Traitement du formulaire de modification du profil
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Profil Servlet [POST] -->");
		logger.info("Tentative de modification du profil en cours ..");

		// Récupération de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		// Récupération des informations saisies dans le formulaire
		String oldPassword = new String(request.getParameter("oldpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPassword = new String(request.getParameter("newpassword").trim().getBytes("ISO-8859-1"), "UTF-8");
		String newPasswordConfirm = new String(request.getParameter("newpassword-confirm").trim().getBytes("ISO-8859-1"), "UTF-8");

		try {
			// Vérifie si la modification du profil est possible fonctionnellement,
			// renvoi newHash si possible, sinon exception fonctionnelle
			String newHash = usersMgr.checkUpdateProfil(user, oldPassword, newPassword, newPasswordConfirm);

			// Met à jour l'utilisateur local
			user.setPass(newHash);

			// Enregistre l'utilisateur en base
			usersMgr.updateProfil(user);

			// Met à jour l'utilisateur de session
			request.getSession().removeAttribute("user");
			request.getSession().setAttribute("user", user);

			// Ajoute le message de succès à la session
			request.getSession().setAttribute("success", "Le mot de passe a été correctement modifié");

			// Redirection vers la page d'affichage
			redirectToThisServletAfterPost(request, response);
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			request.getSession().setAttribute("error", fex.getMessage());

			// Redirige vers la page de modification
			redirectToThisUpdateServletAfterPost(request, response);
		} catch (TechnicalException tex) {
			logger.debug(tex.getMessage());
			request.getSession().setAttribute("error", PROFIL_ERROR_DB);

			// Redirection vers la page d'affichage
			redirectToThisServletAfterPost(request, response);
		}
	}

	/**
	 * Redirige la requête vers la JSP Profil
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToProfilJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Profil JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/profil/profil.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Update
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToUpdateJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Update JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/users/profil/update.jsp");
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

	/**
	 * Redirige la requête vers cette même servlet en update en Get après un Post
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToThisUpdateServletAfterPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Redirige vers la servlet en GET
		response.sendRedirect(request.getRequestURL().toString() + "?action=update");
	}

}
