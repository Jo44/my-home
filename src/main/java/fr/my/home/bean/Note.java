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
 * Classe modèle de note que peut saisir un utilisateur
 * 
 * @author Jonathan
 * @version 1.3
 * @since 25/04/2018
 */
@Entity
@Table(name = "note")
public class Note implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private int id;
	private int idUser;
	private String title;
	private String message;
	private Timestamp saveDate;

	/**
	 * Constructeur par défaut
	 */
	public Note() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param idUser
	 * @param title
	 * @param message
	 * @param saveDate
	 */
	public Note(int idUser, String title, String message, Timestamp saveDate) {
		this();
		this.idUser = idUser;
		this.title = title;
		this.message = message;
		this.saveDate = saveDate;
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ID: ");
		sb.append(String.valueOf(id));
		sb.append(" , User: ");
		sb.append(String.valueOf(idUser));
		sb.append(" , Title: ");
		sb.append(title);
		sb.append(" , Message: ");
		sb.append(message);
		sb.append(" , Save Date: ");
		if (saveDate != null) {
			sb.append(saveDate.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Getters / Setters (setters en privé car seulement utilisé par Hibernate)
	 */
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "note_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "note_id_user")
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "note_title")
	public String getTitle() {
		return title;
	}

	@SuppressWarnings("unused")
	private void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "note_message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "note_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
