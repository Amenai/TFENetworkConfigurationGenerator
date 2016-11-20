package objects;

import java.awt.Point;
import java.util.ArrayList;

import packSystem.HardwaresList;
import tests.SubnetUtils;

public class Network {

	private SubnetUtils network;
	private boolean firstIPRouter = true;
	private ArrayList<Hardware> Hardwares = new ArrayList<>();
	private String[] FreeIp ;
	private int IpCount = 0;
	
	public Network(SubnetUtils newGlobal) {
		network = newGlobal;
		setFreeIp(newGlobal.getInfo().getAllAddresses());
	}

	public void addHardware(int hardware,Point location) {
		Hardware h;
		switch (hardware) {
		case HardwaresList.ROUTER :
			System.out.println("Add Routers : " + Hardwares.size());
			h = new Router(FreeIp[IpCount],Hardwares.size());
			h.setLocation(location);
			Hardwares.add(h);
			
			IpCount++;
			break;
		case HardwaresList.USER_PC :
			System.out.println("Add PC : " + Hardwares.size());
			h = new UserPC(FreeIp[IpCount],Hardwares.size());
			h.setLocation(location);
			Hardwares.add(h);					
			IpCount++;
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
	
	public String[] getFreeIp() {
		return FreeIp;
	}

	public void setFreeIp(String[] freeIp) {
		FreeIp = freeIp;
	}
}
