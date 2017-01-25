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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.RepaintManager;

import objects.Connection;
import objects.Hardware;
import objects.Network;
import objects.Router;
import objects.UserPC;
import packSystem.ConnectionsTypes;
import packSystem.HardwaresListS;
import packSystem.Messages;
import packSystem.SubnetUtils;

public class ConfigurationGUI implements ActionListener {
	private JButton Save = new JButton("Save");
	private JButton Generate = new JButton("Generate"); 
	private JLabel hostname = new JLabel("Hostname");
	private JLabel secret = new JLabel("Secret");
	private JTextField secretEdit = new JTextField();
	private JLabel password = new JLabel("Password");
	private JTextField passwordEdit = new JTextField();
	private JTextField hostnameEdit = new JTextField();
	private JLabel gateway = new JLabel("Gateway");
	private JLabel gatewayIP = new JLabel("");
	private JLabel ip = new JLabel("Ip");
	private JTextField ipEdit = new JTextField();
	private ArrayList<InterfacePanel> panels = new ArrayList<InterfacePanel>();
	private JFrame frame = new JFrame();
	private boolean isRouter = false;
	public ConfigurationGUI(Network n,Hardware draggy) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Turn off double buffering
				draggy.getType();
				if (draggy.getType() == HardwaresListS.ROUTER){isRouter=true;}
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
				if(isRouter){
					JPanel secretP = new JPanel();
					secretP.add(secret);
					secretP.add(secretEdit);
					secret.setPreferredSize(new Dimension(100, 25));
					secretEdit.setPreferredSize(new Dimension(100, 25));
					String sec = ((Router)draggy).getSecret();
					if(!sec.isEmpty()){
						sec = "******";
					}
					secretEdit.setText(sec);				
					frame.add(secretP, gbc);
					gbc.gridy++;
					JPanel passwordP = new JPanel();
					passwordP.add(password);
					passwordP.add(passwordEdit);
					password.setPreferredSize(new Dimension(100, 25));
					passwordEdit.setPreferredSize(new Dimension(100, 25));
					passwordEdit.setText(((Router)draggy).getPassword());				
					frame.add(passwordP, gbc);
					gbc.gridy++;
					int i = 1;
					for( Connection c : n.getConnections(draggy.getConnection())){    
						InterfacePanel p = new  InterfacePanel(c,i,draggy.getID());
						panels.add(p);
						frame.add(p,gbc);
						gbc.gridy++;
						i++;
					}
					gbc.gridy++;
					frame.add(new JSeparator(), gbc);
				}
				else {
					JPanel ipP = new JPanel();
					ipP.add(ip);
					ipP.add(ipEdit);
					ip.setPreferredSize(new Dimension(100, 25));
					ipEdit.setPreferredSize(new Dimension(100, 25));
					ipEdit.setText(((UserPC)draggy).getIP());
					frame.add(ipP, gbc);
					gbc.gridy++;

					JPanel gatewayP = new JPanel();
					gatewayP.add(gateway);
					gatewayP.add(gatewayIP);
					gatewayIP.setText(((UserPC)draggy).getGateway());
					gateway.setPreferredSize(new Dimension(100, 25));
					gatewayIP.setPreferredSize(new Dimension(100, 25));
					frame.add(gatewayP, gbc);
					gbc.gridy++;
				}

				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(Save);			
				Save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int index = 0;
						boolean noError = true;
						draggy.setHostname(hostnameEdit.getText());
						if(isRouter){					
							if (!(secretEdit.getText().equals("******"))){
								((Router)draggy).setSecret(secretEdit.getText());
							}						
							((Router)draggy).setPassword(passwordEdit.getText());
							for( Connection c : n.getConnections(draggy.getConnection())){
								InterfacePanel p =  panels.get(index);
								if (c.getFirstCompo() == draggy.getID()){
									if(!(c.setCompoIP1(p.getIp()))){
										packSystem.Messages.showErrorMessage("Problème IP");
										noError = false;
									}
									else {
										if(n.getHardware(c.getSecondCompo()).getType() == HardwaresListS.USER_PC){
											((UserPC)n.getHardware(c.getSecondCompo())).setGateway(p.getIp());
										}
									}
								}
								else {
									if(!(c.setCompoIP2(p.getIp()))){
										packSystem.Messages.showErrorMessage("Problème IP");
										noError = false;
									}
									else {
										if(n.getHardware(c.getFirstCompo()).getType() == HardwaresListS.USER_PC){
											((UserPC)n.getHardware(c.getFirstCompo())).setGateway(p.getIp());
										}
									}
								}
								c.setType(p.getType());	
								c.setCompoName(draggy.getID(),n.getInterfaceName(draggy.getID(), p.getType()));
								index++;
							}
						}
						else {
							for( Connection c : n.getConnections(draggy.getConnection())){
								if (c.getFirstCompo() == draggy.getID()){
									if(!(c.setCompoIP1(ipEdit.getText()))){
										packSystem.Messages.showErrorMessage("Problème IP");
										noError = false;
									}
									else {
										((UserPC)draggy).setIp(c.getCompoIP1());
										((UserPC)draggy).setGateway(c.getCompoIP2());
									}
								}
								else {
									if(!(c.setCompoIP2(ipEdit.getText()))){
										packSystem.Messages.showErrorMessage("Problème IP");
										noError = false;
									}
									else {
										((UserPC)draggy).setIp(c.getCompoIP2());
										((UserPC)draggy).setGateway(c.getCompoIP1());;
									}
								}								

							}

						}

