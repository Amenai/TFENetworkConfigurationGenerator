package graphique;

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
import javax.swing.JFileChooser;
import javax.swing.JLayeredPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;
import packSystem.MouseHandler;
import packSystem.SubnetUtils;

public class GUIController extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Network network ; 
	private boolean showHostname = true;
	private boolean showInterfaces = true;
	private int height = 1500;
	private int width = 800;
	private MouseHandler handler;
	private Dimension d = new Dimension (height,width);
	public GUIController() {
		SubnetUtils adrs = new SubnetUtils("192.168.0.0/28");;
		this.network= new Network(adrs);

		handler = new MouseHandler(getPreferredSize(),this);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);	
	}

	@Override
	public Dimension getPreferredSize() {
		return this.d;
	}

	public void plusHard(Hardware h){
		this.add(h);
		this.repaint();
	}
	public void plusInterface(int type, int compoID1, int compoID2){
		if((this.network.getHardware(compoID1).getType() != HardwaresListS.USER_PC) || (this.network.getHardware(compoID2).getType() != HardwaresListS.USER_PC)){
			if(!isPcAndLinked(compoID1) && !isPcAndLinked(compoID2) ){
				Connection co2 = new Connection(this.network,type, compoID1, compoID2,this.network.getConnectionCount());
				if (this.network.getHardware(compoID1).getType() == HardwaresListS.ROUTER){
					co2.setCompoIP1(this.network.getHardware(compoID1).getSubnet().getInfo().getFirstFreeIP());
					co2.setCompoIP2((this.network.getHardware(compoID1).getSubnet().getInfo().getFirstFreeIP()));
					if (this.network.getHardware(compoID2).getType() == HardwaresListS.USER_PC){
						((UserPC)this.network.getHardware(compoID2)).setIp(co2.getCompoIP2());
					}
				}
				else {
					co2.setCompoIP2(this.network.getHardware(compoID2).getSubnet().getInfo().getFirstFreeIP());
					co2.setCompoIP1((this.network.getHardware(compoID2).getSubnet().getInfo().getFirstFreeIP()));
					((UserPC)this.network.getHardware(compoID1)).setIp(co2.getCompoIP1());
				}

				this.network.addConnection(compoID1, compoID2, co2);
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
		if (network.getAllHardwares().size() != this.getComponentCount()){			
			System.out.println("Autre");
			this.removeAll();
			for(Hardware a : network.getAllHardwares()){
				System.out.println("" + a.getHostname());
				this.add(a);			
			}
		}
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(3));
		g2D.setColor(Color.BLACK);
		for (int i=0;i < this.getComponentCount();i++){
			Component comp = this.getComponent(i);
			//TODO	System.out.println("DD " + comp.getLocation().toString());
			if (comp instanceof Hardware){
				Hardware h = (Hardware) comp;						

				for(Connection e : network.getAllConnections()){

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
				for(Hardware a : network.getAllHardwares()){
					a.getIcon().paintIcon(this, g, a.getLocation().x, a.getLocation().y);
				}
				if(showHostname){
					g2D.setFont(new Font("TimesRoman", Font.PLAIN, 20));
					g2D.setColor(Color.BLACK);
					g2D.drawString(h.getHostname(), h.getLocation().x,h.getLocation().y);
				}		
				if(showInterfaces){
					for(Connection e : network.getAllConnections()){
						g2D.setColor(ConnectionsTypes.getColor(e.getType()));
						Point2D p1 = new Point2D.Double(this.network.getAllHardwares().get(e.getFirstCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getFirstCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D p2 = new Point2D.Double(this.network.getAllHardwares().get(e.getSecondCompo()).getX()+(h.getIcon().getIconWidth()/2), this.network.getAllHardwares().get(e.getSecondCompo()).getY()+(h.getIcon().getIconWidth()/2));
						Point2D co1 = calculateCoord(p1,p2);
						Point2D co2 = calculateCoord(p2,p1);
						drawCenteredCircle(g,(int)co1.getX(),(int)co1.getY());					
						drawCenteredCircle(g,(int)co2.getX(),(int)co2.getY());
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
	public void drawCenteredCircle(Graphics g, int d, int e) {
		d = d-(6/2);
		e = e-(6/2);
		g.drawOval(d,e,6,6);
	}
	public Point2D calculateCoord(Point2D s, Point2D e){
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
		Router router = new Router(this.network.getSubnet(),this.network.getHardwaresCount());
		router.setHostname("R"+(this.network.getHardwaresCount()+1));
		this.network.addHardware(router, new Point(x, y));
		router.setSize(new Dimension(router.getIcon().getIconWidth(), router.getIcon().getIconHeight()));
		super.repaint();
	}
	public void plusNewUser(int x, int y) {
		UserPC user = new UserPC(this.network.getSubnet(),this.network.getHardwaresCount());
		user.setHostname("U"+(this.network.getHardwaresCount()+1));
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
			// TODO: handle exception
			e.printStackTrace();
		}
		for (Hardware hardwares : this.network.getAllHardwares()){
			hardwares.setSize(new Dimension(hardwares.getIcon().getIconWidth(), hardwares.getIcon().getIconHeight()));  	
		}
		//this.network= n;
		repaint();
	}

	public void newNetwork() {
		System.out.println("NEW");

		try {
			//{"IP":"192.168.0.0\/24","connections":[],"hardwares":[]}
			JSONObject networkJSON = new JSONObject();
			networkJSON.put("IP", "192.168.0.0/24");
			JSONArray conJSON = new JSONArray();
			networkJSON.put("connections", conJSON);
			networkJSON.put("hardwares", conJSON);

			String ip = (String) networkJSON.get("IP");
			System.out.println("IP = " + ip);
			this.network.load(networkJSON);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		repaint();
	}
	public void showConfig(Hardware draggy) {
		new ConfigurationGUI(network, draggy);
		this.repaint();
	}
	public void showPCConfig(Hardware draggy) {
		new ConfigurationGUI(network, draggy);
		this.repaint();
	}
}
