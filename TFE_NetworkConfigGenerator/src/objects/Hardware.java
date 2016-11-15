package objects;
/*
import java.awt.Image;

public class Hardware{
	 int imageWidth = 60, imageHeight = 60;

	Image image ; //  TODO Default Image
	String IP = "0.0.0.0";
	
	public Hardware(String network,Image image) {		
		this.IP = network;
		image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
		this.image= image;
		
	}
	
	public String getIP(){
		return this.IP;
	}

	public Image getImage(){
		return this.image;
	}
	public void printConfig() {
		System.out.println("Config = ");
	}
}
//------------------------------------------------------*/

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import packSystem.Paintable;

public class Hardware extends JLabel implements Paintable{
	int imageWidth = 60, imageHeight = 60;
	Point pointStart = null;
	Point pointEnd   = null;	 
	HashMap<Point,Point> Lines = new HashMap<Point,Point>();
	ArrayList<Connection> c = new ArrayList<Connection>(); 
	ImageIcon icon ; //  TODO Default Image
	String IP = "0.0.0.0";
	private int ID;

	public Hardware(String network,Image image,int code) {
		this.IP = network;
		this.setID(code);
    	this.icon = new ImageIcon(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
    	//this.setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
    	this.setIcon(this.icon);
    	this.Lines.put(new Point(50,10), new Point(50,450));
	}
	
	public void newConnection(){
		
	}
	public ArrayList<Connection> getCon(){
		return this.c;
	}
	public String getIP(){
		return this.IP;
	}	
	public ImageIcon getIcon(){
		return this.icon;
	}
	public void printConfig() {
		System.out.println("Config = ");
	}

	public HashMap<Point,Point> getConnections() {		
		return Lines;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	@Override
	public void paint(JComponent parent, Graphics2D g2d) {
		g2d.drawImage(icon.getImage(), 20, 20, 60, 60, parent);
		g2d.dispose();		
	}

	@Override
	public void moveTo(Point p) {
		this.setLocation(p);		
	}
}
