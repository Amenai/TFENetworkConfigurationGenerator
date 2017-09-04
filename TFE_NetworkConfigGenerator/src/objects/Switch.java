package objects;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ListsSystem.HardwaresListS;
import controller.SubnetUtils;

public class Switch extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "/resources/switch.png";
	private HashMap<Integer,Vlan> vlansList  = new HashMap<Integer,Vlan>();
	// <VLAN ID , CO ID >
	private HashMap<Integer, Integer> SRConnection = new HashMap<Integer, Integer>();

	public Switch(int code,String hostname) {
		super(ImageFile,code,HardwaresListS.SWITCH,hostname);
	}
	public void addVlans(Vlan vlan){
		this.vlansList.put(vlan.getNum(), vlan);
	}
	public void removeVlans(int num){
		this.vlansList.remove(num);
	}
	public void setVlan(Vlan vlan){
		removeVlans(vlan.getNum());
		addVlans(vlan);
	}
	public Vlan getVlans(int i){
		return this.vlansList.get(i);
	}
	public HashMap<Integer, Vlan> getAllVlans(){
		return this.vlansList;
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();
		obj.put("type", this.getType());
		obj.put("id",this.getID());
		obj.put("hostname",this.getHostname());
		obj.put("positionX",this.getLocation().x);
		obj.put("positionY",this.getLocation().y);
		JSONArray tab = new JSONArray();
		for(int key: this.SRConnection.keySet()){
			int v = this.SRConnection.get(key);	
			JSONObject value = new JSONObject();
			value.put("key",key);
			value.put("value", v);
			tab.add(value);
		}
		obj.put("srConnections", tab);
		return obj;
	}
	@Override
	public Hardware fromJSONObject(JSONObject obj) {
		return this;
	}

	/*public String getIpFromVlan(int vlan){
		this.getConnection();
	}*/

	public void addSRCOtoSwitch(SwitchRouterConnection srCo){
		System.out.println("xx" + srCo.getConnectionID() + srCo.getVlanID());
		this.SRConnection.put(srCo.getVlanID(), srCo.getConnectionID());
		System.out.println("xx" + this.SRConnection.size());
	}
	public void setSRConnection(HashMap<Integer, Integer> sRConnection) {
		this.SRConnection = sRConnection;
	}

	public HashMap<Integer, Integer> getAllSRCo() {
		return this.SRConnection;
	}
	public Integer getSRConnection(int vlan) {	
		if(this.SRConnection.get(vlan) != null){
			return this.SRConnection.get(vlan);
		}
		else {
			return -1;
		}
	}
	public void addSubinterfacetoSR(int srCoID,int coID,Network n){
		((SwitchRouterConnection)n.getConnection(srCoID)).addSubInterface(coID);
		((SwitchRouterConnection)n.getConnection(srCoID)).checkSubInterface();
	}
	public void addSubinterfacetoAllSR(int coID, Network n) {
		for (Integer srCoID : this.SRConnection.values()){
			((SwitchRouterConnection)n.getConnection(srCoID)).addSubInterface(coID);
		}
	}
	public void removeSR(int connectionID) {
		this.SRConnection.remove(connectionID);
	}
}
