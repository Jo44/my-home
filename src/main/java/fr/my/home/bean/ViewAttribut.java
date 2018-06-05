package fr.my.home.bean;

import java.io.Serializable;

/**
 * Classe modèle d'un attribut d'une view de la JSP
 * 
 * @author Jonathan
 * @version 1.0
 * @since 10/17/2017
 */
public class ViewAttribut implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private String key;
	private Object value;

	/**
	 * Constructeur
	 *
	 * @param key
	 * @param value
	 */
	public ViewAttribut(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Key: ");
		sb.append(key);
		sb.append(" , Value: ");
		if (value != null) {
			sb.append(value.toString());
		} else {
			sb.append("null");
		}
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Getters
	 */
	protected String getKey() {
		return key;
	}

	protected Object getValue() {
		return value;
	}

}
