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
 * Classe modèle de vidéo youtube qui sont enregistrée dans une playlist
 * 
 * @author Jonathan
 * @version 1.0
 * @since 25/04/2018
 */
@Entity
@Table(name = "youtube_video")
public class YouTubeVideo implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private int id;
	private int idPlaylist;
	private String idUrl;
	private String artist;
	private String title;
	private String duration;
	private Timestamp saveDate;

	/**
	 * Constructeur par défaut
	 */
	public YouTubeVideo() {
		super();
	}

	/**
	 * Constructeur
	 * 
	 * @param idPlaylist
	 * @param idUrl
	 * @param artist
	 * @param title
	 * @param duration
	 * @param saveDate
	 */
	public YouTubeVideo(int idPlaylist, String idUrl, String artist, String title, String duration, Timestamp saveDate) {
		this();
		this.idPlaylist = idPlaylist;
		this.idUrl = idUrl;
		this.artist = artist;
		this.title = title;
		this.duration = duration;
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
		sb.append(" , ID Playlist: ");
		sb.append(String.valueOf(idPlaylist));
		sb.append(" , ID Url: ");
		sb.append(idUrl);
		sb.append(" , Artist: ");
		sb.append(artist);
		sb.append(" , Title: ");
		sb.append(title);
		sb.append(" , Duration: ");
		sb.append(duration);
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
	@Column(name = "yt_video_id")
	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	@Column(name = "yt_video_id_playlist")
	public int getIdPlaylist() {
		return idPlaylist;
	}

	@SuppressWarnings("unused")
	private void setIdPlaylist(int idPlaylist) {
		this.idPlaylist = idPlaylist;
	}

	@Column(name = "yt_video_id_url")
	public String getIdUrl() {
		return idUrl;
	}

	public void setIdUrl(String idUrl) {
		this.idUrl = idUrl;
	}

	@Column(name = "yt_video_artist")
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	@Column(name = "yt_video_title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "yt_video_duration")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "yt_video_save_date")
	public Timestamp getSaveDate() {
		return saveDate;
	}

	@SuppressWarnings("unused")
	private void setSaveDate(Timestamp saveDate) {
		this.saveDate = saveDate;
	}

}
