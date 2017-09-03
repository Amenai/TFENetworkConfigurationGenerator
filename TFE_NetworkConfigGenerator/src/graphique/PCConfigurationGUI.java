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
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

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

import ListsSystem.ConnectionsTypes;
import ListsSystem.HardwaresListS;
import controller.SubnetUtils;
import graphique.RouterConfigurationGUI.Item;
import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.UserPC;
import objects.Vlan;
import packSystem.Messages;

public class PCConfigurationGUI implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton save = new JButton("Save");
	private JButton delete = new JButton("Remove Connection");
	private JLabel hostname = new JLabel("Hostname");
	private JTextField hostnameEdit = new JTextField();
	private JLabel gateway = new JLabel("Gateway");
	private JLabel gatewayIP = new JLabel("");
	private JLabel ip = new JLabel("Ip");
	private JComboBox ipEdit = new JComboBox();
	private JFrame frame = new JFrame();
	private JLabel vlan = new JLabel();
	public PCConfigurationGUI(Network n,UserPC hardPC) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame(hardPC.getHostname());
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
				hostnameEdit.setPreferredSize(new Dimension(125, 25));
				hostnameEdit.setText(hardPC.getHostname());				
				frame.add(host, gbc);
				gbc.gridy++;
				if(hardPC.isLinked()){
					JPanel ipP = new JPanel();
					ipP.add(ip);
					ip.setPreferredSize(new Dimension(100, 25));
					ipEdit = getComboIp(n.getVlans().get(hardPC.getVlan()).getSubnetwork(), hardPC.getIP());
					ipEdit.setSelectedItem((String) hardPC.getIP());
					ipEdit.setPreferredSize(new Dimension(125, 25));
					ipP.add(ipEdit);
					frame.add(ipP, gbc);
					gbc.gridy++;
					JPanel gatewayP = new JPanel();
					gatewayP.add(gateway);
					gatewayP.add(gatewayIP);
					gatewayIP.setText((hardPC).getGateway());
					gateway.setPreferredSize(new Dimension(100, 25));
					gatewayIP.setPreferredSize(new Dimension(125, 25));
					frame.add(gatewayP, gbc);
					gbc.gridy++;
					JPanel vlanP = new JPanel();
					JLabel label = new JLabel("Vlan");
					vlanP.add(label);
					label.setPreferredSize(new Dimension(100, 25));
					vlan.setText(n.getVlans().get(hardPC.getVlan()).getName());		
					vlanP.add(vlan);
					vlan.setPreferredSize(new Dimension(125, 25));
					frame.add(vlanP,gbc);
					gbc.gridy++;
				}			
				else {
					JLabel l = new JLabel("No connection found");
					l.setPreferredSize(new Dimension(140, 25));
					frame.add(l,gbc);
					gbc.gridy++;
				}
				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(save);
				save.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean noError = true;
						hardPC.setHostname(hostnameEdit.getText());
						for( Connection c : n.getConnections(hardPC.getConnection())){
							if(!(c.setCompoIP((String)ipEdit.getSelectedItem(),hardPC.getID()))){
								packSystem.Messages.showErrorMessage("Problème IP");
								noError = false;
							}
							else {
								if (c.getFirstCompo() == hardPC.getID()){
									(hardPC).setIp(c.getCompoIP1(),n.getVlans().get(hardPC.getVlan()).getNum());	
								}
								else {
									(hardPC).setIp(c.getCompoIP2(),n.getVlans().get(hardPC.getVlan()).getNum());
								}
							}
						}
						if(noError){
							packSystem.Messages.showMessage("Saved successfully", "Confirmation");
							frame.dispose();
							PCConfigurationGUI gui = new PCConfigurationGUI(n, hardPC);
						}
					}
				});
				if(hardPC.isLinked()){
					panel.add(delete);
					delete.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if(packSystem.Messages.confirm("Removing the connection for " + hardPC.getHostname() + " ?")){
								boolean noError = true;
								for( Connection c : n.getConnections(hardPC.getConnection())){
									n.removeConnection(c);
								}
								if(noError){
									packSystem.Messages.showMessage("Connection deleted", "Confirmation");
									frame.dispose();
									PCConfigurationGUI gui = new PCConfigurationGUI(n, hardPC);
								}
							}
						}
					});
				}
				frame.add(panel, gbc); 
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

		});
	}
	private JComboBox<?> getComboIp(SubnetUtils subnetwork, String ip) {
		ArrayList<String> freeIp = subnetwork.getInfo().getAllFreeAddress();
		freeIp.add(ip);
		Collections.sort(freeIp);
		String [] ips = freeIp.toArray(new String[0]);
		JComboBox<?> cb = new JComboBox<Object>(ips);
		return cb;
	}		
	@Override
	public void actionPerformed(ActionEvent arg0) {
	}
}

