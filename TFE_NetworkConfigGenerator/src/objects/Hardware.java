package objects;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import controller.SubnetUtils;
import packSystem.HardwaresListS;

public abstract class Hardware extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int imageWidth = 60, imageHeight = 60;

	private ArrayList<Integer> connection = new ArrayList<Integer>(); 
	private ImageIcon icon ; //  TODO Default Image
	private String Hostname = "Defaut";
	private int ID;
	private int hardwareType = HardwaresListS.DEFAUT;
	public Hardware(String ip,Image image,int code,int type,String hostname) {
		this.setID(code);
		this.setHostname(hostname);
		this.icon = new ImageIcon(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		this.setIcon(this.icon);	
		this.setType(type);
	}

	protected void addConnection(int co ){
		this.connection.add(Integer.valueOf(co));
	}
	protected void deleteConnection(int co){
		this.connection.remove(Integer.valueOf(co));
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
	public String getHostname() {
		return Hostname;
	}

	public void setHostname(String hostname) {
		Hostname = hostname;
	}	
}
