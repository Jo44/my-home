package fr.my.home.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe modèle d'une view regroupant les attributs nécessaire à la JSP
 * 
 * @author Jonathan
 * @version 1.0
 * @since 10/17/2017
 */
public class ViewJSP implements Serializable {
	private static final long serialVersionUID = 930448801449184468L;

	/**
	 * Attributs
	 */
	private List<ViewAttribut> attributeList;

	/**
	 * Constructeur
	 */
	public ViewJSP() {
		attributeList = new ArrayList<ViewAttribut>();
	}

	/**
	 * To String
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ View : ");
		for (ViewAttribut attribut : attributeList) {
			if (attribut != null) {
				sb.append(attribut.toString());
			}
		}
		sb.append(" }");
		return sb.toString();
	}

	/**
	 * Retourne l'objet d'un attribut présent dans la liste ou null
	 * 
	 * @param key
	 * @return Object
	 */
	public Object getValueForKey(String key) {
		Object obj = null;
		if (key != null) {
			for (ViewAttribut attribut : attributeList) {
				if (attribut != null && attribut.getKey() != null && attribut.getKey().equals(key)) {
					obj = attribut.getValue();
				}
			}
		}
		return obj;
	}

	/**
	 * Ajoute un attribut dans la liste
	 * 
	 * @param attribut
	 */
	public void addAttributeToList(ViewAttribut attribut) {
		attributeList.add(attribut);
	}

}
