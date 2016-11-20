package graphique;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;

import javax.swing.JLayeredPane;

import objects.Connection;
import objects.Hardware;
import objects.Network;
import packSystem.HardwaresList;
import packSystem.MouseHandler;
import tests.Example.Relationship;
import tests.SubnetUtils;

public class GUIController extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Network network ; 
	public GUIController(SubnetUtils network) {
		this.network = new Network(network);
		this.network.addHardware(HardwaresList.ROUTER,new Point (225,50));
		this.network.addHardware(HardwaresList.USER_PC,new Point (150,150));
		this.network.addHardware(HardwaresList.USER_PC,new Point (300,150));                  		
		for (Hardware hardwares : this.network.getHardwares()){
			hardwares.setSize(new Dimension(hardwares.getIcon().getIconWidth(), hardwares.getIcon().getIconHeight()));
			//add(hardwares);        	
		}

		MouseHandler handler = new MouseHandler(getPreferredSize(),this);
		this.addMouseListener(handler);
		this.addMouseMotionListener(handler);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}

	public void plusHard(Hardware h){
		this.add(h);
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		if (network.getHardwares().size() != this.getComponentCount()){
			System.out.println("Autre");
			//this.removeAll();
			for(Hardware a : network.getHardwares()){
				this.add(a);
			}
		}
		Graphics2D g2D = (Graphics2D) g;
		System.out.println("COUNT = " + this.getComponentCount());
		for (int i=0;i < this.getComponentCount();i++){
			Component comp = this.getComponent(i);
			if (comp instanceof Hardware){
				Hardware h = (Hardware) comp;
				//for(Connection e : h.getCon()){


				g2D.setStroke(new BasicStroke(3));
				Point2D p1 = new Point2D.Double(this.network.getHardwares().get(0).getX()+(h.getIcon().getIconWidth()/2), this.network.getHardwares().get(0).getY()+(h.getIcon().getIconWidth()/2));
				Point2D p2 = new Point2D.Double(this.network.getHardwares().get(1).getX()+(h.getIcon().getIconWidth()/2), this.network.getHardwares().get(1).getY()+(h.getIcon().getIconWidth()/2));

				g2D.draw(new Line2D.Double(p1, p2));

				p1 = new Point2D.Double(this.network.getHardwares().get(0).getX()+(h.getIcon().getIconWidth()/2), this.network.getHardwares().get(0).getY()+(h.getIcon().getIconWidth()/2));
				p2 = new Point2D.Double(this.network.getHardwares().get(2).getX()+(h.getIcon().getIconWidth()/2), this.network.getHardwares().get(2).getY()+(h.getIcon().getIconWidth()/2));

				g2D.draw(new Line2D.Double(p1, p2));
				//}
			}
			//Paint le reste
			super.paint(g); 
		}
	}
	
	public void saveNetwork (){
		File path = packSystem.Messages.selectFile(0);
	}
	
	public void loadNetwork(){
		File path = packSystem.Messages.selectFile(0);
		
	}
}
