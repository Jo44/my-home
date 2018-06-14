package fr.my.home.servlet.file;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.CustomFile;
import fr.my.home.bean.User;
import fr.my.home.bean.ViewAttribut;
import fr.my.home.bean.ViewJSP;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.manager.FilesManager;
import fr.my.home.tool.Settings;

/*
 * Fonctionnement général:
 * 
 * Chaque utilisateur peut ajouter un ou plusieurs fichiers depuis la page web. Une fois qu'on valide le formulaire, ajoute le(s) fichier(s) en BDD et
 * sur le disque.
 * 
 * De plus, on voit en dessous la liste des fichiers uploadés pour l'utilisateur connecté. Il peut alors télécharger les fichier ou les supprimer (en
 * BDD et sur le disque).
 * 
 */

/**
 * Servlet qui prends en charge la gestion des fichiers personnels (upload / download)
 * 
 * @author Jonathan
 * @version 1.7
 * @since 02/05/2018
 */
@WebServlet("/files")
@MultipartConfig(fileSizeThreshold = 0, maxFileSize = 1024 * 1024 * 100, maxRequestSize = 1024 * 1024 * 250)
public class FilesServlet extends HttpServlet {
	private static final long serialVersionUID = 930448801449184468L;
	private static final Logger logger = LogManager.getLogger(FilesServlet.class);

	/**
	 * Attributs
	 */
	private static final String FILE_ERROR_DB = Settings.getStringProperty("error_db");
	private FilesManager fileMgr;

	/**
	 * Constructeur
	 */
	public FilesServlet() {
		super();
		// Initialisation du manager
		fileMgr = new FilesManager();
	}

	/**
	 * Redirection vers le téléchargement, la liste et la suppression de fichiers
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Files Servlet [GET] -->");

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

		// Récupère l'ID de l'utilisateur en session
		int userId = ((User) request.getSession().getAttribute("user")).getId();

		// Récupère les paramètres de la requête
		String action = request.getParameter("action");
		String id = request.getParameter("id");
		int fileId;
		try {
			fileId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			fileId = 0;
		}
		String orderBy = request.getParameter("order-by");
		String dir = request.getParameter("dir");

		// Détermine le traitement en fonction des paramètres
		// Si paramètre action est renseigné
		if (action != null) {
			switch (action) {
				// Si action télécharger
				case "get":
					// Renvoi le fichier demandé au navigateur de l'utilisateur
					boolean exist = getFunction(response, view, fileId, userId);
					if (!exist) {
						// Si il n'existe pas, renvoi à la liste des fichiers
						redirectToFiles(request, response, view, userId, null, null);
					}
					break;
				// Si action supprimer
				case "delete":
					// Essaye de supprimer le fichier
					deleteFunction(view, fileId, userId);

					// Puis renvoi à la liste des fichiers
					redirectToFiles(request, response, view, userId, null, null);
					break;
				// Si action lister
				case "list":
					// Renvoi à la liste des fichiers
					redirectToFiles(request, response, view, userId, orderBy, dir);
					break;
				// Si action non reconnu
				default:
					// Renvoi à la liste des fichiers
					redirectToFiles(request, response, view, userId, null, null);
					break;
			}
		} else {
			// Si paramètre action non renseigné
			// Renvoi à la liste des fichiers
			redirectToFiles(request, response, view, userId, null, null);
		}
	}

	/**
	 * Traitement du formulaire d'ajout de fichiers
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.info("--> Files Servlet [POST] -->");
		logger.info("Tentative d'upload de fichier(s) ..");
		int nbAddedFile;

		// Récupération de l'utilisateur connecté
		User user = (User) request.getSession().getAttribute("user");

		try {
			// Récupère la liste des parts du formulaire (fichiers envoyés ou non)
			// <input type="file" name="file" multiple="true">
			List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());

			// Essaye d'ajouter les fichiers en base et sur le stockage
			nbAddedFile = fileMgr.addFiles(fileParts, user.getId());

			// Ajoute le message de succès dans la session (en fonction du nombre de fichier ajouté)
			if (nbAddedFile > 1) {
				request.getSession().setAttribute("success", "Les fichiers ont été correctement ajoutés");
			} else {
				request.getSession().setAttribute("success", "Le fichier a été correctement ajouté");
			}
		} catch (FonctionnalException fex) {
			request.getSession().setAttribute("error", fex.getMessage());
		} catch (TechnicalException tex) {
			request.getSession().setAttribute("error", FILE_ERROR_DB);
		} catch (IOException ioe) {
			request.getSession().setAttribute("error", "Erreur d'écriture des fichiers !");
		} catch (IllegalStateException ise) {
			String error = "La taille des fichiers dépassent le poids autorisé";
			logger.error(error);
			request.getSession().setAttribute("error", error);
		} finally {
			// Redirection vers la servlet en GET
			redirectToThisServletAfterPost(request, response);
		}
	}

	/**
	 * Récupère le fichier selon son ID et essaye de renvoyer le téléchargement à l'utilisateur et charge erreur dans la view si besoin
	 * 
	 * @param response
	 * @param view
	 * @param fileId
	 * @param userId
	 * @return boolean
	 */
	private boolean getFunction(HttpServletResponse response, ViewJSP view, int fileId, int userId) {
		boolean valid = false;
		CustomFile file = null;
		try {
			// Récupère le fichier selon son ID et l'ID de l'utilisateur
			file = fileMgr.getFile(fileId, userId);

			// Renvoi le téléchargement à l'utilisateur
			fileMgr.downloadFile(response, file);

			// Le téléchargement est possible
			valid = true;
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", FILE_ERROR_DB));
		} catch (IOException ioe) {
			view.addAttributeToList(new ViewAttribut("error", "Erreur lors de la récupération du fichier"));
		}
		return valid;
	}

