package tests;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import objects.Connection;
import objects.Network;
import objects.Router;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.SubnetUtils;

public class JUnitTest {

	private Network network ;
	@SuppressWarnings("static-access")
	@Test
	public void alltest() {
		SubnetUtils adrs = new SubnetUtils("192.168.0.128","255.255.255.240");
		Assert.assertEquals(adrs.getInfo().getCidrSignature(),"192.168.0.128/28");
		adrs.getIp(adrs.getInfo().getCidrSignature());
		this.network= new Network(adrs);
		//Ajoute un router
		Router router = new Router(this.network.getSubnet(), this.network.getHardwaresCount());
		this.network.addHardware(router, new Point(300,300));		
		buildRouter(router);
		//Ajoute un router
		Router router2 = new Router(this.network.getSubnet(), this.network.getHardwaresCount());
		this.network.addHardware(router2, new Point(400,300));

		Assert.assertEquals(this.network.getHardwaresCount(), 2);
		//Ajoute une connection R1-R2
		Connection con = new Connection(this.network, ConnectionsTypes.ETHERNET,router.getID(), router2.getID(), this.network.getConnectionCount());
		this.network.addConnection(router.getID(), router2.getID(), con);
		buildConnection(con);

		Assert.assertEquals(this.network.getHardwaresCount(),2);
		Assert.assertEquals(this.network.getConnectionCount(),1);
		//Ajoute un router
		Router router3 = new Router(this.network.getSubnet(), this.network.getHardwaresCount());
		this.network.addHardware(router3, new Point(500,300));		
		//Ajoute une connection R3-R1
		Connection con2 = new Connection(this.network, ConnectionsTypes.SERIAL,router3.getID(), router.getID(), this.network.getConnectionCount());
		this.network.addConnection(router3.getID(), router.getID(), con2);

		Assert.assertEquals(this.network.getHardwaresCount(),3);
		Assert.assertEquals(this.network.getConnectionCount(),2);
		//Supprimer le router 2 et toutes ces connections
		this.network.removeHard(router2.getID());
		Assert.assertEquals(this.network.getHardwaresCount(),2);
		Assert.assertEquals(this.network.getConnectionCount(),1);
		//Ajoute un PC
		UserPC pc = new UserPC(this.network.getSubnet(), this.network.getHardwaresCount());
		this.network.addHardware(pc, new Point(200,200));		

		Assert.assertEquals(this.network.getHardwaresCount(),3);
		Assert.assertEquals(this.network.getConnectionCount(),1);
		//Vérifie le réseau
		checkNetwork();
		//Sauvegarde
		saving();
		//Reset
		newNetwork();
		//Vérification du reset
		Assert.assertEquals(this.network.getHardwaresCount(),0);
		Assert.assertEquals(this.network.getConnectionCount(),0);
		Assert.assertEquals(this.network.getSubnet().getInfo().getCidrSignature(),"192.168.0.1/24");
		//Ouverture du réseau enregistré
		loading();
		//Vérification de l'ouverture
		Assert.assertEquals(this.network.getHardwaresCount(),3);
		Assert.assertEquals(this.network.getConnectionCount(),1);
		Assert.assertEquals(this.network.getSubnet().getInfo().getCidrSignature(),"192.168.0.128/28");
	}
	/**
	 * Ouvre un réseau déjà enregistré
	 */
	private void loading() {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("src/testSaving.txt"));
			JSONObject networkJSON = (JSONObject) obj;
			this.network.load(networkJSON);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * Reset le réseau
	 */
	@SuppressWarnings("unchecked")
	private void newNetwork() {
		try {
			//{"IP":"192.168.0.0\/24","connections":[],"hardwares":[]}
			JSONObject networkJSON = new JSONObject();
			networkJSON.put("IP", "192.168.0.1/24");
			JSONArray conJSON = new JSONArray();
			networkJSON.put("connections", conJSON);
			networkJSON.put("hardwares", conJSON);

			this.network.load(networkJSON);
		}catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Sauvegarde dans un fichier
	 */
	private void saving() {
		String path = "src/testSaving.txt";
		String lines = this.network.save();
		try (FileWriter file = new FileWriter(path)) {
			file.write(lines);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file2 = new File(path);
		Assert.assertTrue(file2.exists());
	}

	/**
	 * Teste le contenu du réseau
	 */
	private void checkNetwork() {
		Connection co = this.network.getAllConnections().get(0);
		Assert.assertEquals(co.getFirstCompo(),2);
		Assert.assertEquals(co.getSecondCompo(),0);
		Assert.assertEquals(co.getType(),ConnectionsTypes.SERIAL);

		Router router = (Router)this.network.getHardware(0);
		Assert.assertEquals(router.getHostname(),"R1");
		Router router2 = (Router) this.network.getHardware(1);
		Assert.assertEquals(router2.getHostname(),"R3");
		UserPC pc = (UserPC) this.network.getHardware(2);
		Assert.assertEquals(pc.getHostname(),"U3");
	}

	/**
	 * Teste une connection
	 * @param con
	 */
	private void buildConnection(Connection con) {
		Assert.assertFalse(con.setCompoIP1("192.168.0.1"));
		Assert.assertFalse(con.setCompoIP1("192.168.10.1"));
		Assert.assertFalse(con.setCompoIP1("192.168.1.252"));
		Assert.assertTrue(con.setCompoIP1("192.168.0.130"));
		Assert.assertEquals(con.getType(),ConnectionsTypes.ETHERNET);
		con.setType(ConnectionsTypes.GIGABIT);
		Assert.assertEquals(con.getType(),ConnectionsTypes.GIGABIT);
		con.setType(ConnectionsTypes.SERIAL);
		Assert.assertEquals(con.getType(),ConnectionsTypes.SERIAL);
	}
	/**
	 * Teste un router
	 * @param router
	 */
	private void buildRouter(Router router) {		
		Assert.assertEquals(router.getID(), 0);
		Assert.assertEquals(router.getHostname(),"R"+(router.getID()+1));
		Assert.assertEquals(router.getPassword(),"");
		Assert.assertEquals(router.getSecret(),"");
		Assert.assertEquals(router.getSubnet(),network.getSubnet());			
	}
}
