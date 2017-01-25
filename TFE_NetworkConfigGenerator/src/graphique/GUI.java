package graphique;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import objects.ControlButton;
import objects.Hardware;
import packSystem.HardwaresListS;
import packSystem.MouseTypes;
import packSystem.SubnetUtils;

public class GUI implements ActionListener {
	protected JMenuItem exit = new JMenuItem("Exit");
	protected JCheckBoxMenuItem ipRouter = new JCheckBoxMenuItem("First ip");
	protected JCheckBoxMenuItem showHostname = new JCheckBoxMenuItem("Show Hostname");
	protected JCheckBoxMenuItem showInterfaces = new JCheckBoxMenuItem("Show IP");
	protected JMenu menuF = new JMenu("Fichier");
	protected JMenu menuO = new JMenu("Options");
	protected JMenuItem saveNetwork = new JMenuItem("Enregistrer");
	protected JMenuItem loadNetwork = new JMenuItem("Ouvrir");
	protected JMenuItem newNetwork = new JMenuItem("Nouveau");
	protected JPanel controlPanel = new JPanel();
	protected JButton addRouter = new JButton();
	protected JFrame frame = new JFrame();
	public GUI() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off double buffering
				RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				WindowListener exitListener = new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent e) {
						boolean confirm = packSystem.Messages.confirm("Etes-vous sur de vouloir quitter ? \nTout travail non sauvegardé sera perdu !");
						if (confirm) {
							System.exit(0);
						}
					}
				};
				frame.addWindowListener(exitListener);
				frame.setLayout(new BorderLayout());
				GUIController controller = new GUIController();
				frame.add(controller,BorderLayout.CENTER);

				frame.add(controlPanel,BorderLayout.SOUTH);
				JMenuBar menuBar = new JMenuBar();
				ControlButton routerButton = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/router.png"),MouseTypes.MOUSE_ROUTER);
				routerButton.setToolTipText("Router");
				ControlButton userButton = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/pc.png"),MouseTypes.MOUSE_USER);	
				userButton.setToolTipText("User PC");
				ControlButton defaultMouse = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/mouse.png"),MouseTypes.MOUSE_DEFAULT);	
				defaultMouse.setToolTipText("Grabbing Mouse");
				ControlButton deleteMouse = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/delete.png"),MouseTypes.MOUSE_DELETE);
				deleteMouse.setToolTipText("Suppression");

				ControlButton serialIntButton = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/serial.png"),MouseTypes.MOUSE_SERIAL);
				serialIntButton.setToolTipText("Interface Serial");
				ControlButton etherIntButton = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/ethernet.png"),MouseTypes.MOUSE_ETHERNET);
				etherIntButton.setToolTipText("Interface Ethernet");
				ControlButton gigaIntButton = new ControlButton(Toolkit.getDefaultToolkit().getImage("src/gigabits.png"),MouseTypes.MOUSE_GIGABITS);
				gigaIntButton.setToolTipText("Interface Gigabits");

				controlPanel.add(defaultMouse);
				controlPanel.add(deleteMouse);	
				controlPanel.add(routerButton);
				controlPanel.add(userButton);				
				controlPanel.add(serialIntButton);		
				controlPanel.add(etherIntButton);		
				controlPanel.add(gigaIntButton);		
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
				ActionListener controllerAction = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						controller.changeMouse(((ControlButton)arg0.getSource()).getType());					
					}
				};
				routerButton.addActionListener(controllerAction);
				userButton.addActionListener(controllerAction);
				defaultMouse.addActionListener(controllerAction);
				deleteMouse.addActionListener(controllerAction);
				serialIntButton.addActionListener(controllerAction);
				etherIntButton.addActionListener(controllerAction);
				gigaIntButton.addActionListener(controllerAction);
				menuF.add(newNetwork);
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
				newNetwork.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						boolean confirm = packSystem.Messages.confirm("Etes vous sur de vouloir reset l'interface ? ");
						if (confirm) {
							controller.newNetwork();
						}
												
					}
				});
				menuF.addSeparator();
				menuF.add(exit);

				menuO.add(ipRouter);
				menuO.add(showHostname);
				menuO.add(showInterfaces);
				showHostname.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						controller.showHostname(showHostname.isSelected());

					}
				});
				showInterfaces.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						controller.showInterfaces(showInterfaces.isSelected());

					}
				});
				ipRouter.setSelected(true);
				showHostname.setSelected(true);
				showInterfaces.setSelected(true);
				frame.setJMenuBar(menuBar);          
				frame.setResizable(false);
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
	public void changingTitle(String title){
		frame.setTitle(title);
	}
}