package objects;

import java.util.HashMap;

import controller.SubnetUtils;

public class Connection {


	private HashMap<Integer, MyValue> compo = new HashMap<Integer, MyValue>();
	//2 x ID,Name,IP
	private SubnetUtils subnetwork;
	private int connectionID;
	private int type;

	public Connection(Network network,int type,int compoID1,int compoID2,int connectionID){
		this.setSubnetwork(network.getSubnet());
		this.setType(type);

		this.compo.put(0, new MyValue(compoID1, network.getInterfaceName(compoID1, type)));
		this.compo.put(1, new MyValue(compoID2, network.getInterfaceName(compoID2, type)));
		this.setConnectionID(connectionID);
	}
	public String getCompoName(int compoID) {
		if (this.compo.get(0).getID() == compoID){
			return this.compo.get(0).getName();
		}
		else return this.compo.get(1).getName();
	}
	public void setCompoName(int compoID,String name) {
		if (this.compo.get(0).getID() == compoID){
			this.compo.get(0).setName(name);
		}
		else this.compo.get(1).setName(name);

	}
	public int getFirstCompo() {
		return this.compo.get(0).getID();
	}

	public void setFirstCompo(int compoID1){
		this.compo.get(0).setID(compoID1);
	}

	public int getSecondCompo() {
		return this.compo.get(1).getID();
	}

	public void setSecondCompo(int compoID2){
		this.compo.get(1).setID(compoID2);
	}

	public int getType() {		
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public SubnetUtils getSubnetwork() {
		return subnetwork;
	}

	public void setSubnetwork(SubnetUtils subnetwork) {
		this.subnetwork = subnetwork;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public String getCompoIP1() {
		return this.compo.get(0).getIP();
	}

	public String getCompoIP2() {
		return this.compo.get(1).getIP();
	}
	public boolean setCompoIP(String compoIP,int compoID) {	
		if( getFirstCompo() == compoID){
			if (this.subnetwork.getInfo().isFree(compoIP)){
				this.subnetwork.getInfo().setIPFree(this.compo.get(0).getIP(), true);
				this.compo.get(0).setIP(compoIP); 
				this.subnetwork.getInfo().setIPFree(compoIP, false);
			}
			else {System.out.println("WTF");}
		}
		else{
			if (this.subnetwork.getInfo().isFree(compoIP)){
				this.subnetwork.getInfo().setIPFree(this.compo.get(1).getIP(), true);
				this.compo.get(1).setIP(compoIP); 
				this.subnetwork.getInfo().setIPFree(compoIP, false);
			}
			else {System.out.println("WTF");}
		}
		return true;//TODO
	}
	public void remove(){
		this.subnetwork.getInfo().setIPFree(this.compo.get(0).getIP(), true);
		this.subnetwork.getInfo().setIPFree(this.compo.get(1).getIP(), true);
	}
	class MyValue {
		private int ID ;
		private String name ="";
		private String IP= "0.0.0.0";
		public MyValue(int ID, String name) {
			this.setName(name);
			this.setID(ID);
		}
		public int getID() {
			return ID;
		}
		public void setID(int iD) {
			ID = iD;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getIP() {
			return IP;
		}
		public void setIP(String iP) {
			IP = iP;
		}
		@Override
		public String toString(){
			return " \n"+"ID= "+this.ID+" \n"+"Name= "+this.name+" \n"+"IP= "+this.IP+" \n";
		}

	}
}
