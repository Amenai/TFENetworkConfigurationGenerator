package graphique;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.RepaintManager;

public class GUI implements ActionListener {
	protected JMenuItem exit = new JMenuItem("Exit");
	protected JCheckBoxMenuItem ipRouter = new JCheckBoxMenuItem("First ip");
	protected JMenu menuF = new JMenu("Fichier");
	protected JMenu menuO = new JMenu("Options");
    public GUI(String network) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Turn off double buffering
               RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);

                JFrame frame = new JFrame("Title");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new GUIController(network));
                
            	JMenuBar menuBar = new JMenuBar();
            	
            	menuBar.add(menuF);
            	menuBar.add(menuO);

            	menuF.setMnemonic(KeyEvent.VK_F);
            	menuO.setMnemonic(KeyEvent.VK_O);
            	
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