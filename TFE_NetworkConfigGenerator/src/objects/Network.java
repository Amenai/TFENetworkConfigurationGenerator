package objects;

import java.util.ArrayList;

import packSystem.HardwaresList;
import packSystem.IPClass;

public class Network {

	private String network = "192.168.0.0";
	private String mask = "255.255.255.0";
	private boolean firstIPRouter = true;
	private ArrayList<Hardware> Hardwares = new ArrayList<>();
	
	public Network(String newGlobal) {
		network = newGlobal;
	}

	public void addHardware(int hardware) {
		switch (hardware) {
		case HardwaresList.ROUTER :
			System.out.println("Add Routers : " + Hardwares.size());
			Hardwares.add(new Router(network,Hardwares.size()));
			break;
		case HardwaresList.USER_PC :
			System.out.println("Add PC : " + Hardwares.size());
			Hardwares.add(new UserPC(network,Hardwares.size()));
			break;
		}
	}

	public void printConfig() {
		for(Hardware devices : Hardwares) {
			devices.printConfig();
		}
	}

	public ArrayList<Hardware> getHardwares() { 
		return Hardwares;
	}
	
	public String getRouterIP(){
		if (firstIPRouter){
			return "192.168.0.1";
		}
		else return "192.168.0.255";
		
	}
	
	private static String getPersonnalAdress(String network) {
		return IPClass.getFirstOpenedAddress();
	}
}
