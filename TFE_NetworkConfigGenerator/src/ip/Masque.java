package ip;

public class Masque extends Classe32bits{
	public Masque() {
		super();
	}
	public Masque(int[] tab, boolean decimal) {
		super(tab, decimal);
	}
	/**
	 * Permet de recevoir le Masque réseau en Décimal
	 * @param prefix
	 * @return
	 */
	public Masque getMasqueFromPrefix(int prefix){
		int[] tabOctet = {11111111,11111111,11111111,11111111};
		Masque masque= new Masque(tabOctet,false);
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
		Masque masque2 = new Masque(masque.octet,false);
		return masque2;
	}
	/**
	 * Renvoye le prefix par rapport au masque
	 * @param masque
	 * @return
	 */
	public static int getPrefixFromMasque(Masque masque) {
		int prefix = 0;
		for (int i = 0;i<4;i++){
			int tampon = masque.octet[i];
			for (int x = 0;x<8;x++){
				if (tampon%10 != 0){prefix++;}
				tampon = tampon /10;
			}
		}
		return prefix;
	}
}
