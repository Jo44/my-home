package fr.my.home.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.bean.YouTubeVideo;
import fr.my.home.dao.YouTubePlaylistDAO;
import fr.my.home.dao.YouTubeVideoDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Manager qui prends en charge la gestion des playlists/vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 02/05/2018
 */
public class YouTubeManager {
	private static final Logger logger = LogManager.getLogger(YouTubeManager.class);

	/**
	 * Attributs
	 */
	private YouTubePlaylistDAO ytPlaylistDAO;
	private YouTubeVideoDAO ytVideoDAO;

	/**
	 * Constructeur
	 */
	public YouTubeManager() {
		ytPlaylistDAO = new YouTubePlaylistDAO();
		ytVideoDAO = new YouTubeVideoDAO();
	};

	/**
	 * Renvoi la liste des vidéos des playlists actives de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubeVideo>
	 * @throws TechnicalException
	 */
	public List<YouTubeVideo> getVideosFromPlaylists(int userId) throws TechnicalException {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Récupère la liste des playlists actives
			List<YouTubePlaylist> listPlaylist = ytPlaylistDAO.getAllActivePlaylists(userId);
			logger.debug("Récupération des playlists actives");

			// Pour chaque playlist active
			for (YouTubePlaylist playlist : listPlaylist) {
				try {
					// Récupère la liste des vidéos de la playlist
					List<YouTubeVideo> tempListVideo = ytVideoDAO.getAllVideos(playlist.getId());

					// Ajoute la liste des vidéos à la liste générale
					listVideo.addAll(tempListVideo);
				} catch (FonctionnalException fex) {
					logger.debug("Aucune vidéo pour cette playlist");
				}
			}
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listVideo;
	}

