package objects;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import packSystem.HardwaresListS;
import packSystem.SubnetUtils;

public abstract class Hardware extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int imageWidth = 60, imageHeight = 60;

	private ArrayList<Integer> connection = new ArrayList<Integer>(); 
	private ImageIcon icon ; //  TODO Default Image
	private SubnetUtils subNet ;
	private String Hostname = "Defaut";
	private int ID;
	private int hardwareType = HardwaresListS.DEFAUT;
	public Hardware(SubnetUtils network,String ip,Image image,int code,int type,String hostname) {
		this.setSubNet(network);
		this.setID(code);
		this.setHostname(hostname);
		this.icon = new ImageIcon(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		this.setIcon(this.icon);	
		this.setType(type);
	}

	public void addConnection(int co ){
		//TODO CHECK IF c in NETWORK
		this.connection.add(co);
	}

	public void setType(int type){
		this.hardwareType = type;
	}
	public int getType(){
		return this.hardwareType;
	}
	public ArrayList<Integer> getConnection(){
		return this.connection;
	}

	public ImageIcon getIcon(){
		return this.icon;
	}
	public int getID() {
		return this.ID;
	}
	public void setID(int iD) {
		this.ID = iD;
	}
	public SubnetUtils getSubnet(){
		return this.subNet;
	}
	public void setSubNet(SubnetUtils subnet){
		this.subNet = subnet;
	}
	public String getHostname() {
		return Hostname;
	}

	public void setHostname(String hostname) {
		Hostname = hostname;
	}	
}
