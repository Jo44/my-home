package fr.my.home.dao;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.query.Query;

import fr.my.home.bean.User;
import fr.my.home.dao.impl.HibernateDAO;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;
import fr.my.home.tool.DatabaseAccess;
import fr.my.home.tool.Settings;

/**
 * Classe DAO Hibernate qui prends en charge la gestion des utilisateurs
 * 
 * @author Jonathan
 * @version 1.1
 * @since 26/04/2018
 */
public class UserDAO implements HibernateDAO<User> {

	/**
	 * Attributs
	 */
	private static final String USERDAO_GET_BY_LOGINS = Settings.getStringProperty("user_get_by_logins");
	private static final String USERDAO_GET_BY_USERNAME = Settings.getStringProperty("user_get_by_username");
	private static final String USERDAO_GET_BY_EMAIL = Settings.getStringProperty("user_get_by_email");
	private static final String USERDAO_GET_BY_REMEMBER_ME_TOKEN = Settings.getStringProperty("user_get_by_remember_me_token");
	private static final String USERDAO_GET_BY_VALIDATION_TOKEN = Settings.getStringProperty("user_get_by_validation_token");
	private static final String USERDAO_GET_BY_REINIT_TOKEN = Settings.getStringProperty("user_get_by_reinit_token");

	/**
	 * Constructeur
	 */
	public UserDAO() {}

	/**
	 * Méthodes
	 */

	/**
	 * Renvoi l'utilisateur associé au couple login / password, ou exception fonctionnelle si aucun utilisateur
	 * 
	 * @param username
	 * @param password
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUser(String username, String password) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_LOGINS);
		query.setParameter("user_name", username);
		query.setParameter("user_pass", password);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Identifiants incorrects");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Renvoi l'utilisateur associé au username, ou exception fonctionnelle si aucun utilisateur (pour vérification de disponibilité lors de
	 * l'inscription d'un utilisateur)
	 * 
	 * @param username
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByUsername(String username) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_USERNAME);
		query.setParameter("user_name", username);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le login est disponible");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Renvoi l'utilisateur associé à l'email, ou exception fonctionnelle si aucun utilisateur (pour vérification de disponibilité lors de
	 * l'inscription d'un utilisateur)
	 * 
	 * @param email
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByEmail(String email) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_EMAIL);
		query.setParameter("user_email", email);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Cet email n'est pas enregistré");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Renvoi l'utilisateur associé au 'Remember Me', ou exception fonctionnelle si aucun utilisateur (pour récupération du nom de l'utilisateur sur
	 * la page login)
	 * 
	 * @param rememberMe
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByRememberMeToken(String rememberMeToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_REMEMBER_ME_TOKEN);
		query.setParameter("user_remember_me_token", rememberMeToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Aucun utilisateur pour ce Remember Me");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Renvoi l'utilisateur associé au token de validation, ou exception fonctionnelle si aucun utilisateur (pour vérification par email lors de
	 * l'inscription d'un utilisateur)
	 * 
	 * @param validationToken
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByValidationToken(String validationToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_VALIDATION_TOKEN);
		query.setParameter("user_validation_token", validationToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le token de validation n'existe pas ?");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Renvoi l'utilisateur associé au token de ré-initialisation, ou exception fonctionnelle si aucun utilisateur (pour vérification par email lors
	 * de la ré-initialisation du mot de passe d'un utilisateur)
	 * 
	 * @param reInitToken
	 * @return User
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public User getUserByReInitToken(String reInitToken) throws FonctionnalException, TechnicalException {
		User user = null;
		Session session = DatabaseAccess.getInstance().openSession();
		@SuppressWarnings("unchecked")
		Query<User> query = session.createQuery(USERDAO_GET_BY_REINIT_TOKEN);
		query.setParameter("user_reinit_token", reInitToken);
		try {
			user = query.getSingleResult();
		} catch (NoResultException nre) {
			throw new FonctionnalException("Le token de ré-initialisation n'existe pas ?");
		} finally {
			DatabaseAccess.getInstance().validateSession(session);
		}
		return user;
	}

	/**
	 * Ajoute un nouvel utilisateur en base, ou exception fonctionnelle si impossible
	 * 
	 * @param user
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void add(User user) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.save(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible d'ajouter l'utilisateur");
		}
	}

	/**
	 * Met à jour un utilisateur, ou exception fonctionnelle impossible (e-mail indisponible ?)
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void update(User user) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.saveOrUpdate(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de mettre à jour l'utilisateur");
		}
	}

	/**
	 * Supprime un utilisateur, ou exception fonctionnelle si impossible
	 * 
	 * @param user
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	@Override
	public void delete(User user) throws FonctionnalException, TechnicalException {
		Session session = DatabaseAccess.getInstance().openSession();
		try {
			session.delete(user);
			DatabaseAccess.getInstance().validateSession(session);
		} catch (PersistenceException pe) {
			throw new FonctionnalException("Impossible de supprimer l'utilisateur");
		}
	}

}
