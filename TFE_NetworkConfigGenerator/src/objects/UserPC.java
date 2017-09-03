package objects;

import java.awt.Toolkit;

import org.json.simple.JSONObject;

import ListsSystem.HardwaresListS;
import controller.SubnetUtils;

public class UserPC extends Hardware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String ImageFile = "/resources/pc.png";
	private String gateway = "0.0.0.0";
	private boolean isLinked = false;
	private String IP = "0.0.0.0";
	private int vlanId;
	public UserPC(int code,String hostname) {
		super(ImageFile,code,HardwaresListS.USER_PC,hostname);
	}
	public String getGateway() {
		return this.gateway;
	}
	public void setGateway(String gateway){
		this.gateway = gateway;
	}
	public boolean isLinked() {
		return isLinked;
	}
	public void setLinked(boolean isLinked) {
		this.isLinked = isLinked;
	}
	public String getIP(){
		return this.IP ;
	}	
	public boolean setIp(String ip,int vlanID){
			this.IP = ip;
			this.vlanId=vlanID;
			return true;
	}
	public void reset(){
		this.gateway = "0.0.0.0";
		this.isLinked = false;
		this.IP = "0.0.0.0";
	}
	public void setVlan(int id){
		this.vlanId = id;
	}
	public int getVlan() {
		return this.vlanId;
	}
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();      	
		obj.put("type", this.getType());
		obj.put("gateway", this.getGateway());
		obj.put("linked",this.isLinked());
		obj.put("ip",this.getIP());
		obj.put("id",this.getID());		
		obj.put("hostname",this.getHostname());
		obj.put("positionX",this.getLocation().x);
		obj.put("positionY",this.getLocation().y);
		obj.put("vlanID",this.getVlan());
		return obj;
	}
	@Override
	public Hardware fromJSONObject(JSONObject obj) {
		return this;
	}
}
