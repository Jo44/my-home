package fr.my.home.manager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import fr.my.home.bean.ObjectReCaptcha;
import fr.my.home.bean.User;
import fr.my.home.dao.UserDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.GlobalTools;
import fr.my.home.tool.Settings;

/**
 * Manager qui prends en charge la gestion des utilisateurs
 * 
 * @author Jonathan
 * @version 1.3
 * @since 27/04/2018
 */
public class UsersManager {
	private static final Logger logger = LogManager.getLogger(UsersManager.class);

	/**
	 * Attributs
	 */
	private static final String COOKIE_NAME = Settings.getStringProperty("cookie_name");

	private UserDAO userDAO;

	/**
	 * Constructeur
	 */
	public UsersManager() {
		userDAO = new UserDAO();
	};

	/**
	 * Méthodes
	 */

	/**
	 * Connexion
	 */

	/**
	 * Récupération l'utilisateur via Username et Password ou renvoi exception fonctionnelle / technique (et gestion de la fonction 'Remember Me')
	 * 
	 * @param request
	 * @prama response
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User checkSignIn(HttpServletRequest request, HttpServletResponse response, String username, String password, String rememberMe)
			throws FonctionnalException, TechnicalException {
		User user = null;
		String hashpass = null;
		try {
			// Cryptage du mot de passe pour comparaison en base
			hashpass = GlobalTools.hash(password.trim());

			// Vérification du login et mdp en base, renvoi user valide ou exceptions
			user = userDAO.getUser(username.trim(), hashpass);

			// Vérification 'RememberMe'
			if (rememberMe != null && !rememberMe.isEmpty()) {
				// Test la présence d'un cookie
				if (cookieCheck(request) == null) {
					// Création du cookie
					String cookie = createCookie(response);

					// Met à jour le Token 'Remember Me' de l'utilisateur récupéré
					user.setRememberMeToken(cookie);

					// Mise à jour de l'utilisateur dans la base
					userDAO.update(user);

					logger.debug("Création du cookie réussie");
				}
			}
		} catch (FonctionnalException fex) {
			throw fex;
		} catch (TechnicalException tex) {
			throw tex;
		}
		return user;
	}

	/**
	 * Test la présence d'un cookie et retourne le nom de l'utilisateur reconnu
	 * 
	 * @param request
	 * @param response
	 * @return username
	 */
	public String coockieRestore(HttpServletRequest request, HttpServletResponse response) {
		String username = null;
		// Test la présence d'un cookie de connection
		String cookie = cookieCheck(request);

		// Si cookie présent ..
		if (cookie != null && !cookie.isEmpty()) {
			logger.debug("Tentative de récupération par cookie ..");
			try {
				// .. récupère le nom de l'utilisateur
				User user = userDAO.getUserByRememberMeToken(cookie);
				username = user.getName();

				logger.debug("Récupération réussie  -> " + user.getName());
			} catch (FonctionnalException fex) {
				// Supprime le cookie si aucun utilisateur ne correspond
				deleteCookie(request, response);

				logger.debug(fex.getMessage());
			} catch (TechnicalException tex) {
				logger.error("Erreur de Base de Données");
			}
		}
		return username;
	}

