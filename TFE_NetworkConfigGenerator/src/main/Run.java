package main;

import java.util.Scanner;
import java.util.regex.Pattern;

import graphique.GUI;
import ip.Ip;
import tests.SubnetUtils;
import tests.SubnetUtils.SubnetInfo;

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
		SubnetUtils subnet = SubnetUtils.getIp();
		new GUI(subnet);
		
				
	}
}
