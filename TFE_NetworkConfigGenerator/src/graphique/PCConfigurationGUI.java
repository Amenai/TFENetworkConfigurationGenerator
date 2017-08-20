package graphique;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.SubnetUtils;
import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;
import packSystem.Messages;

public class PCConfigurationGUI implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton Save = new JButton("Save");
	private JLabel hostname = new JLabel("Hostname");
	private JTextField hostnameEdit = new JTextField();
	private JLabel gateway = new JLabel("Gateway");
	private JLabel gatewayIP = new JLabel("");
	private JLabel ip = new JLabel("Ip");
	private JTextField ipEdit = new JTextField();
	private JFrame frame = new JFrame();

	public PCConfigurationGUI(Network n,UserPC draggy) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off double buffering
				RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
				frame = new JFrame(draggy.getHostname());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new GridBagLayout());      
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				frame.add(hostname, gbc);
				JPanel host = new JPanel();
				host.add(hostname);
				host.add(hostnameEdit);
				hostname.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setText(draggy.getHostname());				
				frame.add(host, gbc);
				gbc.gridy++;
				JPanel ipP = new JPanel();
				ipP.add(ip);
				ipP.add(ipEdit);
				ip.setPreferredSize(new Dimension(100, 25));
				ipEdit.setPreferredSize(new Dimension(100, 25));
				ipEdit.setText((draggy).getIP());
				frame.add(ipP, gbc);
				gbc.gridy++;

				JPanel gatewayP = new JPanel();
				gatewayP.add(gateway);
				gatewayP.add(gatewayIP);
				gatewayIP.setText((draggy).getGateway());
				gateway.setPreferredSize(new Dimension(100, 25));
				gatewayIP.setPreferredSize(new Dimension(100, 25));
				frame.add(gatewayP, gbc);
				gbc.gridy++;


				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(Save);			
				Save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int index = 0;
						boolean noError = true;
						draggy.setHostname(hostnameEdit.getText());
						for( Connection c : n.getConnections(draggy.getConnection())){
							if(!(c.setCompoIP(ipEdit.getText(),draggy.getID()))){
								packSystem.Messages.showErrorMessage("Problème IP");
								noError = false;
							}
							else {
								if (c.getFirstCompo() == draggy.getID()){
									(draggy).setIp(c.getCompoIP1());	
								}
								else {
									(draggy).setIp(c.getCompoIP2());
								}
							}		
						}

						if(noError){
							packSystem.Messages.showMessage("Sauvegarde réussie", "Confirmation");
						}
						frame.dispose();
						PCConfigurationGUI gui = new PCConfigurationGUI(n, draggy);
					}
				});

				frame.add(panel, gbc); 
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

		});
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
}

