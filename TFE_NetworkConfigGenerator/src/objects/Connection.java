package objects;

import java.util.HashMap;

import org.json.simple.JSONObject;

import controller.SubnetUtils;

public class Connection {


	private HashMap<Integer, MyValue> compo = new HashMap<Integer, MyValue>();
	private SubnetUtils subnetwork;
	private int vlanID;
	private int connectionID;
	private int type;
	private boolean isSubInterface;

	public Connection(Vlan vlan,int type,int compoID1,int compoID2,int connectionID,String name1,String name2,boolean isSubInterface){
		this.subnetwork = (vlan.getSubnetwork());
		this.setVlanID(vlan.getNum());
		this.setType(type);
		this.setSubInterface(isSubInterface);
		this.compo.put(0, new MyValue(compoID1, name1));
		this.compo.put(1, new MyValue(compoID2, name2));
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

	/*public boolean setSsubnetwork(SubnetUtils subnetwork) {
		this.subnetwork = subnetwork;
		boolean t = setCompoIP(this.subnetwork.getInfo().getFirstFreeIP(), this.getFirstCompo(),false);
		t = setCompoIP(this.subnetwork.getInfo().getFirstFreeIP(), this.getSecondCompo(),false);
		return t;
	}*/

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
	public boolean setCompoIP(String compoIP,int compoID,boolean hack) {	
		//TODO
		if( getFirstCompo() == compoID){
			if (this.subnetwork.getInfo().isFree(compoIP) || hack){
				this.subnetwork.getInfo().setIPFree(this.compo.get(0).getIP(), true);
				this.compo.get(0).setIP(compoIP);
				this.subnetwork.getInfo().setIPFree(compoIP, false);
			}
		}
		else{
			if (this.subnetwork.getInfo().isFree(compoIP) || hack){
				this.subnetwork.getInfo().setIPFree(this.compo.get(1).getIP(), true);
				this.compo.get(1).setIP(compoIP); 
				this.subnetwork.getInfo().setIPFree(compoIP, false);
			}
		}
		return true;
	}
	public void remove(){
		this.subnetwork.getInfo().setIPFree(this.compo.get(0).getIP(), true);
		this.subnetwork.getInfo().setIPFree(this.compo.get(1).getIP(), true);
	}
	public int getVlanID() {
		return vlanID;
	}
	public void setVlanID(int vlanID) {
		this.vlanID = vlanID;
	}
	public boolean isSubInterface() {
		return isSubInterface;
	}
	public void setSubInterface(boolean isSubInterface) {
		this.isSubInterface = isSubInterface;
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("connectionID", this.getConnectionID());
		obj.put("type",this.getType());
		//obj.put("IP",this.getSubnetwork().getInfo().getCidrSignature());
		obj.put("compoID1", this.getFirstCompo()); 
		obj.put("compoIP1", this.getCompoIP1());  
		obj.put("compoID2", this.getSecondCompo()); 
		obj.put("compoIP2", this.getCompoIP2());
		obj.put("compoName1", this.getCompoName(this.getFirstCompo()));
		obj.put("compoName2", this.getCompoName(this.getSecondCompo()));
		obj.put("vlanID", this.getVlanID());
		obj.put("isSub", this.isSubInterface());
		return obj;
	}

	public void fromJSONObject(JSONObject obj) {

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
