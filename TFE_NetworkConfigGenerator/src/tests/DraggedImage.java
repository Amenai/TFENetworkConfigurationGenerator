package tests;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class DraggedImage extends JComponent implements MouseMotionListener {
		  static int imageWidth = 60, imageHeight = 60;
		  int imageX, imageY;

		  Image image;

		  public DraggedImage(Image i) {
			System.out.println("Constructor");			
		    image = i;
		    image = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_DEFAULT);
		    addMouseMotionListener(this);
		  }

		  public void mouseDragged(MouseEvent e) {
			System.out.println("Drag");
		    imageX = e.getX();
		    imageY = e.getY();
		    repaint();
		  }

		  public void mouseMoved(MouseEvent e) {
		  }

		  public void paint(Graphics g) {
		    System.out.println("Paint");
		    Graphics2D g2 = (Graphics2D) g;
		    
		    g2.drawImage(image, imageX, imageY, this);
		    
		  }
}