						if(noError){
							packSystem.Messages.showMessage("Sauvegarde réussie", "Confirmation");
							frame.dispose();
						}
					}
				});
				Generate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String path = Messages.savingFile(JFileChooser.OPEN_DIALOG).getPath();
						String conf = n.printConfig(draggy.getID());
						System.out.println("GENERATING");
						try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
							out.print(conf);
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						}
					}
				});
				if(isRouter){panel.add(Generate);}
				frame.add(panel, gbc); 
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("unused")
		Object source = e.getSource();
	}
	class InterfacePanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JComboBox<?> ip ;
		private int num ;
		private JComboBox<?> combo ;
		private JTextField mask = new JTextField();
		private JTextField name = new JTextField();
		private JButton delete = new JButton("Delete");
		private JButton addSub = new JButton("Add SubInterface");
		public InterfacePanel(Connection c, int num, int ID) {
			this.num = num;
			JTextField t = new JTextField((this.num)+".");			
			this.add(t);			
			t.setEditable(false);			
			if (c.getFirstCompo() == ID){
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP1());
				name.setText(c.getCompoName(c.getFirstCompo()));
				ip.setSelectedItem(c.getCompoIP1());
			}
			else {
				ip = getComboIp(c.getSubnetwork(),c.getCompoIP2());
				name.setText(c.getCompoName(c.getSecondCompo()));
				ip.setSelectedItem(c.getCompoIP2());
			}		
			name.setPreferredSize(new Dimension(100, 25));
			ip.setPreferredSize(new Dimension(100, 25));
			mask.setPreferredSize(new Dimension(100, 25));
			this.add(name);
			this.add(ip);
			this.add(mask);
			mask.setText(c.getSubnetwork().getInfo().getNetmask());
			combo = createComboColorPanel(c.getType());

			this.add(combo);

			this.add(delete);
			delete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean confirm = packSystem.Messages.confirm("Etes vous sur de vouloir supprimer l'interface ? ");
					if (confirm) {
						
					}					
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
		private JComboBox<?> createComboColorPanel(int type){

			JComboBox<?> cb = new JComboBox<Object>(ConnectionsTypes.list);		
			switch(type){ 
			case ConnectionsTypes.GIGABIT: cb.setSelectedIndex(0); break;
			case ConnectionsTypes.SERIAL: cb.setSelectedIndex(1); break;
			case ConnectionsTypes.ETHERNET: cb.setSelectedIndex(2); break;
			}
			return cb;
		}	
		public int getNum() {		
			return this.num;
		}

		protected String getIp(){
			return (String)this.ip.getSelectedItem();
		}
		protected int getType(){
			return this.combo.getSelectedIndex();
		}
		public String getName(){
			return this.name.getText();
		}

	}
}

