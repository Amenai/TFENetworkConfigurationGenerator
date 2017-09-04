package objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ListsSystem.ConnectionsTypes;
import ListsSystem.HardwaresListS;
import controller.SubnetUtils;

public class Network {

	private boolean firstIPRouter = true;
	private HashMap<Integer,Hardware> hardwaresList = new HashMap<Integer,Hardware>();
	private HashMap<Integer,Connection> connectionsList  = new HashMap<Integer,Connection>();
	private HashMap<Integer,Vlan> vlanList  = new HashMap<Integer,Vlan>();

	public Network() {
		try {
			addVlan(new Vlan(SubnetUtils.getIp("192.168.0.0/25"), 0, "Global"));
			/*addVlan(new Vlan(SubnetUtils.getIp("1.168.0.128/24"),1,""));
			addVlan(new Vlan(SubnetUtils.getIp("3.168.0.160/27"),99,"Admin"));
			addVlan(new Vlan(SubnetUtils.getIp("1.168.0.192/26"),2,"BOB"));*/
		} catch (Exception e) {
		}		
		System.out.println("GLOBAL = " + vlanList.get(0).getSubnetwork().getInfo().getCidrSignature());
	}

	public Network(String subnet) {
		try {
			addVlan(new Vlan(SubnetUtils.getIp(subnet), 0, "Global"));
		} catch (Exception e) {
		}		
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
			this.vlanList.get(tab.get(i).getVlanID()).removeConnectionInVlan(tab.get(i).getConnectionID());
		}

