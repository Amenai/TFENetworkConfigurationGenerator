package tests;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.RepaintManager;

public class DragImage {
	
  public static void main(String[] args) {
    String imageFile = "router.png";
    String imagePC = "pc.png";
    // Turn off double buffering
    //RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);

    Image image = Toolkit.getDefaultToolkit().getImage("D:/Amenai/Documents/Projets/TFE_NetworkConfigGenerator/src/router.png");
   Image image2 = Toolkit.getDefaultToolkit().getImage("D:/Amenai/Documents/Projets/TFE_NetworkConfigGenerator/src/pc.png");
    //image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
    //image2 = image2.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
    JFrame frame = new JFrame("DragImage");
    JMenu menu = new JMenu("Fichier");
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(menu);
	 frame.setJMenuBar(menuBar);
    frame.add(new DraggedImage(image));
    frame.add(new DraggedImage(image2));
    frame.setSize(300, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}