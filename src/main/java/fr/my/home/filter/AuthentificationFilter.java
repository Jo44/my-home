package fr.my.home.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.User;

/**
 * Filtre qui permet de vérifier si un utilisateur est bien authentifié pour accèder à la ressource demandée
 * 
 * @author Jonathan
 * @version 1.1
 * @since 22/04/2018
 *
 */
public class AuthentificationFilter implements Filter {
	private static final Logger logger = LogManager.getLogger(AuthentificationFilter.class);

	/**
	 * Initialisation
	 */
	@Override
	public void init(FilterConfig fc) throws ServletException {}

	/**
	 * Filter
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hsreq = (HttpServletRequest) request;
		HttpServletResponse hsres = (HttpServletResponse) response;
		HttpSession session = hsreq.getSession(false);
		// Récupère l'url d'accès des servlets ne nécessitant pas de connexion d'utilisateur
		// Servlet de login
		String loginURI = hsreq.getContextPath() + "/check";
		// Servlet de status (brut)
		String statusURI = hsreq.getContextPath() + "/status";
		// Servlet d'inscription d'un compte
		String registerURI = hsreq.getContextPath() + "/register";
		// Servlet de validation d'un compte
		String validationURI = hsreq.getContextPath() + "/validation";
		// Servlet de récupération d'un compte
		String forgotURI = hsreq.getContextPath() + "/forgot";
		// Servlet de modification d'un mot de passe
		String reinitURI = hsreq.getContextPath() + "/reinit";
		// Récupère l'url demandée actuellement
		String requestURI = hsreq.getRequestURI();

		User user = null;
		boolean loggedIn = false;
		boolean loginRequest = requestURI.equals(loginURI);
		boolean statusRequest = requestURI.equals(statusURI);
		boolean registerRequest = requestURI.equals(registerURI);
		boolean validationRequest = requestURI.equals(validationURI);
		boolean forgotRequest = requestURI.equals(forgotURI);
		boolean reinitRequest = requestURI.equals(reinitURI);

		// Détermine si l'utilisateur est présent en session
		if (session != null) {
			user = (User) session.getAttribute("user");
			if (user != null) {
				loggedIn = true;
			}
		}

		// Laisse passer la requête si utilisateur déjà connecté, si url autorisée ou toutes ressources css/js/png/ico
		if (loggedIn || loginRequest || statusRequest || registerRequest || validationRequest || forgotRequest || reinitRequest
				|| requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.endsWith(".png") || requestURI.endsWith(".ico")) {
			// Laisse passer la requête
			chain.doFilter(request, response);
		} else {
			logger.debug("Utilisateur non connecté");
			// Redirige vers la page de login
			hsres.sendRedirect(loginURI);
		}
	}

	/**
	 * Destroy
	 */
	@Override
	public void destroy() {}

}
