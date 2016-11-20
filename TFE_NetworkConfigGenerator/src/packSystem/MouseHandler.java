package packSystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import graphique.GUIController;
import objects.Hardware;

public class MouseHandler extends MouseAdapter {

	private Dimension dimension;
	private int xOffset;
	private int yOffset;
	private Hardware draggy;
	private GUIController gui;

	public MouseHandler (Dimension dimension, GUIController guiController){
		this.dimension = dimension;
		this.gui = guiController;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (draggy != null) {
			draggy.setText(""+draggy.getIP());
			draggy.setSize(draggy.getPreferredSize());
			draggy = null;
		}
	}
	@Override
	public void mousePressed(MouseEvent me) {
		JComponent comp = (JComponent) me.getComponent();
		Component l  =  comp.findComponentAt(me.getPoint());		
		if (l instanceof Hardware) {
			draggy = (Hardware) l ;
			xOffset = me.getX() - draggy.getX();
			yOffset = me.getY() - draggy.getY();
			if (SwingUtilities.isRightMouseButton(me)){           
				draggy.setText(""+draggy.getIP());
				draggy.setSize(draggy.getPreferredSize());
			}
			if ( SwingUtilities.isLeftMouseButton(me)){
				draggy.setText(""+draggy.getIP());
			}                             
			if ( SwingUtilities.isMiddleMouseButton(me)){
				String t = Messages.askStringValue("IP ? ");
				draggy.setIP(t);
				draggy.setText(draggy.getIP());
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		if (draggy != null) {
			if ( SwingUtilities.isLeftMouseButton(me)){
				if (me.getX() < dimension.getWidth() && me.getY() < dimension.getHeight()){
					if (me.getX() > 0 && me.getY() >0){
						draggy.setLocation(me.getX() - xOffset, me.getY() - yOffset);
					}
				}
				this.gui.repaint();
			}			
		}
	}
}
