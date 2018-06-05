package fr.my.home.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.YouTubePlaylist;
import fr.my.home.dao.impl.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Classe DAO Hibernate qui prends en charge la gestion des playlists YouTube
 * 
 * @author Jonathan
 * @version 1.1
 * @since 26/04/2018
 */
public class YouTubePlaylistDAO implements HibernateDAO<YouTubePlaylist> {

	/**
	 * Attributs
	 */
	private static final String YT_PLAYLIST_DAO_GET_ALL = Settings.getStringProperty("yt_playlist_get_all");
	private static final String YT_PLAYLIST_DAO_GET_ALL_ACTIVE = Settings.getStringProperty("yt_playlist_get_all_active");
	private static final String YT_PLAYLIST_DAO_GET_ONE = Settings.getStringProperty("yt_playlist_get_one");

	/**
	 * Constructeur
	 */
	public YouTubePlaylistDAO() {};

	/**
	 * Méthodes
	 */

	/**
	 * Récupère la liste de toutes les playlists de l'utilisateur, ou exception fonctionnelle si aucune playlist
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<YouTubePlaylist> getAllPlaylists(int userId) throws FonctionnalException, TechnicalException {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL);
		query.setParameter("yt_playlist_id_user", userId);
		try {
			listPlaylist = query.getResultList();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucune playlist enregistrée");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return listPlaylist;
	}

	/**
	 * Récupère la liste de toutes les playlists actives de l'utilisateur, ou exception fonctionnelle si aucune playlist
	 * 
	 * @param userId
	 * @return List<YouTubePlaylist>
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public List<YouTubePlaylist> getAllActivePlaylists(int userId) throws FonctionnalException, TechnicalException {
		List<YouTubePlaylist> listPlaylist = new ArrayList<YouTubePlaylist>();
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ALL_ACTIVE);
		query.setParameter("yt_playlist_id_user", userId);
		try {
			listPlaylist = query.getResultList();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucune playlist active");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return listPlaylist;
	}

	/**
	 * Récupère une playlist selon son ID et l'ID de l'utilisateur, ou exception fonctionnelle si elle n'existe pas
	 * 
	 * @param playlistId
	 * @param userId
	 * @return YouTubePlaylist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public YouTubePlaylist getOnePlaylist(int playlistId, int userId) throws FonctionnalException, TechnicalException {
		YouTubePlaylist playlist = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<YouTubePlaylist> query = session.createQuery(YT_PLAYLIST_DAO_GET_ONE);
		query.setParameter("yt_playlist_id", playlistId);
		query.setParameter("yt_playlist_id_user", userId);
		try {
			playlist = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("La playlist n'existe pas");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return playlist;
	}

	/**
	 * Ajoute une nouvelle playlist en base, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(YouTubePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.save(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter la playlist");
		}
	}

	/**
	 * Met à jour une playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(YouTubePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour la playlist");
		}
	}

	/**
	 * Supprime une playlist, ou exception fonctionnelle si impossible
	 * 
	 * @param playlist
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(YouTubePlaylist playlist) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(playlist);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("La playlist ne doit pas être active pour être supprimer");
		}
	}

}
