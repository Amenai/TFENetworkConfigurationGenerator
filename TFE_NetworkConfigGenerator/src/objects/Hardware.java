package objects;

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