	/**
	 * Récupère le fichier selon son ID et essaye de le supprimer de la base de donnée et du stockage et charge erreur dans la view si besoin
	 * 
	 * @param view
	 * @param fileId
	 * @param userId
	 */
	private void deleteFunction(ViewJSP view, int fileId, int userId) {
		CustomFile file = null;
		try {
			// Récupère le fichier selon son ID et l'ID de l'utilisateur
			file = fileMgr.getFile(fileId, userId);

			// Supprime le fichier
			fileMgr.deleteFile(file);

			// Ajoute le message de succès dans la view
			view.addAttributeToList(new ViewAttribut("success", "Le fichier a été correctement supprimé"));
		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", FILE_ERROR_DB));
		}
	}

	/**
	 * Récupère la liste des fichiers de l'utilisateur, le poids total utilisé, tri la liste en fonction des paramètres, puis renvoi vers la JSP
	 * fichiers avec message d'erreur si besoin
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @param userId
	 * @param orderBy
	 * @param dir
	 * @throws IOException
	 * @throws ServletException
	 */
	private void redirectToFiles(HttpServletRequest request, HttpServletResponse response, ViewJSP view, int userId, String orderBy, String dir)
			throws ServletException, IOException {
		List<CustomFile> listFile = null;
		long usedSpace = 0L;
		try {
			// Récupère la liste des fichiers de l'utilisateur
			listFile = fileMgr.getFiles(userId);

			// Récupère le poids total utilisé
			for (CustomFile file : listFile) {
				usedSpace += file.getWeight();
			}

			// Tri la liste en fonction des paramètres
			listFile = fileMgr.orderBy(listFile, orderBy, dir);

		} catch (FonctionnalException fex) {
			view.addAttributeToList(new ViewAttribut("error", fex.getMessage()));
		} catch (TechnicalException tex) {
			view.addAttributeToList(new ViewAttribut("error", FILE_ERROR_DB));
		}
		view.addAttributeToList(new ViewAttribut("listFile", listFile));
		view.addAttributeToList(new ViewAttribut("usedSpace", usedSpace));
		redirectToFilesJSP(request, response, view);
	}

	/**
	 * Redirige la requête vers la JSP Files
	 * 
	 * @param request
	 * @param response
	 * @param view
	 * @throws ServletException
	 * @throws IOException
	 */
	private void redirectToFilesJSP(HttpServletRequest request, HttpServletResponse response, ViewJSP view) throws ServletException, IOException {
		// Charge la view dans la requête
		request.setAttribute("view", view);

		// Redirige vers la JSP
		logger.info(" --> Files JSP --> ");
		RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/files/files.jsp");
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

}
