package fr.my.home.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe modèle d'un object reCaptcha regroupant tous les informations d'une requête à https://www.google.com/recaptcha/api/siteverify permettant la
 * vérification du système reCatpcha V2
 * 
 * @author Jonathan
 * @version 1.0
 * @since 26/04/2018
 */
public class ObjectReCaptcha implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private boolean success;
	private Timestamp challengeTs;
	private String hostname;

	/**
	 * Constructeur
	 * 
	 * @param success
	 * @param challengeTs
	 * @param hostname
	 */
	public ObjectReCaptcha(boolean success, Timestamp challengeTs, String hostname) {
		this.success = success;
		this.challengeTs = challengeTs;
		this.hostname = hostname;
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Success: ");
		sb.append(String.valueOf(success));
		sb.append(" , Challenge_ts: ");
		sb.append(String.valueOf(challengeTs));
		sb.append(" , Hostname: ");
		sb.append(hostname);
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Getter
	 */
	public boolean isSuccess() {
		return success;
	}

}
