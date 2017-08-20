package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import graphique.GUI;
import graphique.PCConfigurationGUI;
import graphique.RouterConfigurationGUI;
import graphique.SwitchConfigurationGUI;
import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.Switch;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;

public class GUIController extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Network network = new Network(new SubnetUtils("192.168.0.0/28")); 
	private boolean showHostname = true;
	private boolean showInterfaces = true;
	private int height = 1500;
	private int width = 800;
	private MouseHandler handler;
	private Dimension d = new Dimension (height,width);
	public GUIController() {
		SubnetUtils adrs = new SubnetUtils("192.168.0.0/28");
		network= new Network(new SubnetUtils("192.168.0.0/28"));

		this.handler = new MouseHandler(getPreferredSize(),this);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);	
	}

	@Override
	public Dimension getPreferredSize() {
		return this.d;
	}

	protected void plusHard(Hardware h){
		this.add(h);
		this.repaint();
	}
	public void plusInterface(int type, int compoID1, int compoID2){
		if((this.network.getHardware(compoID1).getType() != HardwaresListS.USER_PC) || (this.network.getHardware(compoID2).getType() != HardwaresListS.USER_PC)){
			if(!isPcAndLinked(compoID1) && !isPcAndLinked(compoID2) ){
				if(this.network.getSubnet().getInfo().getFirstFreeIP() != "0.0.0.0"){
					Connection co2 = null;
					switch(this.network.getHardware(compoID1).getType()) {
					case  HardwaresListS.ROUTER :
						co2 = new Connection(this.network,type, compoID1, compoID2,this.network.getCoId());
						co2.setCompoIP(this.network.getSubnet().getInfo().getFirstFreeIP(),compoID1);
						co2.setCompoIP(this.network.getSubnet().getInfo().getFirstFreeIP(),compoID2);
						if (this.network.getHardware(compoID2).getType() == HardwaresListS.USER_PC){
							((UserPC)this.network.getHardware(compoID2)).setIp(co2.getCompoIP2());
						}
						break;
					case HardwaresListS.USER_PC :
						co2 = new Connection(this.network,type, compoID2, compoID1,this.network.getCoId());
						co2.setCompoIP(this.network.getSubnet().getInfo().getFirstFreeIP(),compoID2);
						co2.setCompoIP(this.network.getSubnet().getInfo().getFirstFreeIP(),compoID1);
						((UserPC)this.network.getHardware(compoID1)).setIp(co2.getCompoIP2());
						break;
					case HardwaresListS.SWITCH :
						// TODO
						//Switch na pas d'IP !!
						break;
					}
					this.network.addConnection(compoID1, compoID2, co2);
				}
			}
		}
	}
	private boolean isPcAndLinked(int compoID){
		if (this.network.getHardware(compoID).getType() == HardwaresListS.USER_PC ){			
			return ((UserPC)this.network.getHardware(compoID)).isLinked();
		}
		return false;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (network.getAllHardwares().size() != this.getComponentCount()){
			this.removeAll();
			for(int key : this.network.getAllHardwares().keySet()){
				System.out.println("" + this.network.getAllHardwares().get(key).getHostname());
				this.add(this.network.getAllHardwares().get(key));			
			}
		}
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(3));
		g2D.setColor(Color.BLACK);
		for (int i=0;i < this.getComponentCount();i++){
			Component comp = this.getComponent(i);
			if (comp instanceof Hardware){
				Hardware h = (Hardware) comp;						

				for(int key: this.network.getAllConnections().keySet()){
					Connection e = this.network.getAllConnections().get(key);
					g2D.setColor(ConnectionsTypes.getColor(e.getType()));
					g2D.setStroke(new BasicStroke(3));

					Point2D p1 = new Point2D.Double(this.network.getAllHardwares().get(e.getFirstCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getFirstCompo()).getY()+(h.getIcon().getIconWidth()/2));
					Point2D p2 = new Point2D.Double(this.network.getAllHardwares().get(e.getSecondCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getSecondCompo()).getY()+(h.getIcon().getIconWidth()/2));
					g2D.draw(new Line2D.Double(p1, p2));
				}
				int pointStart = handler.getpointStart();
				if ( pointStart != -1) {
					g.setColor(ConnectionsTypes.getColor(handler.getMouseType()));

					int pointStartX = this.network.getHardware(pointStart).getX() + this.network.getHardware(pointStart).getIcon().getIconWidth()/2;
					int pointStartY = this.network.getHardware(pointStart).getY() + this.network.getHardware(pointStart).getIcon().getIconWidth()/2;
					Point pointEnd = this.getMousePosition();
					g.drawLine(pointStartX,pointStartY, pointEnd.x, pointEnd.y);	 
				}
				for(int key: this.network.getAllHardwares().keySet()){
					Hardware a = this.network.getAllHardwares().get(key);
					a.getIcon().paintIcon(this, g, a.getLocation().x, a.getLocation().y);
				}
				if(showHostname){
					g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
					g2D.setColor(Color.BLACK);
					g2D.drawString(h.getHostname(), h.getLocation().x,h.getLocation().y);
				}		
				if(showInterfaces){
					for(int key : this.network.getAllConnections().keySet()){
						Connection e = this.network.getAllConnections().get(key);
						g2D.setColor(ConnectionsTypes.getColor(e.getType()));
						Point2D p1 = new Point2D.Double(this.network.getAllHardwares().get(e.getFirstCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getFirstCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D p2 = new Point2D.Double(this.network.getAllHardwares().get(e.getSecondCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getSecondCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D co1 = calculateCoord(p1,p2);
						Point2D co2 = calculateCoord(p2,p1);
						GUI.drawCenteredCircle(g,(int)co1.getX(),(int)co1.getY());					
						GUI.drawCenteredCircle(g,(int)co2.getX(),(int)co2.getY());
						g2D.setColor(Color.BLACK);
						g2D.setFont(new Font("TimesRoman", Font.PLAIN, 15));
						g2D.drawString(e.getCompoIP1(), (int)co1.getX(), (int)co1.getY());
						g2D.drawString(e.getCompoIP2(), (int)co2.getX(), (int)co2.getY());
					}
				}

			}
			//Paint le reste
			//super.paint(g); 
		}
	}
	private Point2D calculateCoord(Point2D s, Point2D e){
		Point p = null;
		int coordx = 0;
		int coordy = 0;
		double a = Math.atan((s.getY()-e.getY())/(e.getX()-s.getX())); // Angle
		int r = 25; // Rayon
		if (s.getX() <= e.getX()){
			if (s.getY() <= e.getY()){
				//DROITE - BAS Q4		
				coordx = (int) (s.getX() + r*Math.cos(a));
				coordy = (int) (s.getY() - r*Math.sin(a));
			}
			else {
				//DROITE - HAUT Q1

				coordx = (int) (s.getX() + r*Math.cos(a));
				coordy = (int) (s.getY() - r*Math.sin(a));
			}
		}
		else {
			if (s.getY() <= e.getY()){
				//GAUCHE - BAS Q3
				coordx = (int) (s.getX() - r*Math.cos(a));
				coordy = (int) (s.getY() + r*Math.sin(a));
			}
			else {
				//GAUCHE - HAUT Q2
				coordx = (int) (s.getX() - r*Math.cos(a));
				coordy = (int) (s.getY() + r*Math.sin(a));
			}
		}
		p = new Point(coordx,coordy);
		return p;
	}	

	public void plusNewRouter(int x, int y) {
		int count = this.network.getFreeHardwareCount();
		Router router = new Router(this.network.getSubnet().getInfo().getLowAddress(),count);
		router.setHostname("R"+(count+1));
		this.network.addHardware(router, new Point(x, y));
		router.setSize(new Dimension(router.getIcon().getIconWidth(), router.getIcon().getIconHeight()));
		super.repaint();
	}
	public void plusNewSwitch(int x, int y) {
		int count = this.network.getFreeHardwareCount();
		Switch switc = new Switch(this.network.getSubnet().getInfo().getLowAddress(),count);
		switc.setHostname("S"+(count+1));
		this.network.addHardware(switc, new Point(x, y));
		switc.setSize(new Dimension(switc.getIcon().getIconWidth(), switc.getIcon().getIconHeight()));
		super.repaint();

	}
	public void plusNewUser(int x, int y) {
		int count = this.network.getFreeHardwareCount();
		UserPC user = new UserPC(this.network.getSubnet().getInfo().getLowAddress(),count);
		user.setHostname("U"+(count+1));
		this.network.addHardware(user, new Point(x, y));
		user.setSize(new Dimension(user.getIcon().getIconWidth(), user.getIcon().getIconHeight()));
		super.repaint();
	}
	public void changeMouse(int type){	
		this.setCursor(handler.changingMouse(type));

	}
	public void showInterfaces(boolean selected) {
		this.showInterfaces = selected;
		repaint();
	}

	public void showHostname(boolean selected) {
		this.showHostname = selected;
		repaint();
	}

	public void deleteHard(Hardware hardware) {
		this.network.removeHard(hardware.getID());
	}
	public void saveNetwork (){
		System.out.println("SAVING");
		File path = packSystem.Messages.savingFile(JFileChooser.SAVE_DIALOG);
		String lines = this.network.save();
		try (FileWriter file = new FileWriter(path.getPath())) {
			file.write(lines);	
			packSystem.Messages.showMessage("Sauvegarde Réussie", "Confirmation");
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
	}

	public void loadNetwork(){
		System.out.println("LOADING");
		File path = packSystem.Messages.selectFile(JFileChooser.OPEN_DIALOG);
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path));
			JSONObject networkJSON = (JSONObject) obj;
			String ip = (String) networkJSON.get("IP");
			System.out.println("IP = " + ip);
			this.network.load(networkJSON);
		}catch (Exception e) {
			e.printStackTrace();
		}
		for (int key : this.network.getAllHardwares().keySet()){
			Hardware hardwares = this.network.getAllHardwares().get(key);
			hardwares.setSize(new Dimension(hardwares.getIcon().getIconWidth(), hardwares.getIcon().getIconHeight()));  	
		}
		//this.network= n;
		repaint();
	}

	public void newNetwork() {
		System.out.println("NEW");
		String subnet = packSystem.Messages.askStringValue("Nouveau subnet");
		try {
			//{"IP":"192.168.0.0\/24","connections":[],"hardwares":[]}
			JSONObject networkJSON = new JSONObject();
			networkJSON.put("IP", subnet);
			JSONArray conJSON = new JSONArray();
			networkJSON.put("connections", conJSON);
			networkJSON.put("hardwares", conJSON);

			String ip = (String) networkJSON.get("IP");
			System.out.println("IP = " + ip);
			this.network.load(networkJSON);
		}catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}
	public void showRouterConfig(Router draggy) {
		new RouterConfigurationGUI(network, draggy);
		this.repaint();
	}
	public void showPCConfig(UserPC draggy) {
		new PCConfigurationGUI(network, draggy);
		this.repaint();
	}
	public void showSwitchConfig(Switch draggy) {
		new SwitchConfigurationGUI(network, draggy);
		this.repaint();
	}

}
