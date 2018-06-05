package fr.my.home.servlet.agenda;

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
 * Servlet qui prends en charge la gestion de l'agenda
 * 
 * @author Jonathan
 * @version 1.1
 * @since 26/04/2018
 */
@WebServlet("/agenda")
public class AgendaServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(AgendaServlet.class);

	/**
	 * Redirection vers la page de l'agenda
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Agenda Servlet [GET] -->");

		// Création de la view renvoyée à la JSP
		ViewJSP view = new ViewJSP();

		// Récupère l'attribut erreur si il existe
		String error = (String) request.getSession().getAttribute("error");
		request.getSession().removeAttribute("error");
		view.addAttributeToList(new ViewAttribut("error", error));

		// Redirection
		redirectToAgendaJSP(request, response, view);
	}

	/**
	 * Redirection
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Redirige la requête vers la JSP Agenda
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToAgendaJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Agenda JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/agenda/agenda.jsp");
		dispatcher.forward(request, response);
	}

}
