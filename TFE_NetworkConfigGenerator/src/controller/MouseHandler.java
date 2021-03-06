package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import ListsSystem.HardwaresListS;
import ListsSystem.MouseTypes;
import objects.ControlButton;
import objects.Hardware;
import objects.Router;
import objects.Switch;
import objects.UserPC;
import packSystem.Messages;

public class MouseHandler extends MouseAdapter {

	private Dimension dimension;
	private int xOffset;
	private int yOffset;
	private Hardware draggy;
	private GUIController gui;
	private int mouseType = MouseTypes.MOUSE_DEFAULT;
	private int pointStart = -1;
	public MouseHandler (Dimension dimension, GUIController guiController){
		this.dimension = dimension;
		this.gui = guiController;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (draggy != null) {
			//draggy.setText(""+draggy.getHostname());
			draggy.setSize(draggy.getPreferredSize());
			draggy = null;
		}
		if (pointStart != -1) {
			JComponent comp = (JComponent) me.getComponent();
			Component l  =  comp.findComponentAt(me.getPoint());	
			if (l instanceof Hardware){
				draggy = (Hardware) l ;
				if (pointStart != draggy.getID()){
					gui.plusInterface(mouseType, pointStart, draggy.getID());
				}
			}
			pointStart = -1;
		}
		gui.repaint();
	}
	@Override
	public void mousePressed(MouseEvent me) {
		JComponent comp = (JComponent) me.getComponent();
		Component l  =  comp.findComponentAt(me.getPoint());	
		if (l instanceof Hardware){
			draggy = (Hardware) l ;
			xOffset = me.getX() - draggy.getX();
			yOffset = me.getY() - draggy.getY();
			if (SwingUtilities.isRightMouseButton(me)){   
				switch(draggy.getType()){
				case HardwaresListS.ROUTER: 	gui.showRouterConfig((Router)draggy);break;
				case HardwaresListS.USER_PC:	gui.showPCConfig((UserPC)draggy);break;
				case HardwaresListS.SWITCH:		gui.showSwitchConfig((Switch)draggy);break;
				}

				gui.repaint();
			}
			if ( SwingUtilities.isLeftMouseButton(me)){
				switch(this.mouseType){
				case MouseTypes.MOUSE_DELETE:	boolean confirm = packSystem.Messages.confirm("Etes vous sur de vouloir supprimer "+draggy.getHostname()+" ? ");
				if (confirm) {
					gui.deleteHard(draggy);
				}				 break;
				case MouseTypes.MOUSE_SERIAL: 	pointStart= draggy.getID();break;
				case MouseTypes.MOUSE_ETHERNET: pointStart= draggy.getID();break;
				case MouseTypes.MOUSE_GIGABITS: pointStart= draggy.getID();break;
				}
			}                     
		}
		if ( SwingUtilities.isLeftMouseButton(me)){
			switch(this.mouseType){
			case MouseTypes.MOUSE_DEFAULT :	break;
			case MouseTypes.MOUSE_ROUTER: 	gui.plusNewRouter(me.getX(),me.getY());break;
			case MouseTypes.MOUSE_USER: 	gui.plusNewUser(me.getX(),me.getY());break;					
			case MouseTypes.MOUSE_SWITCH: 	gui.plusNewSwitch(me.getX(),me.getY());break;			
			}
		}                             			
		if ( SwingUtilities.isMiddleMouseButton(me)){

		}
		gui.repaint();
	}
	@Override
	public void mouseDragged(MouseEvent me) {
		if (draggy != null && mouseType == MouseTypes.MOUSE_DEFAULT) {
			if ( SwingUtilities.isLeftMouseButton(me)){
				if (me.getX() < dimension.getWidth() && me.getY() < dimension.getHeight()){
					if (me.getX() > 0 && me.getY() >0){
						draggy.setLocation(me.getX() - xOffset, me.getY() - yOffset);
					}
				}
				this.gui.repaint();
			}
		}
		gui.repaint();
	}
	public Cursor changingMouse(int type){
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		switch(type){
		case MouseTypes.MOUSE_DEFAULT :	mouseType =MouseTypes.MOUSE_DEFAULT;  break;

		case MouseTypes.MOUSE_ROUTER: cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/router.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_ROUTER; break;
		case MouseTypes.MOUSE_SWITCH: cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/switch.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_SWITCH; break;
		case MouseTypes.MOUSE_USER: cursor =Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/pc.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_USER; break;
		case MouseTypes.MOUSE_DELETE: cursor =Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/delete.png")).getImage(),
				new Point(0,0),"custom cursor"); mouseType =MouseTypes.MOUSE_DELETE;break;
		case MouseTypes.MOUSE_SERIAL: cursor =Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/serial.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_SERIAL; break;
		case MouseTypes.MOUSE_ETHERNET: cursor =Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/ethernet.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_ETHERNET; break;
		case MouseTypes.MOUSE_GIGABITS: cursor =Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon(MouseHandler.class.getResource("/resources/gigabits.png")).getImage(),
				new Point(0,0),"custom cursor");mouseType =MouseTypes.MOUSE_GIGABITS; break;
		}
		return cursor;
	}

	public int getMouseType() {		
		return this.mouseType;	
	}	
	public int getpointStart(){
		return this.pointStart;
	}
}
