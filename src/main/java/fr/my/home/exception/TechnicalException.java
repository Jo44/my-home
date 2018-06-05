package fr.my.home.exception;

/**
 * Classe modèle d'une exception 'maison' de type technique
 * 
 * @author Jonathan
 * @version 1.0
 * @since 23/04/2018
 */
public class TechnicalException extends Exception {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private String message;

	/**
	 * Constructeur
	 * 
	 * @param message
	 */
	public TechnicalException(String message) {
		this.message = message;
	}

	/**
	 * Getters / Setters
	 */
	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
