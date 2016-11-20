package graphique;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.RepaintManager;

import objects.Hardware;
import packSystem.HardwaresList;
import tests.SubnetUtils;

public class GUI implements ActionListener {
	protected JMenuItem exit = new JMenuItem("Exit");
	protected JCheckBoxMenuItem ipRouter = new JCheckBoxMenuItem("First ip");
	protected JMenu menuF = new JMenu("Fichier");
	protected JMenu menuO = new JMenu("Options");
	protected JButton menuADD = new JButton("ADD USER");
	protected JMenuItem saveNetwork = new JMenuItem("Save");
	protected JMenuItem loadNetwork = new JMenuItem("Load");
    public GUI(SubnetUtils network) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Turn off double buffering
               RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);

                JFrame frame = new JFrame("Title");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                GUIController controller = new GUIController(network);
                frame.add(controller);
                
            	JMenuBar menuBar = new JMenuBar();
            	
            	menuBar.add(menuF);
            	menuBar.add(menuO);
            	menuBar.add(menuADD);
            	menuF.setMnemonic(KeyEvent.VK_F);
            	menuO.setMnemonic(KeyEvent.VK_O);
            	menuADD.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("ADD USER");
						controller.network.addHardware(HardwaresList.USER_PC,new Point(225,50));
						Hardware h = controller.network.getHardwares().get(controller.network.getHardwares().size()-1);
						System.out.println("IP = " + h.getIP());
						controller.plusHard(h);
					}
				});
            	exit.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						//TODO
						Object source = e.getSource();
						if (source == exit){
							System.out.println("Exit Menu");
							System.exit(0);
						}
						
					}
				});
            	menuF.add(saveNetwork);
            	menuF.add(loadNetwork);
            	saveNetwork.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						controller.saveNetwork();						
					}
				});
            	loadNetwork.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						controller.loadNetwork();						
					}
				});
            	menuF.addSeparator();
            	menuF.add(exit);
            	
            	menuO.add(ipRouter);
            	ipRouter.setSelected(true);
            	frame.setJMenuBar(menuBar);                         
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO Not working
		Object source = e.getSource();
		if (source == exit){
			System.out.println("Exit Menu");
			System.exit(0);
		}
		
	}
}