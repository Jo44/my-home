package fr.my.home.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Classe modèle des utilisateurs de l'application
 * 
 * @author Jonathan
 * @version 1.3
 * @since 26/04/2018
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private int id;
	private String name;
	private String pass;
	private String email;
	private String rememberMeToken;
	private String validationToken;
	private boolean active;
	private String reInitToken;
	private Timestamp reInitDate;
	private Timestamp inscriptionDate;

	/**
	 * Constructeur par défaut
	 */
	public User() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param name
	 * @param pass
	 * @param email
	 * @param rememberMeToken
	 * @param validationToken
	 * @param active
	 * @param reInitToken
	 * @param reInitDate
	 * @param inscriptionDate
	 */
	public User(String name, String pass, String email, String rememberMeToken, String validationToken, boolean active, String reInitToken,
			Timestamp reInitDate, Timestamp inscriptionDate) {
		this();
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.rememberMeToken = rememberMeToken;
		this.validationToken = validationToken;
		this.active = active;
		this.reInitToken = reInitToken;
		this.reInitDate = reInitDate;
		this.inscriptionDate = inscriptionDate;
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ID: ");
		sb.append(String.valueOf(id));
		sb.append(" , Name: ");
		sb.append(name);
		sb.append(" , Email: ");
		sb.append(email);
		sb.append(" , RememberMe Token: ");
		sb.append(rememberMeToken);
		sb.append(" , Validation Token: ");
		sb.append(validationToken);
		sb.append(" , Active: ");
		sb.append(String.valueOf(active));
		sb.append(" , ReInit Token: ");
		sb.append(reInitToken);
		sb.append(" , ReInit Date: ");
		if (reInitDate != null) {
			sb.append(reInitDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" , Inscription Date: ");
		if (inscriptionDate != null) {
			sb.append(inscriptionDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Getters / Setters (les setters privés sont seulement utilisés par Hibernate)
	 */
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "user_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "user_name")
	public String getName() {
		return name;
	}

	@SuppressWarnings("unused")
	private void setName(String name) {
		this.name = name;
	}

	@Column(name = "user_pass")
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	@Column(name = "user_email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "user_remember_me_token")
	private String getRememberMeToken() {
		return rememberMeToken;
	}

	public void setRememberMeToken(String rememberMeToken) {
		this.rememberMeToken = rememberMeToken;
	}

	@Column(name = "user_validation_token")
	public String getValidationToken() {
		return validationToken;
	}

	public void setValidationToken(String validationToken) {
		this.validationToken = validationToken;
	}

	@Column(name = "user_active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "user_reinit_token")
	public String getReInitToken() {
		return reInitToken;
	}

	public void setReInitToken(String reInitToken) {
		this.reInitToken = reInitToken;
	}

	@Column(name = "user_reinit_date")
	public Timestamp getReInitDate() {
		return reInitDate;
	}

	public void setReInitDate(Timestamp reInitDate) {
		this.reInitDate = reInitDate;
	}

	@Column(name = "user_inscription_date")
	public Timestamp getInscriptionDate() {
		return inscriptionDate;
	}

	@SuppressWarnings("unused")
	private void setInscriptionDate(Timestamp inscriptionDate) {
		this.inscriptionDate = inscriptionDate;
	}

}
