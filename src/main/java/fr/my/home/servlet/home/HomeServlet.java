package fr.my.home.servlet.home;

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

/**
 * Servlet qui prends en charge la gestion de l'accueil
 * 
 * @author Jonathan
 * @version 1.2
 * @since 26/04/2018
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(HomeServlet.class);

	/**
	 * Redirection vers la page d'accueil
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Home Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToHomeJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Home
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToHomeJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Home JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/home/home.jsp");
		dispatcher.forward(request, response);
	}

}
