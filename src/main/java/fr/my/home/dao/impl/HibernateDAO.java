package fr.my.home.dao.impl;

import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Interface DAO Hibernate qui déclare les 3 méthodes génériques CREATE / UPDATE / DELETE
 * 
 * @author Jonathan
 * @version 1.0
 * @since 26/04/2018
 */
public interface HibernateDAO<T> {

	/**
	 * Ajoute un nouvel objet en base, ou exception fonctionnelle si impossible et technique si erreur de base de données
	 * 
	 * @param object
	 * @param FonctionnalException
	 * @throws TechnicalException
	 */
	public void add(T object) throws FonctionnalException, TechnicalException;

	/**
	 * Met à jour un objet, ou exception fonctionnelle si impossible et technique si erreur de base de données
	 * 
	 * @param object
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void update(T object) throws FonctionnalException, TechnicalException;

	/**
	 * Supprime un objet, ou exception fonctionnelle si impossible et technique si erreur de base de données
	 * 
	 * @param object
	 * @throws FonctionnalException
	 * @throws TechnicalException
	 */
	public void delete(T object) throws FonctionnalException, TechnicalException;

}
