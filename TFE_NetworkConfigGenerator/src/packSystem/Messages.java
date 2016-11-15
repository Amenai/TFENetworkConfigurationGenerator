package packSystem;

import javax.swing.JOptionPane;
/**
 * Classe utilisée pour les messages envers l'utilisateurs
 * @author sylvain
 * @version 05 Oct. 2014
 */
public class Messages {

	public static void showMessage(String message,String Titre) {
		JOptionPane.showMessageDialog(null, message,Titre,JOptionPane.INFORMATION_MESSAGE);
	}
	public static void showErrorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error Message", JOptionPane.ERROR_MESSAGE);
	}
	public static void showWarningMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning Message", JOptionPane.WARNING_MESSAGE);
	}
	/**
	 * Affiche un demande de type Oui/Non,renvoye la response
	 *  @return True = + / False = autre
	 */
	public static boolean confirm(String question) {
		return JOptionPane.showConfirmDialog(null, 
				question,
				"Confirmation",
				JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
	}

	/**
	 * Demmande la valeur d'un String
	 *  @return Un String
	 */
	public static String askStringValue(String message) {
		String value = (String) JOptionPane.showInputDialog(null,
				message, "Input please",
				JOptionPane.QUESTION_MESSAGE, null, null, "");
		return value;
	}
	/**
	 * Demmande la valeur d'un int
	 *  @return Un String
	 */
	public static int askIntValue(String message) {
		String value = "-1";
		value = (String)JOptionPane.showInputDialog(null,
				message, "Input please",
				JOptionPane.QUESTION_MESSAGE, null, null, "");
		int valeur = Integer.parseInt(value);
		if (valeur == -1 ) valeur = askIntValue(message);
		return valeur;
	}
	public static void showMessageIn(String message,String in, String titre) {
		JOptionPane.showInputDialog(null,
				message, titre,
				JOptionPane.INFORMATION_MESSAGE, null, null, in);
	}
}
