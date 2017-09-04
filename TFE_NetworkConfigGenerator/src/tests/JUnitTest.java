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

import ListsSystem.ConnectionsTypes;
import controller.GUIController;
import controller.SubnetUtils;
import objects.Connection;
import objects.Network;
import objects.Router;
import objects.UserPC;
import objects.Vlan;

public class JUnitTest {

	private Network network ;
	@SuppressWarnings("static-access")
	@Test
	public void alltest() {
		SubnetUtils adrs = new SubnetUtils("192.168.0.128","255.255.255.240");
		Assert.assertEquals(adrs.getInfo().getCidrSignature(),"192.168.0.128/28");
		adrs.getIp(adrs.getInfo().getCidrSignature());
		this.network= new Network(adrs.getInfo().getCidrSignature());
		Assert.assertFalse(adrs.getInfo().isInRange("192.168.0.1"));
		Assert.assertTrue(adrs.getInfo().isInRange("192.168.0.129"));
		Assert.assertTrue(adrs.getInfo().isInRange("192.168.0.140"));
		Assert.assertFalse(adrs.getInfo().isInRange("192.168.0.160"));
		//Ajoute un router
		Router router = new Router(network.getAllHardwares().size(), "R1");
		this.network.addHardware(router, new Point(300,300));		
		buildRouter(router);
		//Ajoute un router		
		Router router2 = new Router(network.getAllHardwares().size(), "R1");
		this.network.addHardware(router2, new Point(400,300));		

		Assert.assertEquals(network.getAllHardwares().size(), 2);
		//Ajoute une connection R1-R2
		Vlan vlan = new Vlan(this.network.getSubnet(0), 0, "Global");
		Vlan vlan2 = new Vlan(new SubnetUtils("192.168.0.192/26"),1,"Vlan 1");
		try {
			this.network.addVlan(vlan);
		} catch (Exception e) {
			System.out.println("VLAN " + e.getMessage());
		}
		try {
			this.network.addVlan(vlan2);
		} catch (Exception e) {
			System.out.println("VLAN " + e.getMessage());
		}
		Connection con= new Connection(vlan2, ConnectionsTypes.ETHERNET, router.getID(), router2.getID(), this.network.getCoId(), this.network.getInterfaceName(router.getID(),ConnectionsTypes.ETHERNET), this.network.getInterfaceName(router2.getID(),ConnectionsTypes.ETHERNET), false);
		this.network.addConnection(vlan2.getNum(), router.getID(), router2.getID(), con);

		buildConnection(con);

		Assert.assertEquals(this.network.getAllHardwares().size(),2);
		Assert.assertEquals(this.network.getAllConnections().size(),1);
		//Ajoute un router
		Router router3 = new Router(network.getAllHardwares().size(), "R1");
		this.network.addHardware(router3, new Point(500,300));			
		//Ajoute une connection R3-R1
		Connection con2= new Connection(vlan2, ConnectionsTypes.SERIAL, router2.getID(), router3.getID(), this.network.getCoId(), this.network.getInterfaceName(router2.getID(),ConnectionsTypes.SERIAL), this.network.getInterfaceName(router3.getID(),ConnectionsTypes.SERIAL), false);
		this.network.addConnection(vlan2.getNum(), router.getID(), router2.getID(), con2);

		Assert.assertEquals(this.network.getAllHardwares().size(),3);
		Assert.assertEquals(this.network.getAllConnections().size(),2);
		//Supprimer le router 2 et toutes ces connections
		this.network.removeHard(router.getID());
		Assert.assertEquals(this.network.getAllHardwares().size(),2);
		Assert.assertEquals(this.network.getAllConnections().size(),1);
		//Ajoute un PC
		UserPC pc = new UserPC(0, "U1");
		this.network.addHardware(pc, new Point(200,200));		

		Assert.assertEquals(this.network.getAllHardwares().size(),3);
		Assert.assertEquals(this.network.getAllConnections().size(),1);
		//Vérifie le réseau
		//checkNetwork();
		//Sauvegarde
		//saving();
		//Reset
		//newNetwork();
		//Vérification du reset
		/*
		Assert.assertEquals(this.network.getAllHardwares().size(),0);
		Assert.assertEquals(this.network.getAllConnections().size(),0);
		Assert.assertEquals(this.network.getVlans().get(0).getSubnetwork().getInfo().getCidrSignature(),"192.168.0.1/24");
		//Ouverture du réseau enregistré
		//loading();
		//Vérification de l'ouverture
		Assert.assertEquals(this.network.getAllHardwares().size(),3);
		Assert.assertEquals(this.network.getAllConnections().size(),1);
		Assert.assertEquals(this.network.getVlans().get(0).getSubnetwork().getInfo().getCidrSignature(),"192.168.0.128/28");*/
		
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
		/*	Assert.assertFalse(con.setCompoIP1("192.168.0.1"));
		Assert.assertFalse(con.setCompoIP1("192.168.10.1"));
		Assert.assertFalse(con.setCompoIP1("192.168.1.252"));
		Assert.assertTrue(con.setCompoIP1("192.168.0.130"));*/
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
		//Assert.assertEquals(router.getSubnet(),network.getSubnet());			
	}
}