	/**
	 * Renvoi la liste des playlists de l'utilisateur
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws TechnicalException
	 */
	public List<YouTubePlaylist> getPlaylists(int userId) throws TechnicalException {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		try {
			// Récupère la liste des playlists
			listPlaylist = ytPlaylistDAO.getAllPlaylists(userId);
			logger.debug("Récupération des playlists enregistrés");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listPlaylist;
	}

	/**
	 * Récupère la playlist demandée
	 * 
	 * @param playlistId
	 * @param userId
	 * @return YouTubePlaylist
	 * @throws FonctionnalException
	 * @thorws TechnicalException
	 */
	public YouTubePlaylist getPlaylist(int playlistId, int userId) throws FonctionnalException, TechnicalException {
		YouTubePlaylist playlist = null;
		try {
			// Récupère la playlist
			playlist = ytPlaylistDAO.getOnePlaylist(playlistId, userId);
			logger.debug("Récupération de la playlist");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return playlist;
	}

	/**
	 * Récupère la liste des vidéos de la playlist demandée
	 * 
	 * @param playlistId
	 * @return List<YouTubeVideo>
	 * @thorws TechnicalException
	 */
	public List<YouTubeVideo> getVideos(int playlistId) throws TechnicalException {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		try {
			// Récupère la liste des vidéos de la playlist
			listVideo = ytVideoDAO.getAllVideos(playlistId);
			logger.debug("Récupération des vidéos de la playlist");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return listVideo;
	}

	/**
	 * Récupère la vidéo demandée
	 * 
	 * @param videoId
	 * @param playlistId
	 * @return YouTubeVideo
	 * @throws FonctionnalException
	 * @thorws TechnicalException
	 */
	public YouTubeVideo getVideo(int videoId, int playlistId) throws FonctionnalException, TechnicalException {
		YouTubeVideo video = null;
		try {
			// Récupère la vidéo
			video = ytVideoDAO.getOneVideo(videoId, playlistId);
			logger.debug("Récupération de la vidéo");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return video;
	}

	/**
	 * Vérifie si la playlist demandée appartient bien à l'utilisateur et renvoi boolean
	 * 
	 * @param playlistId
	 * @param userId
	 * @return boolean
	 * @thorws TechnicalException
	 */
	public boolean checkPlaylist(int playlistId, int userId) throws TechnicalException {
		boolean valid = false;
		try {
			// Récupère la playlist
			ytPlaylistDAO.getOnePlaylist(playlistId, userId);
			logger.debug("Récupération de la playlist");
			valid = true;
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
		return valid;
	}

	/**
	 * Ajoute la nouvelle playlist
	 * 
	 * @param userId
	 * @param title
	 * @param type
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addPlaylist(int userId, String title, String type) throws FonctionnalException, TechnicalException {
		try {
			// Vérifie si les paramètres sont valides
			checkPlaylistParameters(title, type);

			// Ajoute la nouvelle playlist
			YouTubePlaylist playlist = new YouTubePlaylist(userId, title.trim(), type.trim(), false, null);
			ytPlaylistDAO.add(playlist);
			logger.debug("Nouvelle playlist ajoutée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Ajoute la nouvelle vidéo
	 * 
	 * @param playlistId
	 * @param urlVideo
	 * @param artist
	 * @param title
	 * @param duration
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void addVideo(int playlistId, String urlVideo, String artist, String title, String duration)
			throws FonctionnalException, TechnicalException {
		try {
			// Vérifie si les paramètres sont valides
			checkVideoParameters(urlVideo, artist, title, duration);

			// Ajoute la nouvelle playlist
			YouTubeVideo video = new YouTubeVideo(playlistId, urlVideo.trim(), artist.trim(), title.trim(), duration.trim(), null);
			ytVideoDAO.add(video);
			logger.debug("Nouvelle vidéo ajoutée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Modifie la playlist
	 * 
	 * @param playlistId
	 * @param userId
	 * @param active
	 * @param title
	 * @param type
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updatePlaylist(int playlistId, int userId, boolean active, String title, String type)
			throws FonctionnalException, TechnicalException {
		try {
			// Vérifie si les paramètres sont valides
			checkPlaylistParameters(title, type);

			// Récupère la playlist
			YouTubePlaylist playlist = ytPlaylistDAO.getOnePlaylist(playlistId, userId);
			logger.debug("Récupération de la playlist");

			// Modifie la playlist
			playlist.setActive(active);
			playlist.setTitle(title.trim());
			playlist.setType(type.trim());
			ytPlaylistDAO.update(playlist);
			logger.debug("Playlist modifiée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Modifie la vidéo
	 * 
	 * @param videoId
	 * @param playlistId
	 * @param urlVideo
	 * @param artist
	 * @param title
	 * @param duration
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void updateVideo(int videoId, int playlistId, String urlVideo, String artist, String title, String duration)
			throws FonctionnalException, TechnicalException {
		try {
			// Vérifie si les paramètres sont valides
			checkVideoParameters(urlVideo, artist, title, duration);

			// Récupère la vidéo
			YouTubeVideo video = ytVideoDAO.getOneVideo(videoId, playlistId);
			logger.debug("Récupération de la vidéo");

			// Modifie la vidéo
			video.setIdUrl(urlVideo.trim());
			video.setArtist(artist.trim());
			video.setTitle(title.trim());
			video.setDuration(duration.trim());
			ytVideoDAO.update(video);
			logger.debug("Vidéo modifiée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Vérifie si les paramètres d'une playlist sont valides sinon renvoi exception fonctionnelle
	 * 
	 * @param title
	 * @param type
	 * @throws FonctionnalException
	 */
	private void checkPlaylistParameters(String title, String type) throws FonctionnalException {
		if (title == null || title.trim().length() < 1 || title.trim().length() > 50) {
			throw new FonctionnalException("Le titre n'est pas valide");
		}
		if (type == null || type.trim().length() < 1 || type.trim().length() > 50) {
			throw new FonctionnalException("Le type n'est pas valide");
		}
	}

	/**
	 * Vérifie si les paramètres d'une vidéo sont valides sinon renvoi exception fonctionnelle
	 * 
	 * @param urlVideo
	 * @param artist
	 * @param title
	 * @param duration
	 * @throws FonctionnalException
	 */
	private void checkVideoParameters(String urlVideo, String artist, String title, String duration) throws FonctionnalException {
		if (urlVideo == null || urlVideo.trim().length() < 1 || urlVideo.trim().length() > 50) {
			throw new FonctionnalException("L'url de la vidéo n'est pas valide");
		}
		if (artist == null || artist.trim().length() < 1 || artist.trim().length() > 50) {
			throw new FonctionnalException("L'artiste n'est pas valide");
		}
		if (title == null || title.trim().length() < 1 || title.trim().length() > 50) {
			throw new FonctionnalException("Le titre n'est pas valide");
		}
		if (duration == null || duration.trim().length() < 1 || duration.trim().length() > 12) {
			throw new FonctionnalException("La durée n'est pas valide");
		}
	}

	/**
	 * Récupère la playlist désirée, vérifie si elle n'est pas 'active' puis supprime la playlist et toutes ses vidéos, ou exception si impossible
	 * 
	 * @param userId
	 * @param playlistId
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deletePlaylist(int playlistId, int userId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une playlist en cours ..");
		try {
			// Récupère la playlist avant sa suppression
			YouTubePlaylist playlist = ytPlaylistDAO.getOnePlaylist(playlistId, userId);

			// Vérifie si la playlist n'est pas celle qui est active, sinon renvoi exception fonctionnelle
			if (playlist.isActive()) {
				throw new FonctionnalException("Impossible de supprimer une playlist active");
			} else {
				List<YouTubeVideo> listVideo;
				try {
					// Si non-active, récupère la liste des vidéos de la playlist avant la suppression
					listVideo = ytVideoDAO.getAllVideos(playlistId);
				} catch (FonctionnalException fex) {
					listVideo = null;
				}

				// Supprime les vidéos de la base de donnée
				if (listVideo != null && listVideo.size() > 0) {
					for (YouTubeVideo video : listVideo) {
						ytVideoDAO.delete(video);
						logger.debug("Suppression de la vidéo {" + video.getId() + "} de la playlist {" + playlistId + "} effectuée");
					}
				}

				// Supprime la playlist de la base de donnée
				ytPlaylistDAO.delete(playlist);
				logger.debug("Suppression de la playlist {" + playlistId + "} effectuée");
			}
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Récupère la vidéo désirée puis la supprime
	 * 
	 * @param videoId
	 * @param playlistId
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void deleteVideo(int videoId, int playlistId) throws FonctionnalException, TechnicalException {
		logger.debug("Tentative de suppression d'une vidéo en cours ..");
		try {
			// Récupère la vidéo avant sa suppression
			YouTubeVideo video = ytVideoDAO.getOneVideo(videoId, playlistId);

			// Supprime la vidéo de la base de donnée
			ytVideoDAO.delete(video);
			logger.debug("Suppression de la vidéo {" + video.getId() + "} de la playlist {" + playlistId + "} effectuée");
		} catch (FonctionnalException fex) {
			logger.debug(fex.getMessage());
			throw fex;
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw tex;
		}
	}

	/**
	 * Organise la liste des playlists en fonction des paramètres
	 * 
	 * @param listPlaylist
	 * @param orderBy
	 * @param dir
	 * @return List<YouTubePlaylist>
	 */
	public List<YouTubePlaylist> playlistOrderBy(List<YouTubePlaylist> listPlaylist, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty()
				&& (orderBy.equals("title") | orderBy.equals("type") | orderBy.equals("active") | orderBy.equals("date")) && dir != null
				&& !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("title")) {
				// Si ordre par titre
				listPlaylist.sort(Comparator.comparing(YouTubePlaylist::getTitle).thenComparing(YouTubePlaylist::getCreateDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listPlaylist);
				}
			} else if (orderBy.equals("type")) {
				// Si ordre par type
				listPlaylist.sort(Comparator.comparing(YouTubePlaylist::getType).thenComparing(YouTubePlaylist::getCreateDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listPlaylist);
				}
			} else if (orderBy.equals("active")) {
				// Si ordre par active
				listPlaylist.sort(Comparator.comparing(YouTubePlaylist::isActive).thenComparing(YouTubePlaylist::getCreateDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listPlaylist);
				}
			} else {
				// Si ordre par date
				listPlaylist.sort(Comparator.comparing(YouTubePlaylist::getCreateDate).thenComparingInt(YouTubePlaylist::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listPlaylist);
				}
			}
		} else {
			// Ordre par défaut
			listPlaylist.sort(Comparator.comparing(YouTubePlaylist::getCreateDate).thenComparingInt(YouTubePlaylist::getId));
			Collections.reverse(listPlaylist);
		}
		return listPlaylist;
	}

	/**
	 * Organise la liste des vidéos en fonction des paramètres
	 * 
	 * @param listVideo
	 * @param orderBy
	 * @param dir
	 * @return List<YouTubeVideo>
	 */
	public List<YouTubeVideo> videoOrderBy(List<YouTubeVideo> listVideo, String orderBy, String dir) {
		// Si l'ordre et la direction sont correctement renseignés
		if (orderBy != null && !orderBy.trim().isEmpty()
				&& (orderBy.equals("artist") | orderBy.equals("title") | orderBy.equals("duration") | orderBy.equals("date")) && dir != null
				&& !dir.trim().isEmpty() && (dir.equals("asc") | dir.equals("desc"))) {
			if (orderBy.equals("artist")) {
				// Si ordre par artiste
				listVideo.sort(Comparator.comparing(YouTubeVideo::getArtist).thenComparing(YouTubeVideo::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listVideo);
				}
			} else if (orderBy.equals("title")) {
				// Si ordre par titre
				listVideo.sort(Comparator.comparing(YouTubeVideo::getTitle).thenComparing(YouTubeVideo::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listVideo);
				}
			} else if (orderBy.equals("duration")) {
				// Si ordre par durée
				listVideo.sort(Comparator.comparing(YouTubeVideo::getDuration).thenComparing(YouTubeVideo::getSaveDate));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listVideo);
				}
			} else {
				// Si ordre par date
				listVideo.sort(Comparator.comparing(YouTubeVideo::getSaveDate).thenComparingInt(YouTubeVideo::getId));
				// Inverse si descendant
				if (dir.equals("desc")) {
					Collections.reverse(listVideo);
				}
			}
		} else {
			// Ordre par défaut
			listVideo.sort(Comparator.comparing(YouTubeVideo::getSaveDate).thenComparingInt(YouTubeVideo::getId));
			Collections.reverse(listVideo);
		}
		return listVideo;
	}

}
