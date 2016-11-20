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

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Hardware extends JLabel {
	int imageWidth = 60, imageHeight = 60;

	ArrayList<Connection> c = new ArrayList<Connection>(); 
	ImageIcon icon ; //  TODO Default Image
	String IP = "0.0.0.0";
	private int ID;
	
	public Hardware(String network,Image image,int code) {
		this.IP = network;
		this.setID(code);
    	this.icon = new ImageIcon(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
    	this.setIcon(this.icon);
	}
	
	public void newConnection(){
		//TODO
	}
	public ArrayList<Connection> getConnection(){
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
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}

	public void setIP(String ip) {
		this.IP = ip;		
	}
}
