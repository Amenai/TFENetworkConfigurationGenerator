package objects;

import java.awt.Toolkit;

import packSystem.IPClass;

public class Router extends Hardware{

	private static String ImageFile = "src/router.png";
	
	public Router(String network,int code) {		
		super(network, Toolkit.getDefaultToolkit().getImage(ImageFile),code);
	}

	private static String getGatewayAdress(String network) {
		return IPClass.getGatewayAddress();
	}
	
	public void printConfig() {
		System.out.println("Config Router = ");
		//System.out.println(this.IP);
	}
}
