package objects;

import java.awt.Point;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;
import packSystem.SubnetUtils;

public class Network {

	private SubnetUtils network;
	private boolean firstIPRouter = true;
	private ArrayList<Hardware> HardwaresList = new ArrayList<>();
	private ArrayList<Connection> Connections  = new ArrayList<>();
	private String[] FreeIp ;
	private int IpCount = 0;

	public Network(SubnetUtils newGlobal) {
		this.network = newGlobal;
		setFreeIp(newGlobal.getInfo().getAllAddresses());
	}

	public void addHardware(Hardware r,Point location) {
		r.setLocation(location);
		HardwaresList.add(r);
		IpCount++;	
	}

	public String printConfig(int harwareID) {
		ArrayList<Connection> allConnections = this.getConnectionsof(harwareID);
		String config = "Configuration" + "\n";
		for(Connection c : allConnections){
			config += "interface " + c.getCompoName(harwareID) + "\n";
			if(harwareID == c.getFirstCompo()){
				config += "ip address " + c.getCompoIP1() +" "+ c.getSubnetwork().getInfo().getNetmask().toString() + "\n";			
			}
			else {
				config += "ip address " + c.getCompoIP2() +" "+ c.getSubnetwork().getInfo().getNetmask().toString() + "\n";			
			}
		}
		return config;
	}

	public ArrayList<Hardware> getAllHardwares() { 
		return HardwaresList;
	}
	public Hardware getHardware(int e){
		return HardwaresList.get(e);
	}
	public String[] getFreeIp() {
		return FreeIp;
	}

	public void setFreeIp(String[] freeIp) {
		this.FreeIp = freeIp;
	}

	public ArrayList<Connection> getConnections(ArrayList<Integer> connection) {
		ArrayList<Connection> tabC = new ArrayList<Connection>();
		for(int i=0;i<connection.size();i++){
			for (Connection c : this.Connections) {
				if (c.getConnectionID() == connection.get(i)){
					tabC.add(c);
				}
			}			
		}
		return tabC;
	}

	public void addConnection(int r1ID, int r2ID, Connection co) {
		this.Connections.add(co);
		setLinked(r1ID, co);
		setLinked(r2ID, co);		
	}
	private void setLinked(int compo,Connection co){
		if(this.HardwaresList.get(compo).getType() == HardwaresListS.USER_PC){		
			((UserPC)this.HardwaresList.get(compo)).setLinked(true);
			if (this.HardwaresList.get(compo).getID() == co.getFirstCompo()){
				((UserPC)this.HardwaresList.get(compo)).setGateway(co.getCompoIP2());	
			}
			else {
				((UserPC)this.HardwaresList.get(compo)).setGateway(co.getCompoIP2());
			}
		}
		this.HardwaresList.get(compo).addConnection(co.getConnectionID());
	}
	public int getConnectionCount(){
		return this.Connections.size();
	}
	public int getHardwaresCount(){
		return this.HardwaresList.size();
	}

	public ArrayList<Connection> getAllConnections() {		
		return this.Connections;
	}

	public ArrayList<Connection> getConnectionsof(int compoID){
		ArrayList<Connection> result = new ArrayList<Connection> ();
		for(Connection c : this.Connections){
			if (c.getFirstCompo() == compoID || c.getSecondCompo() == compoID){
				result.add(c);
			}
		}
		return result;
	}
	public void removeHard(int id) {	
		Hardware deleting = this.getHardware(id);	
		for(Connection i : this.getConnectionsof(id)){		
			this.Connections.remove(getConnectionIndex(i.getConnectionID()));
		}
		this.HardwaresList.remove(getHardwareIndex(deleting.getID()));
	}
	private int getHardwareIndex(int id) {
		int index = 0;
		for(index=0;index < this.HardwaresList.size();index++){
			if (this.HardwaresList.get(index).getID() == id){
				return index;	
			}
		}
		return index;
	}

	private int getConnectionIndex(int id) {
		int index = 0;
		for(index=0;index < this.Connections.size();index++){
			if (this.Connections.get(index).getConnectionID() == id){
				return index;
			}
		}
		return index;
	}

