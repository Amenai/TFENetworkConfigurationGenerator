package packSystem;
/**
 * Cette classe permet la creation d'une Liste chainee
 * 
 * @author Sylvain-Erwan-Flavian
 * @version 15 Déc.2014
 */
public class RollsRoyceList {
	private Node head;
	private int ligne;
	/**
	 * Constructeur
	 */
	public RollsRoyceList() {
		head = new Node(null);
		ligne = 0;
	}
	/**
	 * Permet d'ajouter un string
	 * @param phrase
	 */
	public void add(String phrase)
	{
		Node tampon = new Node(phrase);
		Node Current = head;
		while (Current.getNext() != null) {
			Current = Current.getNext();
		}
		Current.setNext(tampon);
		ligne++;
	} 
	/**
	 * Permet de recuperer un string
	 * @param index
	 * @return
	 */
	public String get(int index)
	{
		if (index <= 0)
			return "Error";
		Node Current = head.getNext();
		for (int i = 1; i < index; i++) {
			if (Current.getNext() == null)
				return "Error";
			Current = Current.getNext();
		}
		return Current.getPhrase();
	}
	/**
	 * Permet de supprimer le dernier element
	 * @return
	 */
	public boolean remove()
	{
		Node Current = head;
		Current.setNext(Current.getNext().getNext());
		ligne--;
		return true;
	}
	/**
	 * Permet de supprimer l element choisis
	 * @param index
	 * @return
	 */
	public boolean removeAt(int index)
	{
		Node Current = head.getNext();
		for (int i = 1; i < index; i++) {
			if (Current.getNext() == null){
				return false;}
			Current = Current.getNext();
		}
		Current.setNext(Current.getNext().getNext());
		ligne--;
		return true;
	}
	/**
	 * Permet de return la taille de la liste
	 * @return
	 */
	public int size(){
		return ligne;
	}
	/**
	 * Classe d un element de la liste
	 * @author sylvain
	 *
	 */
	private class Node {
		Node next;
		String phrase;
		/**
		 * Constructeur
		 * @param object
		 */
		public Node(String object) {
			next = null;
			phrase = object;
		} 
		/**
		 * Recuperer la phrase
		 * @return
		 */
		public String getPhrase() {
			return phrase;
		} 
		/**
		 * Avoir la phrase suivante
		 * @return
		 */
		public Node getNext() {
			return next;
		}
		/**
		 * Permet de set la phrase qui suis
		 * @param nextValue
		 */
		public void setNext(Node nextValue) {
			next = nextValue;
		}
	}
}
