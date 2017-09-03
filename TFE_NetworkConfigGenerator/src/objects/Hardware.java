package objects;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.json.simple.JSONObject;

import ListsSystem.HardwaresListS;
import controller.SubnetUtils;

public abstract class Hardware extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int imageWidth = 60, imageHeight = 60;

	private ArrayList<Integer> connection = new ArrayList<Integer>(); 
	private ImageIcon icon ;
	private String Hostname = "Defaut";
	private int ID;
	private int hardwareType = HardwaresListS.DEFAUT;
	public Hardware(String imagePath,int code,int type,String hostname) {
		URL url = ControlButton.class.getResource(imagePath);
		this.icon = new ImageIcon((new ImageIcon(url)).getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		this.setID(code);
		this.setHostname(hostname);
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
	public JSONObject getJSONObject() {
		JSONObject obj = new JSONObject();      	
		return obj;
	}
	public Hardware fromJSONObject(JSONObject obj) {
		return this;
	}
}