		this.hardwaresList.remove(deleting.getID());

	}
	/**
	 * SEULEMENT 1 SEUL ! entre chaque
	 * Interface utilisée ont besoin d'un "NO SHUTDOWN" pour fonctionner.
	 * @param harwareID
	 * @return
	 */
	public String printConfig(int harwareID) {
		ArrayList<Connection> allConnections = this.getConnectionsof(harwareID);
		String config = "";
		switch(this.getHardware(harwareID).getType()){
		case HardwaresListS.ROUTER : 
			Router r = (Router) this.getHardware(harwareID);
			config = HardwaresListS.CONFIGU1;
			config+= "hostname " +r.getHostname()+ "\r\n!\r\n";
			config+= "enable secret " + r.getSecret() + "\r\n";
			config+= "enable password " + r.getPassword()+ "\r\n!\r\n";
			for(Connection co : allConnections){				
				config += "interface " + co.getCompoName(harwareID) + "\r\n";
				if(harwareID == co.getFirstCompo()){
					if(this.getHardware(co.getSecondCompo()).getType() == HardwaresListS.SWITCH){
						SwitchRouterConnection co2 = (SwitchRouterConnection) co;
						for(int subCo : co2.getSubinterface()){
							config+="no ip "+ "\r\n!\r\n";
							config+="no shutdown "+ "\r\n!\r\n";
							Connection subConnect = this.getConnection(subCo);
							config += "interface " + subConnect.getCompoName(harwareID)+"."+subConnect.getVlanID() + "\r\n";
							config += "encapsulation dot1Q" + subConnect.getVlanID() +"\r\n";
							config += "ip address " + subConnect.getCompoIP1() +" "+ subConnect.getSubnetwork().getInfo().getNetmask().toString() + "\r\n";
						}
					}
					else {
						config += "ip address " + co.getCompoIP1() +" "+ co.getSubnetwork().getInfo().getNetmask().toString() + "\r\n";			
					}
				}
				else {
					if(this.getHardware(co.getFirstCompo()).getType() == HardwaresListS.SWITCH){
						SwitchRouterConnection co2 = (SwitchRouterConnection) co;
						for(int subCo : co2.getSubinterface()){
							config+="no ip "+ "\r\n!\r\n";
							config+="no shutdown "+ "\r\n!\r\n";
							Connection subConnect = this.getConnection(subCo);
							config += "interface " + subConnect.getCompoName(harwareID)+"."+subConnect.getVlanID() + "\r\n";
							config += "encapsulation dot1Q" + subConnect.getVlanID() +"\r\n";
							config += "ip address " + subConnect.getCompoIP2() +" "+ subConnect.getSubnetwork().getInfo().getNetmask().toString() + "\r\n";
						}
					}
					else{
						config += "ip address " + co.getCompoIP2() +" "+ co.getSubnetwork().getInfo().getNetmask().toString() + "\r\n";	
					}

				}
				config+="no shutdown "+ "\r\n!\r\n";
			}			
			break;
		case HardwaresListS.SWITCH : 
			// IF second compo = ROUTER => switchport mode trunk
			// IF Second compo = USER PC => switchport mode access 
			//								switchport access vlan X
			Switch s = (Switch) this.getHardware(harwareID);
			for(Vlan i : s.getAllVlans().values()){
				config+= "vlan "+ i.getNum() + "\r\n";	
				config+= "name "+ i.getName() + "\r\n!\r\n";
			}
			for(Connection co : allConnections){
				config += "interface " + co.getCompoName(harwareID) + "\r\n";
				if(harwareID == co.getFirstCompo()){
					if(this.hardwaresList.get(co.getSecondCompo()).getType() == HardwaresListS.USER_PC){
						config += "switchport mode access "+ "\r\n";
						config += "switchport access vlan "+ co.getVlanID() + "\r\n";
					}
					else {
						config += "switchport mode trunk" + "\r\n";
					}
				}
				else {
					if(this.hardwaresList.get(co.getSecondCompo()).getType() == HardwaresListS.USER_PC){
						config += "switchport mode access "+ "\r\n";
						config += "switchport access vlan "+ co.getVlanID() + "\r\n";
					}
					else {
						config += "switchport mode trunk " + "\r\n";
					}
				}
				config+= "!\r\n";
			}
			break;
		}
		config+="end";
		return config;
	}

	public HashMap<Integer,Hardware> getAllHardwares() {
		return hardwaresList;
	}
	public Hardware getHardware(int e){		
		Hardware h = hardwaresList.get(e);
		return h;
	}
	public Connection getConnection(int co){
		return this.connectionsList.get((Integer)co);
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

	public void addConnection(int vlanID,int h1ID, int h2ID, Connection co) {
		this.connectionsList.put(co.getConnectionID(),co);
		this.vlanList.get(vlanID).addConnectionInVlan(co.getConnectionID());
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
		this.connectionsList.remove(co.getConnectionID());
		co.remove();		
		if(this.hardwaresList.get(co.getSecondCompo()).getType() == HardwaresListS.USER_PC){	
			((UserPC)this.hardwaresList.get(co.getSecondCompo())).reset();
		}
		if(this.hardwaresList.get(co.getFirstCompo()).getType() == HardwaresListS.USER_PC){		
			((UserPC)this.hardwaresList.get(co.getFirstCompo())).reset();
		}
		if(this.hardwaresList.get(co.getFirstCompo()).getType() == HardwaresListS.SWITCH){
			//SWITCH - PC
			HashMap<Integer, Integer> a = ((Switch)this.hardwaresList.get(co.getFirstCompo())).getAllSRCo();
			for(Integer coID : a.values()){
				((SwitchRouterConnection)this.connectionsList.get(coID)).removeSubInterface(co.getConnectionID());
			}
		}
		if(this.hardwaresList.get(co.getSecondCompo()).getType() == HardwaresListS.SWITCH){
			// ROUTER - SWITCH
			((Switch)this.hardwaresList.get(co.getSecondCompo())).removeSR(co.getConnectionID());

		}
		deleteLink(co.getFirstCompo(),co.getConnectionID());
		deleteLink(co.getSecondCompo(),co.getConnectionID());

	}

	private void setLinked(int compo,Connection co){
		if(this.hardwaresList.get(compo).getType() == HardwaresListS.USER_PC){			
			((UserPC)this.hardwaresList.get(compo)).setLinked(true);
			((UserPC)this.hardwaresList.get(compo)).setIp(co.getCompoIP2(), co.getVlanID());
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
	//TODO
	public Connection getConnectionFromVlan(int vlan,int switchID, int hardType){
		Switch s = (Switch)this.getHardware(switchID);
		ArrayList<Connection> connections = this.getConnectionsof(switchID);
		for (Connection co : connections){
			if(co.getVlanID() == vlan){
				if(co.getFirstCompo() == switchID){
					if(this.getHardware(co.getSecondCompo()).getType() == hardType){
						return co;
					}					
				}
				else {
					if(this.getHardware(co.getFirstCompo()).getType() == hardType){
						return co;
					}	
				}
			}
		}
		return null;
	}
	public int getFreeHardwareCount() {
		if (hardwaresList.isEmpty() == false) {
			for (int i = 0; i <= hardwaresList.size(); i++) {
				if (hardwaresList.containsKey(i) == false) {
					return i;
				}
			}
		}
		return hardwaresList.size();
	}
	public HashMap<Integer, Vlan> getVlans() {
		return this.vlanList;
	}
	public void addVlan(Vlan vlan) throws Exception{
		boolean error = false;
		for (Vlan v : this.vlanList.values()){
			if(SubnetUtils.checkOverlapping(vlan.getSubnetwork(),v.getSubnetwork())){

				String message = ("Overlapping network : " + "\n" + v.getName() + "   :   " + v.getSubnetwork().getInfo().getCidrSignature()
						+ "\n" + vlan.getName() + "   :   " + vlan.getSubnetwork().getInfo().getCidrSignature());
				error = true;
				throw new Exception(message);
			}			
		}
		if (!error){
			this.vlanList.put(vlan.getNum(), vlan);
		}
	}
	public void removeVlan(int vlanID) {
		Vlan v = this.vlanList.get(vlanID);
		ArrayList<Integer> coList = v.getConnectionsList();
		for(Integer x : coList){
			removeConnection(this.connectionsList.get((Integer)x));			
		}
		this.vlanList.get(vlanID).removeConnectionList(coList);
		this.vlanList.remove(vlanID, this.vlanList.get(vlanID));
	}
	public void changingVlan (int neededIP,ArrayList<Connection> connectionList,int newVlanID){
		System.out.println("CHANGING VLAN FOR : " + neededIP);
		System.out.println("NEW VLAN ID = " + newVlanID);
		int availableIP = this.vlanList.get((Integer)newVlanID).getSubnetwork().getInfo().getAllFreeAddress().size();
		if (neededIP > availableIP){
			packSystem.Messages.showErrorMessage("Not enought IP in new VLAN" + "\n" +"IP needed : "+ neededIP + "\n" + "IP available :" + availableIP);
		}
		else {
			Vlan newVlan = this.vlanList.get(newVlanID);			
			for (Connection co : connectionList){
				System.out.println("" + newVlan.getSubnetwork().getInfo().getCidrSignature() +"->" + co.getSubnetwork().getInfo().getCidrSignature());
				Connection newCo = new Connection(newVlan, co.getType(), co.getFirstCompo(), co.getSecondCompo(), co.getConnectionID(), this.getInterfaceName(co.getFirstCompo(),co.getType()), this.getInterfaceName(co.getSecondCompo(),co.getType()),false);

				this.removeConnection(co);

				newCo.setCompoIP(newVlan.getSubnetwork().getInfo().getFirstFreeIP(),newCo.getFirstCompo(),false);
				newCo.setCompoIP(newVlan.getSubnetwork().getInfo().getFirstFreeIP(),newCo.getSecondCompo(),false);
				if (this.getHardware(newCo.getSecondCompo()).getType() == HardwaresListS.USER_PC){
					((UserPC)this.getHardware(newCo.getSecondCompo())).setIp(newCo.getCompoIP2(),newVlan.getNum());
				}
				this.addConnection(newVlanID, newCo.getFirstCompo(), newCo.getSecondCompo(), newCo);
			}
		}
	}
	public Vlan getGlobal() {
		return this.vlanList.get(0);
	}

	public SubnetUtils getSubnet(int vlanID) {
		return this.vlanList.get(vlanID).getSubnetwork();
	}
	public boolean newSubnetwork(int oldNum, SubnetUtils newSubnet) {
		for (Vlan v : this.vlanList.values()){
			if (v.getNum() != oldNum){
				if(SubnetUtils.checkOverlapping(newSubnet,v.getSubnetwork())){

					packSystem.Messages.showErrorMessage("Overlapping network : " + "\n" + v.getName() + "   :   " + v.getSubnetwork().getInfo().getCidrSignature()
							+ "\n" + this.vlanList.get(oldNum).getName() + "   :   " + this.vlanList.get(oldNum).getSubnetwork().getInfo().getCidrSignature());					
				}
				else {
					this.vlanList.get(oldNum).setSubnetwork(newSubnet);
					return true;
				}
			}
		}
		return false;
	}
	public boolean isFirstIPRouter() {
		return firstIPRouter;
	}

	public void setFirstIPRouter(boolean firstIPRouter) {
		this.firstIPRouter = firstIPRouter;
	}


	/**
	 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------
	 * @throws Exception 
	 * 
	 */
	public void load(JSONObject networkJSON) throws Exception{

		this.vlanList = new HashMap<Integer,Vlan>();
		this.hardwaresList = new HashMap<Integer,Hardware>();
		this.connectionsList  = new HashMap<Integer,Connection>();

		// GET VLANS
		JSONArray vlanJSON = (JSONArray) networkJSON.get("vlans");
		for(int i =0; i<vlanJSON.size();i++){
			JSONObject vlan = (JSONObject) vlanJSON.get(i);
			int num = (int) (long)vlan.get("num");
			String name = (String) vlan.get("name");
			SubnetUtils network = new SubnetUtils((String)vlan.get("subnetwork"));
			Vlan v = new Vlan(network, num, name);
			addVlan(v);
		}
		// GET HARDWARES
		JSONArray hardwaresJSON = (JSONArray) networkJSON.get("hardwares");
		for(int z =0; z<hardwaresJSON.size();z++){
			JSONObject hardware = (JSONObject) hardwaresJSON.get(z);
			switch((int) (long)hardware.get("type")){
			case HardwaresListS.ROUTER : 				

				Router router = new Router((int) (long) hardware.get("id"),(String) hardware.get("hostname"));

				router.setSecret((String) hardware.get("secret"));
				router.setPassword((String) hardware.get("password"));	
				this.addHardware(router, new Point((int) (long) hardware.get("positionX"),(int) (long) hardware.get("positionY")));

				break;
			case HardwaresListS.USER_PC : 

				UserPC user = new UserPC((int) (long) hardware.get("id"),(String) hardware.get("hostname"));

				user.setGateway((String)hardware.get("gateway"));
				user.setLinked((boolean) hardware.get("linked"));	
				//user.setIp((String)hardware.get("ip"), (int)(long)hardware.get("vlanID"));
				this.addHardware(user, new Point((int) (long) hardware.get("positionX"),(int) (long) hardware.get("positionY")));

				break;
			case HardwaresListS.SWITCH : 
				Switch switc = new Switch((int) (long) hardware.get("id"),(String) hardware.get("hostname"));
				HashMap<Integer, Integer> srCo = new HashMap<>();
				JSONArray SRCoJSON = (JSONArray) hardware.get("srConnections");				
				for(int xc =0; xc<SRCoJSON.size();xc++){
					JSONObject SRCJson = (JSONObject) SRCoJSON.get(xc);
					int value = (int)(long)SRCJson.get("value");
					int key = (int)(long)SRCJson.get("key");			
					srCo.put(key, value);
				}
				switc.setSRConnection(srCo);

				this.addHardware(switc, new Point((int) (long) hardware.get("positionX"),(int) (long) hardware.get("positionY")));

				break;
			};			
		}
		// GET CONNECTIONS
		JSONArray connectionJSON = (JSONArray) networkJSON.get("connections");
		for(int e =0; e<connectionJSON.size();e++){
			JSONObject connection = (JSONObject) connectionJSON.get(e);
			int connectionID = (int) (long)connection.get("connectionID");
			int type = (int) (long)connection.get("type");
			int compoID1 = (int) (long)connection.get("compoID1");
			int compoID2 = (int) (long)connection.get("compoID2");
			String name1 = (String)connection.get("compoName1");
			String name2 = (String)connection.get("compoName2");
			int vlanID = (int) (long)connection.get("vlanID");

			boolean isSub = (boolean)connection.get("isSub");
			String compoIP1 = (String)connection.get("compoIP1");
			String compoIP2 = (String)connection.get("compoIP2");
			
			if(isSub){
				SwitchRouterConnection co = new SwitchRouterConnection(this.vlanList.get(vlanID), type, compoID1, compoID2, connectionID, name1, name2, isSub);
				co.setCompoIP(compoIP1, compoID1,false);
				co.setCompoIP(compoIP2, compoID2,false);
				this.addConnection(vlanID, compoID1, compoID2, co);
			}
			else {
				Connection co = new Connection(this.vlanList.get(vlanID), type, compoID1, compoID2, connectionID, name1, name2, isSub);
				co.setCompoIP(compoIP1, compoID1,false);
				co.setCompoIP(compoIP2, compoID2,false);
				this.addConnection(vlanID, compoID1, compoID2, co);
			}
			
			
		}
	}
	@SuppressWarnings("unchecked")
	public String save() {
		JSONObject allSave = new JSONObject();		
		//ALL NETWORK INFO
		JSONArray networkJSON = new JSONArray();
		for(int key: this.vlanList.keySet()){
			Vlan v = this.vlanList.get(key);			
			networkJSON.add(v.getJSONObject());
		}
		//ALL HARDWARE INFO
		JSONArray hardwaresJSON = new JSONArray();
		for(int key: this.hardwaresList.keySet()){
			Hardware h = this.hardwaresList.get(key);			
			hardwaresJSON.add(h.getJSONObject());
		}
		//ALL CONNECTION INFO
		JSONArray connectionJSON = new JSONArray();
		for(int key: this.connectionsList.keySet()){
			Connection c = this.connectionsList.get(key);			
			connectionJSON.add(c.getJSONObject());
		}
		allSave.put("vlans", networkJSON);
		allSave.put("hardwares", hardwaresJSON);
		allSave.put("connections", connectionJSON);
		return allSave.toJSONString();
	}
}
