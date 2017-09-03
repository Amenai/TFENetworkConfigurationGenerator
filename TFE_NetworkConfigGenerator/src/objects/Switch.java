package objects;

import java.awt.Toolkit;
import java.util.HashMap;

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

	public Switch(int code,String hostname) {
		super(ImageFile,code,HardwaresListS.SWITCH,hostname);
	}
	public void addVlans(Vlan vlan){
		System.out.println("ADD : "+vlan.getNum());
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
		return obj;
	}
	@Override
	public Hardware fromJSONObject(JSONObject obj) {
	return this;
	}
}
