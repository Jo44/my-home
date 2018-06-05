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
 * Classe modèle de playlist de vidéo(s) youtube que peut enregistrer un utilisateur
 * 
 * @author Jonathan
 * @version 1.0
 * @since 25/04/2018
 */
@Entity
@Table(name = "youtube_playlist")
public class YouTubePlaylist implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private int id;
	private int idUser;
	private String title;
	private String type;
	private boolean active;
	private Timestamp createDate;

	/**
	 * Constructeur par défaut
	 */
	public YouTubePlaylist() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param idPlaylist
	 * @param title
	 * @param type
	 * @param active
	 * @param createDate
	 */
	public YouTubePlaylist(int idUser, String title, String type, boolean active, Timestamp createDate) {
		this();
		this.idUser = idUser;
		this.title = title;
		this.type = type;
		this.active = active;
		this.createDate = createDate;
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
		sb.append(" , Type: ");
		sb.append(type);
		sb.append(" , Active: ");
		sb.append(String.valueOf(active));
		sb.append(" , Create Date: ");
		if (createDate != null) {
			sb.append(createDate.toString());
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
	@Column(name = "yt_playlist_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "yt_playlist_id_user")
	public int getIdUser() {
		return idUser;
	}

	@SuppressWarnings("unused")
	private void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	@Column(name = "yt_playlist_title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "yt_playlist_type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "yt_playlist_active")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "yt_playlist_create_date")
	public Timestamp getCreateDate() {
		return createDate;
	}

	@SuppressWarnings("unused")
	private void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

}
