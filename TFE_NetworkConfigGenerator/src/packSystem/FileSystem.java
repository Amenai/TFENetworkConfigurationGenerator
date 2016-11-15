package packSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Cette classe permet la gestion des fichiers ".txt"
 * 
 * @author Sylvain-Erwan-Flavian
 * @version 15 Déc.2014
 */
public class FileSystem {
	/**
	 * Methode pour essayer de lire le fichier config, si celi ci est corrompu ou abscent
	 * elle en recrée un nouveau
	 */
	public static String[] lireFichier (){
		try
		{
			File f = new File ("./config.txt");
			FileReader fr = new FileReader (f);
			try
			{
				String infoPerso[] = {null,null,null,null};
				for (int i=0;i< infoPerso.length;i++){
					String tampon = "";
					int c = fr.read(); 
					while((char)c != ':'){c = fr.read();}
					c = fr.read();
					while ((char)c != ';')
					{
						if((char)c != ' ')tampon+=(char)c;
						c = fr.read();
					}
					infoPerso[i] = tampon;
				}
				fr.close();
				return infoPerso;
			}
			catch (IOException exception)
			{
				System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
				System.out.println("Reset des configurations");
				resetConfig();
				lireFichier();
			}
		}
		catch (FileNotFoundException exception)
		{
			System.out.println ("Le fichier n'a pas été trouvé");
			System.out.println("Création d'un nouveau fichier de config");
			resetConfig();
		}
		return null;
	}
	/**
	 * Cree un fichier de configuration de base ou le reecris en cas
	 * d erreur. Prochain etape : lire le fichier de sauvegarde config_old et remettre a jour celui ci
	 */
	private static void resetConfig() {
		try {
			File f = new File ("./config.txt");
			PrintWriter pw = new PrintWriter (new BufferedWriter (new FileWriter (f)));
			String infoDefault[] ={"Name :Player;","Skin :0;","IP :127.0.0.1;","Screen :0;"};
			for(String i :infoDefault){
				pw.println(i);
			}
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Permet d'ecrire dans un fichier
	 * @param indice
	 * @param changement
	 */
	public static void ecrireFichier(int indice,String changement) {
		String oldInfo[] = rebuild();
		File fnew = new File ("./config.txt");
		File fold = new File("./config_old.txt");
		try
		{
			PrintWriter pwnew = new PrintWriter (new BufferedWriter (new FileWriter (fnew)));
			PrintWriter pwold = new PrintWriter (new BufferedWriter (new FileWriter (fold)));
			for (int i=0;i< oldInfo.length;i++){
				String add ="";
				switch(i){
				case 0 : add ="Name    :";break;
				case 1 : add ="Skin    :";break;
				case 2 : add ="IP      :";break;
				case 3 : add ="Screen  :";break;
				default : add="ERREUR  :";break;
				}
				pwold.println(add + oldInfo[i]+";");
				if(i == indice) {pwnew.println(add +changement+";");}
				else {pwnew.println(add + oldInfo[i]+";");}
			}
			pwnew.close();
			pwold.close();
		}
		catch (IOException exception)
		{
			System.out.println ("Erreur lors de l'écriture : " + exception.getMessage());
			System.out.println("Reset des configurations par défault"); 
			resetConfig();
		}

	}
	/**
	 * Gestion des fichiers
	 * @return infoPerso
	 */
	private static String[] rebuild() {
		String infoPerso[] = lireFichier();
		File fdel = new File ("./config.txt");
		File fold = new File("./config_old.txt");
		fdel.delete();
		fold.delete();		
		return infoPerso;
	}
}
