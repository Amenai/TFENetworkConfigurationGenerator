package objects;

import java.awt.Toolkit;

import packSystem.IPClass;

public class UserPC extends Hardware{

	private static String ImageFile = "D:/Amenai/Documents/Projets/TFE_NetworkConfigGenerator/src/pc.png";
	public UserPC(String network) {	
		super(network, Toolkit.getDefaultToolkit().getImage(ImageFile));
	}

	private static String getPersonnalAdress(String network) {
		return IPClass.getFirstOpenedAddress();
	}
	
	public void printConfig() {
		System.out.println("Config PC = ");
		//System.out.println(this.IP);
	}
}
