package fr.my.home.manager;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.ObjectIPAPI;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.tool.GlobalTools;

/**
 * Manager qui prends en charge la gestion de la localisation
 * 
 * @author Jonathan
 * @version 1.2
 * @since 10/20/2017
 */
public class LocalisationManager {
	private static final Logger logger = LogManager.getLogger(LocalisationManager.class);

	/**
	 * Constructeur
	 */
	public LocalisationManager() {};

	/**
	 * Méthodes
	 */

	/**
	 * Fonction qui traite l'input si besoin et requête le site ip-api.com pour renvoyer un objet IPAPI si possible
	 * 
	 * @param request
	 * @param inputWebsiteIP
	 */
	public void getIPAPIFunction(HttpServletRequest request, String inputWebsiteIP) {
		// Traitement de l'input
		String newInputWebsiteIP = formatLocString(inputWebsiteIP);
		// Si l'input n'est pas vide, récupération de l'objet IPAPI
		if (newInputWebsiteIP != null && !newInputWebsiteIP.isEmpty()) {
			try {
				// Récupère l'ObjectIPAPI à partir d'une requete à IP-API
				// http://ip-api.com/json/INPUT
				ObjectIPAPI objectIPAPI = getObjectIPAPI(newInputWebsiteIP);
				if (objectIPAPI.getStatus().equals("success")) {
					// Si récupération succès, renvoi l'objet dans la requête
					request.getSession().setAttribute("objectIPAPI", objectIPAPI);
				} else {
					throw new FonctionnalException("Impossible de localiser la cible");
				}
			} catch (FonctionnalException fex) {
				logger.error(fex.getMessage());
				request.getSession().setAttribute("error", fex.getMessage());
			}
		} else {
			String error = "La saisie n'est pas valide";
			logger.error(error);
			request.getSession().setAttribute("error", error);
		}
	}

	/**
	 * Formatte le string pour enlever le protocol https/http et le sous domaine www si présents
	 * 
	 * @param inputStr
	 * @return formatStr
	 */
	private String formatLocString(String inputStr) {
		String formatStr = null;
		// Supprime le début (inutile) de l'url saisie
		if (inputStr != null && !inputStr.isEmpty()) {
			if (inputStr.indexOf("https://www.") != -1) {
				formatStr = inputStr.replace("https://www.", "");
			} else if (inputStr.indexOf("http://www.") != -1) {
				formatStr = inputStr.replace("http://www.", "");
			} else if (inputStr.indexOf("https://") != -1) {
				formatStr = inputStr.replace("https://", "");
			} else if (inputStr.indexOf("http://") != -1) {
				formatStr = inputStr.replace("http://", "");
			} else if (inputStr.indexOf("www.") != -1) {
				formatStr = inputStr.replace("www.", "");
			} else {
				formatStr = inputStr;
			}
		}
		// Supprime la fin (inutile) de l'url saisie
		if (formatStr != null && !formatStr.isEmpty()) {
			if (formatStr.indexOf("/") != -1) {
				formatStr = formatStr.substring(0, formatStr.indexOf("/"));
			}
			// Trim l'url final
			formatStr = formatStr.trim();
		}
		return formatStr;
	}

	/**
	 * Récupère l'objet IP-API à partir de l'input via ip-api.com
	 * 
	 * @param input
	 * @return ObjectIPAPI
	 * @throws FonctionnalException
	 */
	private ObjectIPAPI getObjectIPAPI(String input) throws FonctionnalException {
		ObjectIPAPI objectIPAPI = null;
		try {
			// Récupère le code HTML de la réponse de l'IP-API
			String responseAPI = GlobalTools.getHTML("http://ip-api.com/json/" + input);
			// Parse la réponse JSON en ObjectIPAPI
			objectIPAPI = GlobalTools.getObjectIPAPI(responseAPI);
			if (objectIPAPI == null) {
				throw new FonctionnalException("La saisie n'est pas valide");
			}
			logger.debug("Récupération de l'objet IP-API réussi");
		} catch (FonctionnalException fex) {
			throw fex;
		} catch (IOException e) {
			throw new FonctionnalException("La saisie n'est pas valide");
		}
		return objectIPAPI;
	}

}
