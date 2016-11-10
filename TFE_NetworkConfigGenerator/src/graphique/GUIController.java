package graphique;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import objects.Hardware;
import objects.Network;
import packSystem.HardwaresList;
import packSystem.MouseHandler;

public class GUIController extends JLayeredPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Network network ; 
    public GUIController(String network) {
        this.network = new Network(network);
        this.network.addHardware(HardwaresList.ROUTER);
        this.network.addHardware(HardwaresList.USER_PC);
        this.network.addHardware(HardwaresList.USER_PC);                  
        int i=0;
        for (Hardware hardwares : this.network.getHardwares()){
        	JLabel label = new JLabel("");
        	ImageIcon icon = new ImageIcon(hardwares.getImage());
        	label.setSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        	label.setIcon(icon);
        	label.setLocation(10,i*60);
        	add(label);
        	i++;
        }

        MouseHandler handler = new MouseHandler(getPreferredSize());
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}