package fr.my.home.tool;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import fr.my.home.bean.ObjectIPAPI;
import fr.my.home.bean.ObjectReCaptcha;
import fr.my.home.exception.FonctionnalException;
import fr.my.home.exception.TechnicalException;

/**
 * Classe regroupant différents outils (hash MD5 / get Date / get Timestamp / get Html / post Html / parse ObjectIPAPI / parse ObjectReCaptcha / send
 * Email)
 * 
 * @author Jonathan
 * @version 1.2
 * @since 23/04/2018
 */
public class GlobalTools {
	private static final Logger logger = LogManager.getLogger(GlobalTools.class);

	/**
	 * Attributs
	 */
	private static final String SMTP_HOSTNAME = Settings.getStringProperty("smtp_hostname");
	private static final int SMTP_PORT = Settings.getIntProperty("smtp_port");
	private static final String SMTP_USER = Settings.getStringProperty("smtp_user");
	private static final String SMTP_PASS = Settings.getStringProperty("smtp_pass");

	/**
	 * Méthode qui permet de hasher un string en MD5
	 * 
	 * @param string
	 * @return String
	 * @throws FonctionnalException
	 */
	public static String hash(String string) throws FonctionnalException {
		String hash;
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(string.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			hash = number.toString(16);
		} catch (NoSuchAlgorithmException ex) {
			String error = "Erreur de cryptage";
			logger.error(error);
			throw new FonctionnalException(error);
		}

		return hash;
	}

	/**
	 * Méthode qui renvoi la date / heure du jour, arrondi à 5 min près, selon le format 'yyyy-MM-dd HH:mm:ss.SSS'
	 * 
	 * @return todayStr
	 */
	public static String getDate() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);
		int hour = now.get(Calendar.HOUR_OF_DAY);
		// Arrondir les minutes à 5 près
		int unroundedMinute = now.get(Calendar.MINUTE);
		int minute = (unroundedMinute + 4) / 5 * 5;

		String yearStr, monthStr, dayStr, hourStr, minuteStr, todayStr;

		yearStr = String.valueOf(year);
		if (month < 10) {
			monthStr = "0" + String.valueOf(month);
		} else {
			monthStr = String.valueOf(month);
		}
		if (day < 10) {
			dayStr = "0" + String.valueOf(day);
		} else {
			dayStr = String.valueOf(day);
		}
		if (hour < 10) {
			hourStr = "0" + String.valueOf(hour);
		} else {
			hourStr = String.valueOf(hour);
		}
		if (minute < 10) {
			minuteStr = "0" + String.valueOf(minute);
		} else {
			minuteStr = String.valueOf(minute);
		}

		todayStr = yearStr + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minuteStr + "00.000";
		return todayStr;
	}

	/**
	 * Parse un string au format yyyy-MM-dd HH:mm:ss.SSS en Timestamp
	 * 
	 * @param day
	 * @param hour
	 * @param minute
	 * @return Timestamp
	 * @throws TechnicalException
	 */
	public static Timestamp getTimestampFromString(String day, String hour, String minute) throws TechnicalException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Timestamp timestamp = null;
		Date date = null;
		try {
			date = formatter.parse(day + " " + hour + ":" + minute + ":00.000");
			timestamp = new Timestamp(date.getTime());
		} catch (ParseException e) {
			// Si le parse échoue, parse le jour actuel à 00:00:00.000
			try {
				date = formatter.parse(getDate());
				timestamp = new Timestamp(date.getTime());
				logger.debug("Parsing de la date du formulaire échoué -> date par défaut initialisée");
			} catch (ParseException e1) {
				throw new TechnicalException("Erreur d'initialisation de l'heure");
			}
		}
		return timestamp;
	}

	/**
	 * Méthode qui récupère la réponse HTML en fonction d'une requête GET à partir de son URL et renvoi le contenu de la réponse HTML
	 * 
	 * @param urlToRead
	 * @return String
	 * @throws IOException
	 */
	public static String getHTML(String urlToRead) throws IOException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URL(urlToRead);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Ajoute le header request
		con.setRequestMethod("GET");

		// Récupère le contenu de la réponse (JSON stocké en string)
		BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		return result.toString();
	}

	/**
	 * Méthode qui récupère la réponse HTML en fonction d'une requête POST à partir de son URL et des paramètres du POST transmis via une
	 * HashMap<String, String> et renvoi le contenu de la réponse HTML
	 * 
	 * @param hmap
	 *            request-url => url POST / user-agent => headers / accept-language => headers / content => body content
	 * @return string
	 * @throws IOException
	 */
	public static String postHTML(HashMap<String, String> hmap) throws IOException {
		StringBuilder result = new StringBuilder();

		// Récupère l'url et ouvre la connexion
		URL url = new URL(hmap.get("request-url"));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		// Ajoute les headers request
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", hmap.get("user-agent"));
		con.setRequestProperty("Accept-Language", hmap.get("accept-language"));

		// Envoi le contenu en POST
		con.setDoOutput(true);
		DataOutputStream dos = new DataOutputStream(con.getOutputStream());
		dos.writeBytes(hmap.get("content"));
		dos.flush();
		dos.close();

		// Récupère les paramètres de la réponse
		int responseCode = con.getResponseCode();
		logger.info("Requête 'POST' vers l'URL : " + url);
		logger.info("Paramètres 'POST' : " + hmap.get("content"));
		logger.info("Response Code : " + String.valueOf(responseCode));

		// Récupère le contenu de la réponse (JSON stocké en string)
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while ((line = br.readLine()) != null) {
			result.append(line);
		}
		br.close();

		return result.toString();
	}

	/**
	 * Méthode qui parse un string de Json en ObjectIPAPI
	 * 
	 * @param JsonToParse
	 * @return ObjectIPAPI
	 * @throws JsonSyntaxException
	 */
	public static ObjectIPAPI getObjectIPAPI(String JsonToParse) throws JsonSyntaxException {
		Gson gson = new Gson();
		ObjectIPAPI ipApiObject = gson.fromJson(JsonToParse, ObjectIPAPI.class);
		return ipApiObject;
	}

	/**
	 * Méthode qui parse un string de Json en ObjectReCaptcha
	 * 
	 * @param JsonToParse
	 * @return ObjectReCaptcha
	 * @throws JsonSyntaxException
	 */
	public static ObjectReCaptcha getObjectReCaptcha(String JsonToParse) throws JsonSyntaxException {
		Gson gson = new Gson();
		ObjectReCaptcha reCaptchaObject = gson.fromJson(JsonToParse, ObjectReCaptcha.class);
		return reCaptchaObject;
	}

	/**
	 * Envoi un mail en utilisant Apache Commons Email
	 * 
	 * @param target
	 * @param subject
	 * @param message
	 */
	public static void sendEmail(String target, String subject, String message) {
		try {
			Email email = new SimpleEmail();
			email.setHostName(SMTP_HOSTNAME);
			email.setSmtpPort(SMTP_PORT);
			email.setAuthenticator(new DefaultAuthenticator(SMTP_USER, SMTP_PASS));
			email.setSSLOnConnect(true);
			email.setFrom(SMTP_USER);
			email.setSubject(subject);
			email.setMsg(message);
			email.addTo(target);
			email.send();
			logger.info("Email correctement envoyé à " + target);
		} catch (EmailException ee) {
			ee.printStackTrace();
			logger.error("Erreur lors de l'envoi de l'email à " + target);
		}
	}

}
