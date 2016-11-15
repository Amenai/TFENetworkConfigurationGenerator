package objects;

import java.awt.Toolkit;

import packSystem.IPClass;

public class UserPC extends Hardware{

	private static String ImageFile = "src/pc.png";
	public UserPC(String network,int code) {	
		super(network, Toolkit.getDefaultToolkit().getImage(ImageFile),code);
	}

	private static String getPersonnalAdress(String network) {
		return IPClass.getFirstOpenedAddress();
	}
	
	public void printConfig() {
		System.out.println("Config PC = ");
		//System.out.println(this.IP);
	}
}