	/**
	 * Vérifie la présence d'un cookie enregistré pour le site et renvoi sa valeur si oui, ou null sinon
	 * 
	 * @param request
	 * @return cookieValue
	 */
	private String cookieCheck(HttpServletRequest request) {
		String cookieValue = null;
		// Récupère la liste des cookies
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			// Parmis ces cookies ..
			for (Cookie cookie : cookies) {
				// .. récupère le cookie correspondant à nom de cookie définis
				if (cookie.getName().equals(COOKIE_NAME)) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}

	/**
	 * Génération d'un cookie et renvoi le 'Remember Me' pour association en base avec l'utilisateur
	 * 
	 * @param response
	 * @return rememberMe
	 */
	private String createCookie(HttpServletResponse response) {
		// Génération d'un UUID
		UUID uuid = UUID.randomUUID();
		String rememberMe = uuid.toString();
		// Création d'un cookie
		Cookie cookie = new Cookie(COOKIE_NAME, rememberMe);
		// Valide pour 1 an
		cookie.setMaxAge(365 * 24 * 60 * 60);
		// Ajout du cookie
		response.addCookie(cookie);
		// Retourne le Token 'Remember Me' pour enregistrement en base
		return rememberMe;
	}

	/**
	 * Déconnexion
	 */

	/**
	 * Supprime le cookie et met à jour l'utilisateur en base si possible
	 * 
	 * @param request
	 * @param response
	 * @param user
	 */
	public void signOut(HttpServletRequest request, HttpServletResponse response, User user) {
		// Supprime le cookie si il existe
		deleteCookie(request, response);

		// Met à jour le Token 'Remember Me' de l'utilisateur récupéré
		user.setRememberMeToken(null);

		// Mise à jour de l'utilisateur dans la base
		try {
			userDAO.update(user);
		} catch (FonctionnalException | TechnicalException e) {
			logger.error("Impossible de mettre à jour l'utilisateur pour suppression du cookie");
		}
	}

	/**
	 * Suppression du cookie
	 * 
	 * @param request
	 * @param response
	 */
	public void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
		// Récupère tous les cookies
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			// Si on trouve le cookie de MyHome
			if (cookie.getName().equals(COOKIE_NAME)) {
				// Fait expirer le cookie
				cookie.setMaxAge(0);
				cookie.setValue(null);
				response.addCookie(cookie);
				logger.debug("Cookie supprimé avec succès");
			}
		}
	}

	/**
	 * Inscription
	 */

	/**
	 * Vérifie si l'inscription est possible, puis enregistre en base et envoi le mail de confirmation à l'utilisateur
	 * 
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @param email
	 * @param reCaptcha
	 * @return user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User checkSignUp(String username, String password, String confirmPassword, String email, String reCaptcha)
			throws FonctionnalException, TechnicalException {
		User user = null;
		try {
			// Vérification des paramètres du formulaire d'inscription (+ reCatpcha) et renvoi le hashPass si tout est ok
			String hashPass = checkSignUpParameters(username, password, confirmPassword, email, reCaptcha);

			// Vérification du reCaptcha
			boolean validReCaptcha = checkCaptcha(reCaptcha);

			// Vérification si le login est disponible
			boolean availableLogin = checkAvailableLogin(username);

			// Vérification si l'email est disponible
			boolean availableEmail = checkAvailableEmail(email);

			// Prépare le token de validation pour activation du compte via email
			String validationToken = UUID.randomUUID().toString();

			// Si tout est ok, ajoute l'utilisateur
			if (validReCaptcha && availableLogin && availableEmail) {
				// Créé l'utilisateur (par défaut non activé, et token de validation chargé)
				user = new User(username.trim(), hashPass, email.trim(), null, validationToken, false, null, null, null);

				// Ajoute l'utilisateur en base
				userDAO.add(user);

				// Prépare le contenu de l'email (avec username et validationToken)
				String content = generateVerificationEmailContent(username.trim(), validationToken);

				// Envoi de l'email de confirmation
				GlobalTools.sendEmail(email, "", content);
			} else {
				if (!validReCaptcha) {
					throw new FonctionnalException("Le reCaptcha n'a pas été correctement validé");
				}
				if (!availableLogin) {
					throw new FonctionnalException("Ce login est déjà utilisé");
				}
				if (!availableEmail) {
					throw new FonctionnalException("Cet email est déjà utilisé");
				}
			}
		} catch (FonctionnalException | TechnicalException ex) {
			throw ex;
		}
		return user;
	}

	/**
	 * Vérifie si tous les champs du formulaire d'inscription sont valides (sinon exceptions) et renvoi le password hashé
	 * 
	 * @param username
	 * @param password
	 * @param confirmPassword
	 * @param email
	 * @param reCaptcha
	 * @return hashPass
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public String checkSignUpParameters(String username, String password, String confirmPassword, String email, String reCaptcha)
			throws FonctionnalException, TechnicalException {
		String hashPass = null;
		// Si tous les champs sont renseignés
		if (username != null && password != null && confirmPassword != null && email != null && reCaptcha != null) {
			// Si les champs respectent le nombre de caractère min/max
			if (username.trim().length() > 2 && username.trim().length() < 31 && password.trim().length() > 5 && password.trim().length() < 31
					&& confirmPassword.trim().length() > 5 && confirmPassword.trim().length() < 31 && email.trim().length() > 5
					&& email.trim().length() < 51 && reCaptcha.trim().length() > 0) {
				// Si les mots de passe correspondent bien
				if (password.trim().equals(confirmPassword.trim())) {
					try {
						// Hashage du mot de passe
						hashPass = GlobalTools.hash(password.trim());
					} catch (FonctionnalException fex) {
						throw fex;
					}
				} else {
					throw new FonctionnalException("Les mots de passe saisis sont différents.");
				}
			} else {
				if (username.trim().length() <= 2 && username.trim().length() >= 31 && password.trim().length() <= 5 && password.trim().length() >= 31
						&& confirmPassword.trim().length() <= 5 && confirmPassword.trim().length() >= 31 && email.trim().length() <= 5
						&& email.trim().length() >= 51) {
					throw new FonctionnalException("La longueur des champs saisis n'est pas correcte.");
				} else {
					throw new FonctionnalException("La vérification du reCaptcha est manquante.");
				}
			}
		} else {
			// reCaptcha manquant
			throw new FonctionnalException("Le reCaptcha n'est pas présent.");
		}
		return hashPass;
	}

	/**
	 * Vérifie si le reCaptcha récupéré du formulaire d'inscription a été correctement validé à partir d'une requête POST et de la lecture de sa
	 * réponse auprès de Google
	 * 
	 * @param reCaptcha
	 * @return boolean
	 */
	public boolean checkCaptcha(String reCaptcha) {
		boolean valid = false;

		// URL de vérification : 'https://www.google.com/recaptcha/api/siteverify'
		// Clef privé : '****'
		// Paramètre reCaptcha : 'g-recaptcha-response'

		// HashMap regroupant les headers et le body du POST de vérification reCaptcha
		HashMap<String, String> hmap = new HashMap<String, String>();

		// Ajoute les élements à la HashMap
		hmap.put("request-url", "https://www.google.com/recaptcha/api/siteverify");
		hmap.put("user-agent", "Mozilla/5.0");
		hmap.put("accept-language", "en-US,en;q=0.5");
		// Construis le contenu du POST
		String postParams = "secret=****&response=" + reCaptcha;
		hmap.put("content", postParams);

		try {
			// Récupère le code HTML de la réponse de reCaptcha
			String reponseAPI = GlobalTools.postHTML(hmap);

			// Parse la réponse JSON en ObjectReCaptcha
			ObjectReCaptcha objectReCaptcha = GlobalTools.getObjectReCaptcha(reponseAPI);
			if (objectReCaptcha != null) {
				if (objectReCaptcha.isSuccess()) {
					valid = true;
				}
				logger.info(objectReCaptcha.toString());
			}
		} catch (IOException | JsonSyntaxException e) {
			logger.error("Impossible de vérifier le reCaptcha");
		}

		if (valid) {
			logger.info("Vérification reCaptcha V2 effectuée avec succès");
		} else {
			logger.error("Vérification reCaptcha V2 échouée !");
		}

		return valid;
	}

	/**
	 * Vérifie si le username est disponible pour inscription en base
	 * 
	 * @param username
	 * @return boolean
	 */
	public boolean checkAvailableLogin(String username) {
		boolean valid = false;
		try {
			userDAO.getUserByUsername(username);
			valid = false;
			logger.error("Le login n'est pas disponible");
		} catch (FonctionnalException fex) {
			valid = true;
			logger.info("Le login est disponible");
		} catch (TechnicalException tex) {
			valid = false;
			logger.error("Impossible de déterminer si le login est disponible");
		}
		return valid;
	}

	/**
	 * Vérifie si l'email est disponible pour inscription en base
	 * 
	 * @param email
	 * @return boolean
	 */
	public boolean checkAvailableEmail(String email) {
		boolean valid = false;
		try {
			userDAO.getUserByEmail(email);
			valid = false;
			logger.error("L'email n'est pas disponible");
		} catch (FonctionnalException fex) {
			valid = true;
			logger.info("L'email est disponible");
		} catch (TechnicalException tex) {
			valid = false;
			logger.error("Impossible de déterminer si l'email est disponible");
		}
		return valid;
	}

	/**
	 * Génère l'email de confirmation d'inscription d'un utilisateur à partir de son nom et de son token de validation
	 * 
	 * @param username
	 * @param validationToken
	 * @return string
	 */
	public String generateVerificationEmailContent(String username, String validationToken) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tSalut ");
		sb.append(username);
		sb.append(" o/\n\nMerci pour votre inscription. ");
		sb.append("Votre compte a été créé et doit maintenant être activé pour être utilisé.");
		sb.append("\nIl vous suffit pour cela de cliquer sur le lien suivant ou le lien peut ");
		sb.append("également être copié à partir de ce message et collé dans le navigateur Web :");
		sb.append("\n\n\t****/validation?token=");
		sb.append(validationToken);
		sb.append("\n\nLe lien est valide pendant 72h. Une fois ce délai écoulé, vous devrez ré-effectuer une nouvelle inscription.");
		sb.append("\nAprès l'activation, vous serez automatiquement redirigé vers la page de connexion. ");
		sb.append("\nSi vous rencontrez des soucis d'activation ou autres, n'hésitez pas à contacter ");
		sb.append("l'administrateur : ****@****.com");
		sb.append("\n\nP.S: Ici, aucune donnée n'est partagée vers des sites tiers et aucune donnée 'sensible' ");
		sb.append("n'est stockée.\nVous ne trouverez également aucune publicité ni spam de newsletter dans ");
		sb.append("votre boîte mail !");
		sb.append("\n\n\tBonne navigation !");
		return sb.toString();
	}

	/**
	 * Récupère l'utilisateur à partir du token de validation, vérifie que son token est encore valide, puis met à jour l'utilisateur
	 * 
	 * @param validationToken
	 * @return boolean
	 */
	public boolean validateUser(String validationToken) {
		boolean valid = false;
		try {
			// Récupère l'utilisateur via son token de validation
			User user = userDAO.getUserByValidationToken(validationToken);

			// Si l'utilisateur n'est pas actif
			if (user != null && !user.isActive()) {
				// Vérifie si le token est expiré (+ de 72h)
				// now > limitDate
				// limitDate = validationTokenDate + 72 heures
				Calendar cal = Calendar.getInstance();
				Timestamp now = new Timestamp(cal.getTimeInMillis());
				Timestamp validationTokenDate = user.getInscriptionDate();
				cal.setTimeInMillis(validationTokenDate.getTime());
				cal.add(Calendar.DAY_OF_MONTH, 3);
				Timestamp limitDate = new Timestamp(cal.getTimeInMillis());
				if (now.after(limitDate)) {
					throw new FonctionnalException("Le token a expiré");
				}

				// Met à jour le paramètre 'active'
				user.setActive(true);

				// Met à jour l'utilisateur
				userDAO.update(user);
				valid = true;
			} else {
				valid = true;
			}
		} catch (FonctionnalException | TechnicalException ex) {
			valid = false;
		}
		return valid;
	}

	/**
	 * Ré-initialisation
	 */

	/**
	 * Récupère l'utilisateur demandant une ré-initialisation des identifiants, génére un token de réinitialisation et un timestamp de création du
	 * token, les enregistre en base puis lui envoi un mail de récupération
	 * 
	 * @param email
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void checkReinitRequest(String email) throws FonctionnalException, TechnicalException {
		try {
			// Récupère l'utilisateur
			User user = userDAO.getUserByEmail(email);

			// Génère un token de ré-initialisation du mot de passe
			String reInitToken = UUID.randomUUID().toString();

			// Génère un timestamp pour la création du token de ré-initialisation
			Timestamp reInitDate = new Timestamp(Calendar.getInstance().getTimeInMillis());

			// Met à jour l'utilisateur avec des données de ré-initialisation
			user.setReInitToken(reInitToken);
			user.setReInitDate(reInitDate);

			// Met à jour l'utilisateur en base
			userDAO.update(user);

			// Génère le mail de récupération (avec en paramètre le nom et le token de ré-initialisation)
			String content = generateReinitEmailContent(user.getName(), user.getReInitToken());

			// Envoi de l'email de récupération
			GlobalTools.sendEmail(email, "", content);
		} catch (FonctionnalException | TechnicalException ex) {
			throw ex;
		}
	}

	/**
	 * Génère l'email de récupération des identifiants d'un utilisateur à partir de son nom
	 * 
	 * @param username
	 * @param reInitToken
	 * @return string
	 */
	public String generateReinitEmailContent(String username, String reInitToken) {
		StringBuilder sb = new StringBuilder();
		sb.append("\tSalut o/");
		sb.append("\n\nVous avez demandé une récupération de vos identifiants. ");
		sb.append("\n\n\t\tIdentifiant :\t");
		sb.append(username);
		sb.append("\n\nSi vous désirez demander une ré-initialisation de votre mot de passe, il vous suffit ");
		sb.append("pour cela de cliquer sur le lien suivant ou le lien peut également");
		sb.append("\nêtre copié à partir de ce message et collé dans le navigateur Web :");
		sb.append("\n\n\t****/reinit?token=");
		sb.append(reInitToken);
		sb.append("\n\nLe lien est valide 24h. Une fois ce délai écoulé, si vous n'avez toujours pas modifié ");
		sb.append("votre mot de passe, vous devrez refaire une demande de ré-initialisation si nécessaire.");
		sb.append("\nSi vous rencontrez des soucis de ré-initialisation ou autres, n'hésitez pas à contacter ");
		sb.append("l'administrateur : ****@****.com");
		sb.append("\n\n\tBonne navigation !");
		return sb.toString();
	}

	/**
	 * Récupère l'utilisateur à partir du token de ré-initialisation, vérifie que son token est encore valide, puis met à jour l'utilisateur ou renvoi
	 * exception fonctionnelle si pas possible
	 * 
	 * @param hashPass
	 * @param reInitToken
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void checkReInitialisation(String hashPass, String reInitToken) throws FonctionnalException, TechnicalException {
		try {
			// Récupère l'utilisateur associé au token de ré-initialisation
			User user = userDAO.getUserByReInitToken(reInitToken);

			// Vérifie si le token est expiré (+ de 24h)
			// now > limitDate
			// limitDate = reInitTokenDate + 24 heures
			Calendar cal = Calendar.getInstance();
			Timestamp now = new Timestamp(cal.getTimeInMillis());
			Timestamp reInitTokenDate = user.getReInitDate();
			cal.setTimeInMillis(reInitTokenDate.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			Timestamp limitDate = new Timestamp(cal.getTimeInMillis());
			if (now.after(limitDate)) {
				throw new FonctionnalException("Le token a expiré");
			}

			// Met à jour l'utilisateur en base
			user.setPass(hashPass);
			user.setReInitDate(null);
			user.setReInitToken(null);
			userDAO.update(user);

		} catch (FonctionnalException | TechnicalException e) {
			throw e;
		}
	}

	/**
	 * Vérifie si les 2 mots de passe sont bien compris entre 6 et 30 caractères, les compare, puis renvoi le hash du pass ou renvoi null si problème
	 * 
	 * @param pass
	 * @param confirmPass
	 * @return hash
	 */
	public String checkPass(String pass, String confirmPass) {
		String hash = null;
		if (pass != null && confirmPass != null && pass.trim().length() > 5 && pass.trim().length() < 31 && confirmPass.trim().length() > 5
				&& confirmPass.trim().length() < 31 && pass.trim().equals(confirmPass.trim())) {
			try {
				hash = GlobalTools.hash(pass.trim());
			} catch (FonctionnalException fex) {
				hash = null;
			}
		}
		return hash;
	}

	/**
	 * Profil
	 */

	/**
	 * Vérifie si la modification du profil est possible en fonction de l'utilisateur en cours et des champs du formulaire et renvoi newHash pour
	 * modification en base, ou sinon une exception fonctionnelle qui spécifie la cause de l'erreur
	 * 
	 * @param user
	 * @param oldPassword
	 * @param newPassword1
	 * @param newPassword2
	 * @return newHash
	 * @throws FonctionnalException
	 */
	public String checkUpdateProfil(User user, String oldPassword, String newPassword1, String newPassword2) throws FonctionnalException {
		String oldHash = null;
		String newHash = null;
		// Vérification si nouveau mot de passe est valide
		// Si tous les champs sont renseignés
		if (user != null && oldPassword != null && newPassword1 != null && newPassword2 != null) {
			// Si les champs respectent le nombre de caractère min/max
			if (oldPassword.trim().length() > 5 && oldPassword.trim().length() < 31 && newPassword1.trim().length() > 5
					&& newPassword1.trim().length() < 31 && newPassword2.trim().length() > 5 && newPassword2.trim().length() < 31) {
				// Si nouveaux mots de passe correspondent bien
				if (newPassword1.trim().equals(newPassword2.trim())) {
					// Hashage de l'ancien mot de passe
					try {
						oldHash = GlobalTools.hash(oldPassword.trim());
						newHash = GlobalTools.hash(newPassword1.trim());
					} catch (FonctionnalException fex) {
						fex.setMessage("Le nouveau mot de passe n'est pas valide");
						throw fex;
					}
					// Vérification si ancien mot de passe est valide
					if (user.getPass() != null && user.getPass().equals(oldHash)) {
						logger.debug("Modification du profil possible");
					} else {
						throw new FonctionnalException("L'ancien mot de passe n'est pas valide");
					}
				} else {
					throw new FonctionnalException("La confirmation du nouveau mot de passe n'est pas identique");
				}
			} else {
				throw new FonctionnalException("Les mots de passe doivent contenir entre 6 et 30 caractères");
			}
		} else {
			throw new FonctionnalException("Les champs ne sont pas tous renseignés");
		}
		return newHash;
	}

	/**
	 * Met à jour le profil de l'utilisateur en fonction des champs du formulaire ou renvoi une exception fonctionnelle / technique
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updateProfil(User user) throws FonctionnalException, TechnicalException {
		try {
			userDAO.update(user);
			logger.debug("Mise à jour de l'utilisateur effectuée avec succès");
		} catch (FonctionnalException fex) {
			throw fex;
		} catch (TechnicalException tex) {
			throw tex;
		}
	}

}
