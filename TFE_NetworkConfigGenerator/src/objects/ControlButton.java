package objects;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ControlButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int imageWidth = 60, imageHeight = 60;
	ImageIcon icon ;
	String Name = "Default";	
	private int type;

	public ControlButton(String urlImage,int type) {
		URL url = ControlButton.class.getResource(urlImage);
		this.icon = new ImageIcon((new ImageIcon(url)).getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT));
		this.setIcon(this.icon);
		this.type=type;
	}

	public ImageIcon getIcon(){
		return this.icon;
	}
	public void printConfig() {
		System.out.println("Config = ");
	}

	public int getType() {
		return this.type;
	}

}
