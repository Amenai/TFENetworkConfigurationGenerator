package ip;

public class Ip extends Classe32bits{

	public Ip() {
		super();
	}

	public Ip(int[] tab, boolean decimal) {
		super(tab, decimal);
	}

	/**
	 * Renvoye l'adresse IP Réseau
	 * @param Ip
	 * @param masque
	 * @return
	 */
	public Ip getNetworkIp(Ip Ip,int prefix){
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
		Ip ReseauIp = new Ip(tabOctet,false);
		return ReseauIp;
	}
	/**
	 * Renvoye l'adresse IP Broadcast ( tout a 1 )
	 * @param Ip
	 * @param masque
	 * @return
	 */
	public Ip getBroadcastIp(Ip Ip,int prefix){
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
		Ip Broadcast = new Ip(tabOctet,false);
		return Broadcast;
	}
	/**
	 * Renvoye la classe a laquelle appartiens l'ip
	 * @param Ip
	 * @return
	 */
	public static String getClassesIP(Ip Ip){
		String tabClasses[] = {"A","B","C","multicast","expérimentale"};
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
	}
}
