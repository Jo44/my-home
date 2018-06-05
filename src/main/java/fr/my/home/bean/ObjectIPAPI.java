package fr.my.home.bean;

import java.io.Serializable;

/**
 * Classe modèle d'un object IPAPI regroupant tous les informations d'une requête à ip-api.com permettant la géolocalisation d'une IP ou nom de
 * domaine
 * 
 * @author Jonathan
 * @version 1.0
 * @since 10/17/2017
 */
public class ObjectIPAPI implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private String status;
	private String org;
	private String country;
	private String query;
	private String lat;
	private String lon;

	/**
	 * Constructeur
	 *
	 * @param status
	 * @param org
	 * @param country
	 * @param query
	 * @param lat
	 * @param lon
	 */
	public ObjectIPAPI(String status, String org, String country, String query, String lat, String lon) {
		this.status = status;
		this.org = org;
		this.country = country;
		this.query = query;
		this.lat = lat;
		this.lon = lon;
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ Status: ");
		sb.append(status);
		sb.append(" , Org: ");
		sb.append(org);
		sb.append(" , Country: ");
		sb.append(country);
		sb.append(" , Query: ");
		sb.append(query);
		sb.append(" , Lat: ");
		sb.append(lat);
		sb.append(" , Lon: ");
		sb.append(lon);
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Getters
	 */
	public String getStatus() {
		return status;
	}

	public String getLat() {
		return lat;
	}

	public String getLon() {
		return lon;
	}

	public String getOrg() {
		return org;
	}

	public String getCountry() {
		return country;
	}

	public String getQuery() {
		return query;
	}

}
