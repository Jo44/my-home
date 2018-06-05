package fr.my.home.servlet.localisation;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.ObjectIPAPI;
import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.manager.LocalisationManager;

/**
 * Servlet qui prends en charge la gestion de la localisation d'une IP ou nom de domaine
 * 
 * @author Jonathan
 * @version 1.3
 * @since 26/04/2018
 */
@WebServlet("/localisation")
public class LocalisationServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(LocalisationServlet.class);

	/**
	 * Attributs
	 */
	private LocalisationManager locMgr;

	/**
	 * Constructeur
	 */
	public LocalisationServlet() {
		super();
		// Initialisation du manager
		locMgr = new LocalisationManager();
	}

	/**
	 * Redirection vers la page de localisation
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Localisation Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Récupère l'objet IPAPI si il existe
		ObjectIPAPI objectIPAPI = (ObjectIPAPI) request.getSession().getAttribute("objectIPAPI");
		request.getSession().removeAttribute("objectIPAPI");
		view.addAttributeToList(new ViewAttribut("objectIPAPI", objectIPAPI));

		// Redirection
		redirectToLocalisationJSP(request, response, view);
	}

	/**
	 * Traitement du formulaire de localisation
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Localisation Servlet [POST] -->");
		logger.info("Traitement du formulaire de localisation d'une Ip ou d'un nom de domaine ..");

		// Récupération du nom de domaine ou IP à localiser
		String input = request.getParameter("websiteIP");

		// Traitement du formulaire et renvoi de l'ObjectIPAPI dans la requête
		locMgr.getIPAPIFunction(request, input);

		// Redirection
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Localisation
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToLocalisationJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view)
			throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Localisation JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/localisation/localisation.jsp");
		dispatcher.forward(request, response);
	}

}
