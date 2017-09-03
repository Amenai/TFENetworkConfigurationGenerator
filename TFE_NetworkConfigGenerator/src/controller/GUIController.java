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
				if(using.getSubnetwork().getInfo().getFirstFreeIP() != "0.0.0.0"){
					/* TODO IPFIRST 
					 String IPFirst = using.getSubnetwork().getInfo().getFirstFreeIP();
					String IPSecond = using.getSubnetwork().getInfo().getFirstFreeIP();
					if(!firstIP){						
						packSystem.Messages.showErrorMessage("WTF");
						new askComboIP(using.getSubnetwork(),IPFirst,IPSecond);
					}
					else {
						packSystem.Messages.showErrorMessage("ERROR");
					}*/
					Connection co2 = null;
					switch(this.network.getHardware(compoID1).getType()) {
					case  HardwaresListS.ROUTER :
						co2 = new Connection(using,type, compoID1, compoID2,this.network.getCoId(),this.network.getInterfaceName(compoID1,type),this.network.getInterfaceName(compoID2,type),false);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID1);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID2);
						if (this.network.getHardware(compoID2).getType() == HardwaresListS.USER_PC){
							((UserPC)this.network.getHardware(compoID2)).setIp(co2.getCompoIP2(),using.getNum());
						}
						if (this.network.getHardware(compoID2).getType() == HardwaresListS.SWITCH){
							co2.setSubInterface(true);
						}
						break;
					case HardwaresListS.SWITCH :
						// TODO Switch IP
						co2 = new Connection(using,type, compoID1, compoID2,this.network.getCoId(),this.network.getInterfaceName(compoID1,type),this.network.getInterfaceName(compoID2,type),false);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID1);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID2);
						if (this.network.getHardware(compoID2).getType() == HardwaresListS.USER_PC){
							((UserPC)this.network.getHardware(compoID2)).setIp(co2.getCompoIP2(),using.getNum());
						}
						if (this.network.getHardware(compoID2).getType() == HardwaresListS.ROUTER){
							co2.setSubInterface(true);
						}
						break;
					case HardwaresListS.USER_PC :
						co2 = new Connection(using,type, compoID2, compoID1,this.network.getCoId(),this.network.getInterfaceName(compoID2,type),this.network.getInterfaceName(compoID1,type),false);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID2);
						co2.setCompoIP(using.getSubnetwork().getInfo().getFirstFreeIP(),compoID1);
						((UserPC)this.network.getHardware(compoID1)).setIp(co2.getCompoIP2(),using.getNum());
						break;
					}
					this.network.addConnection(using.getNum(),compoID1, compoID2, co2);
				}
			}
		}
	}
	private String[] askComboIP(SubnetUtils subnetUtils) {
		JFrame f = new JFrame("New IPS");
		JPanel p = new JPanel();
		p.setLayout(new GridLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridheight =1;
		gbc.gridwidth =1;
		gbc.gridx = 0;
		gbc.gridheight = 0;

		return null;
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
			packSystem.Messages.showMessage("Sauvegarde Réussie", "Confirmation");
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
