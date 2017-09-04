package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.SynchronousQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ListsSystem.ConnectionsTypes;
import ListsSystem.HardwaresListS;
import graphique.GUI;
import graphique.NetworkOptionsPanel;
import graphique.NetworkOptionsPanel;
import graphique.PCConfigurationGUI;
import graphique.RouterConfigurationGUI;
import graphique.SwitchConfigurationGUI;
import objects.Connection;
import objects.ControlButton;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.Switch;
import objects.SwitchRouterConnection;
import objects.UserPC;
import objects.Vlan;

public class GUIController extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Network network;
	private boolean showHostname = true;
	private boolean firstIP = true;
	private boolean showInterfaces = true;
	private boolean showNetworkPanel = false;
	private int height = 800;
	private int width = 1500;
	private MouseHandler handler;
	private Dimension d = new Dimension (width,height);
	private NetworkOptionsPanel optionsPanel ;
	public GUIController() {
		network= new Network();
		this.handler = new MouseHandler(getPreferredSize(),this);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);
		this.optionsPanel = new NetworkOptionsPanel(network);
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
				int selected= this.optionsPanel.getSelectedVlan();
				Vlan using = this.network.getVlans().get((Integer)selected);
				Connection co2 = null;
				switch(this.network.getHardware(compoID1).getType()){
				case  HardwaresListS.ROUTER : 
					switch(this.network.getHardware(compoID2).getType()){
					case  HardwaresListS.ROUTER : co2 = linkRouterToRouter(using,type,compoID1,compoID2);break;
					case  HardwaresListS.SWITCH : co2 = linkRouterToSwitch(using,type,compoID1,compoID2);break;
					case  HardwaresListS.USER_PC : co2 = linkRouterToPC(using,type,compoID1,compoID2);break;
					}
					break;
				case  HardwaresListS.SWITCH : 
					switch(this.network.getHardware(compoID2).getType()){
					case  HardwaresListS.ROUTER : co2 = linkRouterToSwitch(using,type,compoID2,compoID1);break;
					case  HardwaresListS.SWITCH : co2 = linkSwitchToSwitch(using,type,compoID1,compoID2);break;
					case  HardwaresListS.USER_PC : co2 = linkSwitchToPC(using,type,compoID1,compoID2);break;
					}
					break;
				case  HardwaresListS.USER_PC : 
					switch(this.network.getHardware(compoID2).getType()){
					case  HardwaresListS.ROUTER : co2 = linkRouterToPC(using,type,compoID2,compoID1);break;
					case  HardwaresListS.SWITCH : co2 = linkSwitchToPC(using,type,compoID2,compoID1);break;
					}
					break;
				}
				this.network.addConnection(using.getNum(),compoID1, compoID2, co2);
			}
		}
	}
	private Connection linkRouterToPC(Vlan using, int type, int routerID, int PCID) {
		Connection co2 = new Connection(using,type, routerID, PCID,this.network.getCoId(),this.network.getInterfaceName(routerID,type),this.network.getInterfaceName(PCID,type),false);
		co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),routerID,false);
		co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),PCID,false);
		((UserPC)this.network.getHardware(PCID)).setIp(co2.getCompoIP2(),using.getNum());
		return co2;
	}
	private Connection linkRouterToRouter(Vlan using, int type, int routerID1, int routerID2){
		Connection co2 = new Connection(using,type, routerID1, routerID2,this.network.getCoId(),this.network.getInterfaceName(routerID1,type),this.network.getInterfaceName(routerID2,type),false);
		co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),routerID1,false);
		co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),routerID2,false);
		return co2;
	}

	private Connection linkSwitchToSwitch(Vlan using, int type, int switchID1, int switchID2){
		Connection co2= new Connection(using, type, switchID1, switchID2, this.network.getCoId(), this.network.getInterfaceName(switchID1,type), this.network.getInterfaceName(switchID2,type), false);
		return co2;
	}
	private SwitchRouterConnection linkRouterToSwitch(Vlan using, int type, int routerID, int switchID){
		SwitchRouterConnection co2 = new SwitchRouterConnection(using,type, routerID, switchID,this.network.getCoId(),this.network.getInterfaceName(routerID,type),this.network.getInterfaceName(switchID,type),false);
		Connection setIP = network.getConnectionFromVlan(co2.getVlanID(), switchID, HardwaresListS.USER_PC);

		if(setIP != null){
			//CAS 3
			co2.setCompoIP(setIP.getCompoIP1(),routerID,true);
			co2.setCompoIP(setIP.getCompoIP2(),switchID,true);
		}
		else {
			//CAS 1 
			//Pas encore PC connecté à ce VLAN
			co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),routerID,false);
			co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),switchID,false);	
		}
		for(Connection coco : this.network.getConnectionsof(switchID)){
			System.out.println("PC DEJAS CONNECTE");
			co2.addSubInterface(coco.getConnectionID());
		}
		//Switch s = ((Switch)this.network.getAllHardwares().get(switchID));
		Switch s = ((Switch)(this.network.getHardware((Integer)switchID)));
		for(Hardware h : this.network.getAllHardwares().values()){
			System.out.println("" + h.getHostname() + "/" + h.getID());
		}
		s.addSRCOtoSwitch((SwitchRouterConnection)co2);	
		//	Switch s = ((Switch)(this.network.getHardware((Integer)switchID)));
		System.out.println("ID = " + s.getName());
		System.out.println("" + s.getAllSRCo().toString());
		return co2;
	}
	private Connection linkSwitchToPC(Vlan using, int type, int switchID, int PCID){
		//CHECK IF ROUTER CONNECTED IN SAME VLAN
		Connection co2 = new Connection(using,type, switchID, PCID,this.network.getCoId(),this.network.getInterfaceName(switchID,type),this.network.getInterfaceName(PCID,type),false);
		Switch s = ((Switch)(this.network.getHardware(switchID)));
		System.out.println("USING " + using.getNum());
		int id = ((Switch)(this.network.getHardware((Integer)switchID))).getSRConnection(using.getNum());
		System.out.println("ID = " + id);
		if(id != -1){
			//CAS 4
			SwitchRouterConnection setIP = (SwitchRouterConnection) this.network.getConnection(id);
			co2.setCompoIP(setIP.getCompoIP1(),switchID,true);
			co2.setCompoIP(setIP.getCompoIP2(),PCID,true);
			((UserPC)this.network.getHardware(PCID)).setIp(setIP.getCompoIP2(),using.getNum());			
		}
		else {
			//CAS 2
			//Pas encore router connecté à ce VLAN
			co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),switchID,false);
			co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),PCID,false);
			((UserPC)this.network.getHardware(PCID)).setIp(co2.getCompoIP2(),using.getNum());
		}
		for(int srCoZ : ((Switch)this.network.getHardware(switchID)).getAllSRCo().values()){
			System.out.println("ROUTERS DEJAS CONNECTE");
			((SwitchRouterConnection)this.network.getConnection(srCoZ)).addSubInterface(co2.getConnectionID());
		}
		return co2;
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
					//TODO SUBINTERFACE SWITCH PAINT
					if(!e.isSubInterface()){
						g2D.setColor(ConnectionsTypes.getColor(e.getType()));
						g2D.setStroke(new BasicStroke(3));

						Point2D p1 = new Point2D.Double(this.network.getAllHardwares().get(e.getFirstCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getFirstCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D p2 = new Point2D.Double(this.network.getAllHardwares().get(e.getSecondCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getSecondCompo()).getY()+(h.getIcon().getIconWidth()/2));
						g2D.draw(new Line2D.Double(p1, p2));
					}
					else {
						Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
						g2D.setStroke(dashed);
						g2D.setColor(ConnectionsTypes.getColor(e.getType()));
						Point2D p1 = new Point2D.Double(this.network.getAllHardwares().get(e.getFirstCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getFirstCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D p2 = new Point2D.Double(this.network.getAllHardwares().get(e.getSecondCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getSecondCompo()).getY()+(h.getIcon().getIconWidth()/2));

						g2D.draw(new Line2D.Double(p1, p2));
						Stroke base = new BasicStroke(3);
						g2D.setStroke(base);
					}
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
						if(!(network.getHardware(e.getFirstCompo()).getType() == HardwaresListS.SWITCH)){
							g2D.drawString(e.getCompoIP1(), (int)co1.getX(), (int)co1.getY());
						}
						if(!(network.getHardware(e.getSecondCompo()).getType() == HardwaresListS.SWITCH)){
							g2D.drawString(e.getCompoIP2(), (int)co2.getX(), (int)co2.getY());
						}
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
		Router router = new Router(count,"R"+count+1);
		router.setHostname("R"+(count+1));
		this.network.addHardware(router, new Point(x, y));
		router.setSize(new Dimension(router.getIcon().getIconWidth(), router.getIcon().getIconHeight()));
		super.repaint();
	}
	public void plusNewSwitch(int x, int y) {
		int count = this.network.getFreeHardwareCount();
		Switch switc = new Switch(count,"S"+count+1);
		switc.setHostname("S"+(count+1));
		this.network.addHardware(switc, new Point(x, y));
		switc.setSize(new Dimension(switc.getIcon().getIconWidth(), switc.getIcon().getIconHeight()));
		super.repaint();

	}
	public void plusNewUser(int x, int y) {
		int count = this.network.getFreeHardwareCount();
		UserPC user = new UserPC(count,"U"+count+1);
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
	public void showNetworkPanel(boolean selected){
		this.optionsPanel.setVisible(selected);
		this.showNetworkPanel = selected;
	}
	public void setNetworkPanel(NetworkOptionsPanel p){
		this.optionsPanel = p;		
	}
	public void showHostname(boolean selected) {
		this.showHostname = selected;
		repaint();
	}
	public void setFirstIp(boolean selected) {
		this.firstIP = selected;
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
			packSystem.Messages.showMessage("Saved completed", "Confirmation");
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
	}

	public void loadNetwork(){
		File path = packSystem.Messages.selectFile(JFileChooser.OPEN_DIALOG);
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path));
			System.out.println("PATH = " + path);
			JSONObject networkJSON = (JSONObject) obj;
			JSONArray vlanJSON = (JSONArray) networkJSON.get("vlans");

			this.network.load(networkJSON);
		}catch (Exception e) {
			e.printStackTrace();
		}
		for (int key : this.network.getAllHardwares().keySet()){
			Hardware hardwares = this.network.getAllHardwares().get(key);
			hardwares.setSize(new Dimension(hardwares.getIcon().getIconWidth(), hardwares.getIcon().getIconHeight()));  	
		}
		this.optionsPanel.dispose();
		this.optionsPanel = new NetworkOptionsPanel(this.network);
		if(showNetworkPanel){
			this.optionsPanel.setVisible(true);
		}
		this.repaint();
	}

	@SuppressWarnings("unchecked")
	public void newNetwork() {

		String subnet = packSystem.Messages.askStringValue("New global subnet");
		try {		
			this.network = new Network(subnet);
			this.optionsPanel.dispose();
			this.optionsPanel = new NetworkOptionsPanel(this.network);
			if(showNetworkPanel){
				this.optionsPanel.setVisible(true);
			}
			this.repaint();
		}catch (Exception e) {
			packSystem.Messages.showErrorMessage("Subnetwork not correct" + "\n" + "ex : 192.168.0.0/24");
		}
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

	class askComboIP extends JDialog{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JComboBox<String> combo1 = new JComboBox<String>();
		private JComboBox<String> combo2 = new JComboBox<String>();
		private JButton save = new JButton("Ok");
		private String IPFirst ;
		private String IPSecond ;

		public askComboIP(SubnetUtils s, String IPFirst, String IPSecond){			
			this.IPFirst = IPFirst;
			this.IPSecond = IPSecond;
			this.combo1 = getComboIp(s,this.IPFirst);
			this.combo2 = getComboIp(s,this.IPSecond);

			JFrame f = new JFrame("New Connection");
			JPanel p = new JPanel();

			p.add(combo1);
			p.add(combo2);
			p.add(save);
			save.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("" + (String)combo1.getSelectedItem());
					System.out.println("" + (String)combo2.getSelectedItem());
					/*IPFirst = (String)combo1.getSelectedItem();
					IPSecond = (String)combo2.getSelectedItem();
					 */
				}
			});
			f.add(p);
			f.setResizable(false);
			f.pack();
			f.setLocationRelativeTo(null);
			f.setVisible(true);
			f.setFocusable(true);
			f.setAlwaysOnTop(true);

		}		
		private JComboBox<String> getComboIp(SubnetUtils subnetwork,String ip) {
			ArrayList<String> freeIp = subnetwork.getInfo().getAllFreeAddress();
			Collections.sort(freeIp);			
			String [] ips = freeIp.toArray(new String[0]);
			JComboBox<String> cb = new JComboBox<String>(ips);
			cb.setSelectedItem(ip);
			return cb;
		}
	}

	public Network getNetwork() {		
		return this.network;
	}
}
