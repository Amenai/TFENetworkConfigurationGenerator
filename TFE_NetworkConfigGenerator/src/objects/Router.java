package objects;

import java.awt.Toolkit;

import packSystem.IPClass;

public class Router extends Hardware{

	private static String ImageFile = "D:/Amenai/Documents/Projets/TFE_NetworkConfigGenerator/src/router.png";
	
	public Router(String network) {		
		super(network, Toolkit.getDefaultToolkit().getImage(ImageFile));
	}

	private static String getGatewayAdress(String network) {
		return IPClass.getGatewayAddress();
	}
	
	public void printConfig() {
		System.out.println("Config Router = ");
		//System.out.println(this.IP);
	}
}
