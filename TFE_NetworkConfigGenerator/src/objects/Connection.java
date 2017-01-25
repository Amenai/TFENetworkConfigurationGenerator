package objects;

import packSystem.SubnetUtils;
import packSystem.SubnetUtils.SubnetInfo;

public class Connection {


	private int compoID1;
	private String compoIP1 = "0.0.0.0";
	private String compoName1 = "";
	private int compoID2;
	private String compoIP2 = "0.0.0.0";
	private String compoName2 = "";
	private int type;
	private SubnetUtils subnetwork;
	private int connectionID;

	public Connection(Network network,int type,int compoID1,int compoID2,int connectionID){
		this.setSubnetwork(network.getSubnet());
		this.setType(type);
		this.setFirstCompo(compoID1);
		this.setSecondCompo(compoID2);
		this.compoName1 = network.getInterfaceName(compoID1, type);
		this.compoName2 = network.getInterfaceName(compoID2, type);
		this.setConnectionID(connectionID);
	}
	public String getCompoName(int compoID) {
		if(compoID == compoID1){
			return this.compoName1;
		}
		else return this.compoName2;
	}

	public int getFirstCompo() {
		return this.compoID1;
	}

	public void setFirstCompo(int compoID1){
		this.compoID1=compoID1;
	}

	public int getSecondCompo() {
		return this.compoID2;
	}

	public void setSecondCompo(int compoID2){
		this.compoID2=compoID2;
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
		return compoIP1;
	}

	public String getCompoIP2() {
		return compoIP2;
	}
	public boolean setCompoIP1(String compoIP) {		
		if(this.subnetwork.getInfo().isInRange(compoIP)){
			//if (this.subnetwork.getInfo().isFree(compoIP)){
			this.subnetwork.getInfo().setIPFree(this.compoIP1, true);
			this.compoIP1 = compoIP;
			this.subnetwork.getInfo().setIPFree(compoIP, false);
			return true;
			//	}	else System.out.println("ERRRE");
		}
		return false;
	}
	public boolean setCompoIP2(String compoIP) {
		if(this.subnetwork.getInfo().isInRange(compoIP)){
			//if (this.subnetwork.getInfo().isFree(compoIP)){

			this.subnetwork.getInfo().setIPFree(this.compoIP2, true);
			this.compoIP2 = compoIP;
			this.subnetwork.getInfo().setIPFree(compoIP, false);
			return true;
			//	}else System.out.println("ERRRE");
		}
		return false;
	}
	public void setCompoName(int compoID,String name) {
		if(compoID == compoID1){
			this.compoName1 = name;
		}
		else this.compoName2 = name;

	}

}
