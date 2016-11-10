package main;

import java.util.Scanner;

import graphique.GUI;
import ip.Ip;

/**
 * Classe Main/Lancement du programme
 * 
 * @author Sylvain
 * @version 23 Octobre 2016	
 */
public class Run{
	/**
	 * MAIN 
	 * @param args
	 */
	public static void main(String[] args) {

		@SuppressWarnings("resource")
		Scanner keyboard = new Scanner(System.in);
		System.out.println("Addresse globale ? ");
		String newGlobal = keyboard.next();
		Ip oo = new Ip(Ip.stringTo32Bits(newGlobal),false);
		System.out.println("" + oo);
		new GUI(newGlobal);
				
	}
}
