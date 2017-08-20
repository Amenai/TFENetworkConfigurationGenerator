package objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import controller.SubnetUtils;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;

public class Network {

	private SubnetUtils network; // UN SEUL VLAN
	private boolean firstIPRouter = true;
	private HashMap<Integer,Hardware> hardwaresList = new HashMap<Integer,Hardware>();
	private HashMap<Integer,Connection> connectionsList  = new HashMap<Integer,Connection>();
	private String[] freeIp ;

	public Network(SubnetUtils newGlobal) {
		this.network = newGlobal;
		setFreeIp(newGlobal.getInfo().getAllAddresses());
	}

	public void addHardware(Hardware r,Point location) {
		r.setLocation(location);
		hardwaresList.put(r.getID(),r);
	}
	public void removeHard(int id) {
		Hardware deleting = this.getHardware(id);
		ArrayList<Connection> tab = this.getConnectionsof(id);
		for(int i = 0;i<tab.size();i++){
			removeConnection(tab.get(i));
		}
		this.hardwaresList.remove(deleting.getID());
		
	}
	/**
	 * TODO
	 * SEULEMENT 1 SEUL ! entre chaque
	 * Interface utilisée ont besoin d'un "NO SHUTDOWN" pour fonctionner.
	 * @param harwareID
	 * @return
	 */
	public String printConfig(int harwareID) {
		ArrayList<Connection> allConnections = this.getConnectionsof(harwareID);
		Router r = (Router)this.getHardware(harwareID);
		String config = HardwaresListS.CONF1;
		config+= "hostname " +r.getHostname()+ "\n";
		config+= HardwaresListS.CONF2;
		config+= "enable secret" + r.getSecret() + "\n";
		config+= "enable password " + r.getPassword()+ "\n";
		config+= HardwaresListS.CONF3;
		for(Connection c : allConnections){
			config += "interface " + c.getCompoName(harwareID) + "\n";
			if(harwareID == c.getFirstCompo()){
				config += "ip address " + c.getCompoIP1() +" "+ c.getSubnetwork().getInfo().getNetmask().toString() + "\n";			
			}
			else {
				config += "ip address " + c.getCompoIP2() +" "+ c.getSubnetwork().getInfo().getNetmask().toString() + "\n";			
			}
		}
		config+= HardwaresListS.CONF4;
		return config;
	}

	public HashMap<Integer,Hardware> getAllHardwares() {
		return hardwaresList;
	}
	public Hardware getHardware(int e){
		return hardwaresList.get(e);
	}
	public String[] getFreeIp() {
		return freeIp;
	}

	public void setFreeIp(String[] freeIp) {
		this.freeIp = freeIp;
	}

	public ArrayList<Connection> getConnections(ArrayList<Integer> connection) {
		ArrayList<Connection> tabC = new ArrayList<Connection>();
		for(int i=0;i<connection.size();i++){
			for (int key : this.connectionsList.keySet()) {
				if (this.connectionsList.get(key).getConnectionID() == connection.get(i)){
					tabC.add(this.connectionsList.get(key));
				}
			}			
		}
		return tabC;
	}

	public void addConnection(int h1ID, int h2ID, Connection co) {
		this.connectionsList.put(co.getConnectionID(),co);
		if((this.hardwaresList.get(h1ID).getType()) == HardwaresListS.USER_PC){	
			setLinked(h2ID, co);
			setLinked(h1ID, co);
		}
		else{
			setLinked(h1ID, co);
			setLinked(h2ID, co);
		}
	}
	public void removeConnection(Connection co){
		System.out.println("REMOVE : " + co.getConnectionID() + ": " + co.getCompoIP1() +"/" + co.getCompoIP2());
		this.connectionsList.remove(co.getConnectionID());
		co.remove();
		if(this.hardwaresList.get(co.getSecondCompo()).getType() == HardwaresListS.USER_PC){		
			System.out.println("RESET");
			((UserPC)this.hardwaresList.get(co.getSecondCompo())).reset();
		}
		if(this.hardwaresList.get(co.getFirstCompo()).getType() == HardwaresListS.USER_PC){		
			System.out.println("RESET");
			((UserPC)this.hardwaresList.get(co.getFirstCompo())).reset();
		}
		deleteLink(co.getFirstCompo(),co.getConnectionID());
		deleteLink(co.getSecondCompo(),co.getConnectionID());
	}

