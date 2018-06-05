package fr.my.home.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.YouTubeVideo;
import fr.my.home.dao.impl.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Classe DAO Hibernate qui prends en charge la gestion des vidéos YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 26/04/2018
 */
public class YouTubeVideoDAO implements HibernateDAO<YouTubeVideo> {

	/**
	 * Attributs
	 */
	private static final String YT_VIDEO_DAO_GET_ALL = Settings.getStringProperty("yt_video_get_all");
	private static final String YT_VIDEO_DAO_GET_ONE = Settings.getStringProperty("yt_video_get_one");

	/**
	 * Constructeur
	 */
	public YouTubeVideoDAO() {};

	/**
	 * Méthodes
	 */

	/**
	 * Récupère la liste de toutes les vidéos de la playlist désirée, ou exception fonctionnelle si aucune vidéo
	 * 
	 * @param playlistId
	 * @return List<YouTubeVideo>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<YouTubeVideo> getAllVideos(int playlistId) throws FonctionnalException, TechnicalException {
		List<YouTubeVideo> listVideo = new ArrayList<YouTubeVideo>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubeVideo> query = session.createQuery(YT_VIDEO_DAO_GET_ALL);
		query.setParameter("yt_video_id_playlist", playlistId);
		try {
			listVideo = query.getResultList();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucune vidéo enregistrée");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return listVideo;
	}

	/**
	 * Récupère une video selon son ID et l'ID de la playlist, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param videoId
	 * @param playlistId
	 * @return YouTubeVideo
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public YouTubeVideo getOneVideo(int videoId, int playlistId) throws FonctionnalException, TechnicalException {
		YouTubeVideo video = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubeVideo> query = session.createQuery(YT_VIDEO_DAO_GET_ONE);
		query.setParameter("yt_video_id", videoId);
		query.setParameter("yt_video_id_playlist", playlistId);
		try {
			video = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("La vidéo n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return video;
	}

	/**
	 * Ajoute une nouvelle vidéo en base, ou exception fonctionnelle si impossible
	 * 
	 * @param video
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(YouTubeVideo video) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.save(video);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la vidéo");
		}
	}

	/**
	 * Met à jour une vidéo, ou exception fonctionnelle si impossible
	 * 
	 * @param video
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(YouTubeVideo video) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(video);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la vidéo");
		}
	}

	/**
	 * Supprime une vidéo, ou exception fonctionnelle si impossible
	 * 
	 * @param video
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(YouTubeVideo video) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(video);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer la vidéo");
		}
	}

}
