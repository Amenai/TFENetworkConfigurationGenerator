package ip;
/**
 * Class Ip, contient les methodes par rapport a l'ip
 * 
 * @author Sylvain Delbauve
 * @version 05 Oct. 2014
 * 
 */
public class Classe32bits {
	protected int octet[] = {0,0,0,0};
	protected int deci[] = {0,0,0,0};
	/**
	 * Constructeur par defaut
	 */
	public Classe32bits(){}
	/**
	 * Construit une Ip en fonction de si c'est en décimal ou pas
	 * @param tab
	 * @param decimal True pour décimal
	 */
	public Classe32bits(int[] tab,boolean decimal) {
		if (decimal){
			this.deci[0] = tab[0];
			this.deci[1] = tab[1];
			this.deci[2] = tab[2];
			this.deci[3] = tab[3];

			int []tabBin = deciToBin(tab);
			this.octet[0] = tabBin[0];
			this.octet[1] = tabBin[1];
			this.octet[2] = tabBin[2];
			this.octet[3] = tabBin[3];
			/**TODO**/
			String blop = Integer.toBinaryString(192);
			System.out.println(""+ tab[0]+"  " + blop);	
			System.out.println("|" + Integer.toBinaryString(tab[1]));
			System.out.println("|" + Integer.toBinaryString(tab[2]));
			System.out.println("|" + Integer.toBinaryString(tab[3]));
		}
		else{
			this.octet[0] = tab[0];
			this.octet[1] = tab[1];
			this.octet[2] = tab[2];
			this.octet[3] = tab[3];

			int []tabDeci = binToDeci(tab);
			this.deci[0] = tabDeci[0];
			this.deci[1] = tabDeci[1];
			this.deci[2] = tabDeci[2];
			this.deci[3] = tabDeci[3];
		}
	}
	/**
	 * Permet d'afficher une IP4
	 * @param bits32
	 */
	public void affiche32Bits(Classe32bits bits32,String message) {
		System.out.println(""+ message);
		System.out.println("Binaire = " + bits32.octet[0]+ " "+bits32.octet[1]+ " "+bits32.octet[2]+ " "+bits32.octet[3]);
		System.out.println("Ip  = " + bits32.deci[0]+"."+bits32.deci[1]+ "."+bits32.deci[2]+ "."+bits32.deci[3]);
		System.out.println("--------------------------------------------------");
	}
	/**
	 * getIP
	 */
	public int[] getOctet() {
		return octet;
	}
	public void setOctet(int[] octet) {
		this.octet = octet;
	}
	public int[] getDeci() {
		return deci;
	}
	public void setDeci(int[] ip) {
		this.deci = ip;
	}
	/**
	 * Permet de changer un nombre décimal en nombre binaire 
	 * @param y
	 * @return binaire
	 */
	public int[] deciToBin(int[] i) {
		int binaire[] = {0,0,0,0};
		for (int u = 0; u <4;u++){
			int compteur = 0;

			while (i[u] != 0){
				if (i[u]%2 != 0){
					binaire[u] = binaire[u] + (int) Math.pow(10,compteur);
					i[u]--;
				}
				i[u] = (i[u])/2;
				compteur++;
			}
		}
		return binaire;
	}
	/**
	 * Permet de changer un nombre binaire en nombre décimal
	 * @param y
	 * @return deci
	 */
	public int[] binToDeci(int[] i) {
		int deci[] = {0,0,0,0};
		for (int u = 0; u <4;u++){
			int compteur = 0;

			while (i[u] != 0){
				if (i[u]%10 != 0){
					deci[u] = deci[u] + (int) Math.pow(2,compteur);
				}
				i[u] = (i[u]) /10;
				compteur++;
			}
		}
		return deci;
	}
	/**
	 * Permet de retourner le nombre d'IP utilisable
	 * @param Ip
	 * @param masque
	 * @return
	 */
	public static int getNombreIP (int prefix){		
		return (int) (Math.pow(2,(32-prefix))-2);
	}/**
	 * Renvoye un string pour une IP
	 * @param Ip
	 * @return
	 */
	public String ipToString (Classe32bits Ip){
		String string = "";
		for (int compteur = 0;compteur < 3;compteur ++){
			string += Ip.deci[compteur];
			string += ".";
		}
		string += Ip.deci[3];
		return string;
	}
	/**
	 * Renvoye une Ip pour un string
	 * @param string
	 * @return
	 */
	public static int[] stringTo32Bits (String string){
		int [] tampon = {0,0,0,0};
		string = string +'.';
		char a = ' ';
		String b = "";
		int x =0;
		for (int compteur = 0; compteur < string.length();compteur++){
			a = string.charAt(compteur);
			if (a != '.'){b= b +a ;}
			
			else{
				tampon[x] = Integer.parseInt(b);
				x++;
				b = "";
			}
		}
		//Classe32bits Ip = new Classe32bits(tampon, true);
		return tampon;
	}
	/**
	 * Permet de recevoir le Masque rÃ©seau en DÃ©cimal
	 * @param prefix
	 * @return
	 *
	public Classe32bits getMasqueFromPrefix(int prefix){
		int[] tabOctet = {11111111,11111111,11111111,11111111};
		Classe32bits masque= new Classe32bits(tabOctet,false);
		int compteur = 32-prefix;
		for (int i = 3;i >= 0;i--){
			if (compteur-8 >= 0){
				masque.octet[i] = 0;
			}
			else{
				if (compteur-8 >-8 && compteur-8 <=0){
					int reste = 11111111;
					for (int x = 0;x <compteur;x++){
						reste -= (int) Math.pow(10,x);
					}
					masque.octet[i] = reste;
				}
			}
			compteur = compteur - 8;
		}
		Classe32bits masque2 = new Classe32bits(masque.octet,false);
		return masque2;
	}*/
	/**
	 * Permet de retourner le nombre de bits Hotes
	 * @param prefix
	 * @return
	 */
	public static int getBitHote (int prefix){
		return 32-prefix;
	}
	/**
	 * Permet de retourner le nombre d'IP utilisable
	 * @param Ip
	 * @param masque
	 * @return
	 *
	public static int getNombreIP (int prefix){		
		return (int) (Math.pow(2,(32-prefix))-2);
	}*/
	/**
	 * Renvoye un string pour une IP
	 * @param Ip
	 * @return
	 *
	public String ipToString (Classe32bits Ip){
		String string = "";
		for (int compteur = 0;compteur < 3;compteur ++){
			string += Ip.deci[compteur];
			string += ".";
		}
		string += Ip.deci[3];
		return string;
	}*/
	/**
	 * Renvoye une Ip pour un string
	 * @param string
	 * @return
	 *
	public Classe32bits stringToIpv4 (String string){
		int [] tampon = {0,0,0,0};
		string = string +'.';
		char a = ' ';
		String b = "";
		int x =0;
		for (int compteur = 0; compteur < string.length();compteur++){
			a = string.charAt(compteur);
			if (a != '.'){b= b +a ;}
			
			else{
				tampon[x] = Integer.parseInt(b);
				x++;
				b = "";
			}
		}
		Classe32bits Ip = new Classe32bits(tampon, true);
		return Ip;
	}*/
	/**
	 * Renvoye l'adresse IP RÃ©seau
	 * @param Ip
	 * @param masque
	 * @return
	 *
	public Classe32bits getNetworkIp(Classe32bits Ip,int prefix){
		int[] tabOctet = {0,0,0,0};
		for (int i = 0;i<4;i++){
			tabOctet[i] = Ip.getOctet()[i];
		}
		int compteur = 32-prefix;
		for (int i = 3;i >= 0;i--){
			if (compteur-8 >= 0){
				tabOctet[i] = 00000000;
			}
			else{
				if (compteur-8 >-8 && compteur-8 <=0){
					int reste = Ip.getOctet()[i];
					for (int x = 0;x <compteur;x++){
						reste = reste /10;
					}
					for (int y = 0;y <compteur;y++){
						reste = reste *10;
					}
					tabOctet[i] = reste;
				}
			}
			compteur = compteur - 8;
		}
		Classe32bits ReseauIp = new Classe32bits(tabOctet,false);
		return ReseauIp;
	}*/
	/**
	 * Renvoye l'adresse IP Broadcast ( tout a 1 )
	 * @param Ip
	 * @param masque
	 * @return
	 *
	public Classe32bits getBroadcastIp(Classe32bits Ip,int prefix){
		int[] tabOctet = {0,0,0,0};
		for (int i = 0;i<4;i++){
			tabOctet[i] = Ip.getOctet()[i];
		}
		int compteur = 32-prefix;
		for (int i = 3;i >= 0;i--){
			if (compteur-8 >= 0){
				tabOctet[i] = 11111111;
			}
			else{
				if (compteur-8 >-8 && compteur-8 <=0){
					int reste = Ip.getOctet()[i];
					for (int x = 0;x <compteur;x++){
						reste = reste /10;
					}
					for (int y = 0;y <compteur;y++){
						reste = reste *10;
						reste++;
					}
					tabOctet[i] = reste;
				}
			}
			compteur = compteur - 8;
		}
		Classe32bits Broadcast = new Classe32bits(tabOctet,false);
		return Broadcast;
	}*/
	/**
	 * Renvoye le prefix par rapport au masque
	 * @param masque
	 * @return
	 *
	public int getPrefixFromMasque(Classe32bits masque) {
		int prefix = 0;
		for (int i = 0;i<4;i++){
			int tampon = masque.octet[i];
			for (int x = 0;x<8;x++){
				if (tampon%10 != 0){prefix++;}
				tampon = tampon /10;
			}
		}
		return prefix;
	}*/
	/**
	 * Renvoye la classe a laquelle appartiens l'ip
	 * @param Ip
	 * @return
	 *
	public String getClassesIP(Classe32bits Ip){
		String tabClasses[] = {"A","B","C","multicast","expÃ©rimentale"};
		String choix = "Inconnue";
		int firstIp = Ip.deci[0];
		if (firstIp <= 126){choix = tabClasses[0];}
		else {
			if (firstIp <= 191){choix = tabClasses[1];}
			else {
				if (firstIp <= 223){choix = tabClasses[2];}
				else {
					if (firstIp <= 239){choix = tabClasses[3];}
					else{
						if (firstIp <= 255){choix = tabClasses[4];}
					}
				}
			}
		}
		return choix;
	}*/
}