	private void setLinked(int compo,Connection co){
		if(this.hardwaresList.get(compo).getType() == HardwaresListS.USER_PC){
			((UserPC)this.hardwaresList.get(compo)).setLinked(true);
			((UserPC)this.hardwaresList.get(compo)).setGateway(co.getCompoIP1());
		}
		this.hardwaresList.get(compo).addConnection(co.getConnectionID());
	}
	private void deleteLink(int compo,int co) {
		this.hardwaresList.get(compo).deleteConnection(co);
	}
	public  HashMap<Integer,Connection> getAllConnections() {		
		return this.connectionsList;
	}
	public int getCoId() {
		for(int i=0;i<this.connectionsList.size();i++){
			if (this.connectionsList.get(i) == null){
				System.out.println("I="+i);
				return i;
			}
		}
		return this.connectionsList.size();
	}
	public ArrayList<Connection> getConnectionsof(int compoID){
		ArrayList<Connection> result = new ArrayList<Connection> ();
		for(int key : this.connectionsList.keySet()){
			if (this.connectionsList.get(key).getFirstCompo() == compoID || this.connectionsList.get(key).getSecondCompo() == compoID){
				result.add(this.connectionsList.get(key));
			}
		}
		return result;
	}


	public void load(JSONObject networkJSON){
		String ip = (String) networkJSON.get("IP");
		this.network = new SubnetUtils(ip);
		this.hardwaresList = new HashMap<Integer,Hardware>();
		this.connectionsList  = new HashMap<Integer,Connection>();
		JSONArray hardwaresJSON = (JSONArray) networkJSON.get("hardwares");
		for(int i =0;i<hardwaresJSON.size();i++){
			JSONObject h = (JSONObject) hardwaresJSON.get(i);
			Hardware r ;
			if ((int)(long)h.get("type") == HardwaresListS.USER_PC){
				r = new UserPC(network.getInfo().getLowAddress(), this.hardwaresList.size());
				((UserPC)r).setGateway((String)h.get("gateway"));
				((UserPC)r).setLinked((Boolean)h.get("linked"));
				((UserPC)r).setIp((String)h.get("ip"));
			}
			else {
				r = new Router(network.getInfo().getLowAddress(),this.hardwaresList.size());
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

			Connection co = new Connection(this,(int)(long)c.get("type"), (int)(long)c.get("compoID1"),(int)(long) c.get("compoID2"),this.connectionsList.size());
			co.setCompoName(co.getFirstCompo(), (String) c.get("nameID1"));
			co.setCompoName(co.getSecondCompo(), (String) c.get("nameID2"));
			co.setCompoIP((String)c.get("compoIP1"),(int)c.get("compoID1"));
			co.setCompoIP((String)c.get("compoIP2"),(int)c.get("compoID2"));
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
		for(int key: this.hardwaresList.keySet()){
			Hardware h = this.hardwaresList.get(key);
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
		for(int key: this.connectionsList.keySet()){
			Connection c = this.connectionsList.get(key);
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

	public int getFreeHardwareCount() {
		if (hardwaresList.isEmpty() == false) {
			for (int i = 0; i <= hardwaresList.size(); i++) {
				if (hardwaresList.containsKey(i) == false) {
					System.out.println("COUNT = " + i);
					return i;
				}
			}
		}
		System.out.println("COUNT "+hardwaresList.size());
		return hardwaresList.size();
	}

	public boolean isFirstIPRouter() {
		return firstIPRouter;
	}

	public void setFirstIPRouter(boolean firstIPRouter) {
		this.firstIPRouter = firstIPRouter;
	}
}