	public void load(JSONObject networkJSON){
		String ip = (String) networkJSON.get("IP");
		this.network = new SubnetUtils(ip);
		this.HardwaresList = new ArrayList<>();
		this.Connections  = new ArrayList<>();
		JSONArray hardwaresJSON = (JSONArray) networkJSON.get("hardwares");
		for(int i =0;i<hardwaresJSON.size();i++){
			JSONObject h = (JSONObject) hardwaresJSON.get(i);
			Hardware r ;
			if ((int)(long)h.get("type") == HardwaresListS.USER_PC){
				r = new UserPC(network, this.HardwaresList.size());
				((UserPC)r).setGateway((String)h.get("gateway"));
				((UserPC)r).setLinked((Boolean)h.get("linked"));
				((UserPC)r).setIp((String)h.get("ip"));
			}
			else {
				r = new Router(network,this.HardwaresList.size());
				((Router)r).setSecret((String)h.get("secret"));
				((Router)r).setPassword((String)h.get("password"));				
			}
			r.setLocation((int) (long)h.get("positionX"),(int) (long) h.get("positionY"));
			r.setID((int) (long) h.get("id"));
			r.setHostname((String)h.get("hostname"));

			this.addHardware(r, r.getLocation());
		}

		JSONArray connectionJSON = (JSONArray) networkJSON.get("connections");
		for(int i =0;i<connectionJSON.size();i++){
			JSONObject c = (JSONObject) connectionJSON.get(i);

			Connection co = new Connection(this,(int)(long)c.get("type"), (int)(long)c.get("compoID1"),(int)(long) c.get("compoID2"),this.Connections.size());
			co.setCompoName(co.getFirstCompo(), (String) c.get("nameID1"));
			co.setCompoName(co.getSecondCompo(), (String) c.get("nameID2"));
			co.setCompoIP1((String)c.get("compoIP1"));
			co.setCompoIP2((String)c.get("compoIP2"));
			co.setCompoName(co.getFirstCompo(),(String) c.get("compoName1"));
			co.setCompoName(co.getSecondCompo(),(String) c.get("compoName2"));
			this.addConnection((int)(long)c.get("compoID1"), (int)(long) c.get("compoID2"), co);	
		}
	}
	@SuppressWarnings("unchecked")
	public String save() {
		JSONObject networkJSON = new JSONObject();		

		networkJSON.put("IP", network.getInfo().getCidrSignature());
		JSONArray hardwaresJSON = new JSONArray();
		for(Hardware h : HardwaresList){
			JSONObject hJSON = new JSONObject();
			if (h.getType() == HardwaresListS.ROUTER){				
				hJSON.put("secret",((Router) h).getSecret());
				hJSON.put("password",((Router)h).getPassword());	

			}
			else {
				hJSON.put("gateway", ((UserPC)h).getGateway());
				hJSON.put("linked",((UserPC)h).isLinked());
				hJSON.put("ip",((UserPC)h).getIP());
			}
			hJSON.put("id",h.getID());
			hJSON.put("hostname",h.getHostname());
			hJSON.put("positionX",h.getLocation().x);
			hJSON.put("positionY",h.getLocation().y);
			hJSON.put("type", h.getType());
			hardwaresJSON.add(hJSON);
		}
		JSONArray connectionJSON = new JSONArray();
		for(Connection c : Connections){
			JSONObject cJSON = new JSONObject();
			cJSON.put("connectionID", c.getConnectionID());
			cJSON.put("type",c.getType());
			cJSON.put("IP",c.getSubnetwork().getInfo().getCidrSignature());
			cJSON.put("compoID1", c.getFirstCompo()); 
			cJSON.put("compoIP1", c.getCompoIP1());  
			cJSON.put("compoID2", c.getSecondCompo()); 
			cJSON.put("compoIP2", c.getCompoIP2());
			cJSON.put("compoName1", c.getCompoName(c.getFirstCompo()));
			cJSON.put("compoName2", c.getCompoName(c.getSecondCompo()));
			connectionJSON.add(cJSON);
		}

		networkJSON.put("hardwares", hardwaresJSON);
		networkJSON.put("connections", connectionJSON);
		return networkJSON.toJSONString();
	}
	public String getInterfaceName(int compoID, int type){
		int count = 0;
		String name = "";
		for(Connection c : getConnectionsof(compoID)){
			if (c.getType() == type){
				count++;
			}
		}
		switch(type) {
		case ConnectionsTypes.ETHERNET : name ="FastEthernet"+count+"/0";break;
		case ConnectionsTypes.SERIAL : name ="Serial"+count+"/0";break;
		case ConnectionsTypes.GIGABIT : name ="GigabitEthernet"+count+"/0";break;
		}
		return name;
	}

	public SubnetUtils getSubnet() {
		return this.network;
	}
}
