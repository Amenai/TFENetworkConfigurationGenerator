package packSystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

	public MouseHandler (Dimension dimension){
		this.dimension = dimension;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (draggy != null) {
			draggy.setText("");
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

				//draggy.setText("Left");  
			}                             
		}
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		if (draggy != null ) {
			if ( SwingUtilities.isLeftMouseButton(me)){
				if (me.getX() < dimension.getWidth() && me.getY() < dimension.getHeight()){
					if (me.getX() > 0 && me.getY() >0){
						draggy.setLocation(me.getX() - xOffset, me.getY() - yOffset);
					}
				}
			}
		}
	}
}
