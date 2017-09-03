package objects;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import controller.SubnetUtils;

public class Vlan {
	private int num = 0;
	private String name = "VLAN ";
	private SubnetUtils subnetwork;
	private ArrayList<Integer> connectionsList = new ArrayList<Integer>();
	public Vlan(SubnetUtils network, int num, String name) {
		this.setSubnetwork(network);
		this.setNum(num);
		if(name.equals("")){
			this.setName(this.name + num);
		}
		else {
			this.setName(name);
		}

	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public SubnetUtils getSubnetwork() {
		return subnetwork;
	}
	public void setSubnetwork(SubnetUtils subnetwork) {		 
		this.subnetwork = new SubnetUtils(subnetwork.getInfo().getNetworkAddress(),subnetwork.getInfo().getNetmask());;
	}
	public ArrayList<Integer> getConnectionsList() {
		return connectionsList;
	}
	public void removeConnectionInVlan(int co){
		this.connectionsList.remove((Integer)co);
	}
	public void addConnectionInVlan(int co){
		this.connectionsList.add((Integer)co);
		System.out.println("VLAN CONNECTION LIST = " + this.connectionsList.toString());
	}
	public void removeConnectionList(ArrayList<Integer> delete){
		for(int i = 0 ;i<delete.size() ;i++){
			this.connectionsList.remove((Integer)delete.get(i));
		}
	}
	@SuppressWarnings("unchecked")
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();      	
		obj.put("num", this.getNum());
		obj.put("name", this.getName());
		obj.put("subnetwork", this.getSubnetwork().getInfo().getCidrSignature());
		return obj;
	}
	@SuppressWarnings("unchecked")
	public void fromJSONObject(JSONObject obj) {

	}
}
