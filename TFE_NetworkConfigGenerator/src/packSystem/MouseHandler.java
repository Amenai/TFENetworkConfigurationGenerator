package packSystem;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MouseHandler extends MouseAdapter {

	private Dimension dimension;
    private int xOffset;
    private int yOffset;
    private JLabel draggy;

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

    public void mousePressed(MouseEvent me) {
        JComponent comp = (JComponent) me.getComponent();
        Component child = comp.findComponentAt(me.getPoint());
        if (child instanceof JLabel) {
            xOffset = me.getX() - child.getX();
            yOffset = me.getY() - child.getY();
            draggy = (JLabel) child;             
            if (SwingUtilities.isRightMouseButton(me)){
            	draggy.setText("Right");
            }
           if ( SwingUtilities.isLeftMouseButton(me)){
            	draggy.setText("Left");  
            }                             
            draggy.setSize(draggy.getPreferredSize());
        }
    }

    public void mouseDragged(MouseEvent me) {
        if (draggy != null ) {
        	if (me.getX() < dimension.getWidth() && me.getY() < dimension.getHeight()){
        		if (me.getX() > 0 && me.getY() >0){
        			draggy.setLocation(me.getX() - xOffset, me.getY() - yOffset);
        		}
        	}
        }
    }
}
