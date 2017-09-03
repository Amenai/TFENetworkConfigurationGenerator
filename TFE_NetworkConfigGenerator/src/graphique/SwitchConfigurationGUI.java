package graphique;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import objects.Connection;
import objects.Network;
import objects.Switch;
import objects.Vlan;
import packSystem.HeadsTable;

public class SwitchConfigurationGUI implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton Save = new JButton("Save");
	private JButton Generate = new JButton("Generate"); 
	private JLabel hostname = new JLabel("Hostname");
	private JTextField hostnameEdit = new JTextField();
	private ArrayList<InterfacePanel> panels = new ArrayList<InterfacePanel>();
	private JFrame frame = new JFrame();
	private InterfacePanel intP;
	private HeadsTable headName = new HeadsTable("Name",100);
	private HeadsTable headVlanC = new HeadsTable("Vlans",125);
	private HeadsTable headInt = new HeadsTable("Int",100);

	public SwitchConfigurationGUI(Network n,Switch hardSwitch) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JFrame(hardSwitch.getHostname());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLayout(new GridBagLayout());      
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridheight = 1;	
				gbc.gridwidth = 1;
				JPanel host = new JPanel();

				host.add(hostname);
				host.add(hostnameEdit);
				frame.add(host, gbc);
				gbc.gridy++;

				hostname.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setPreferredSize(new Dimension(100, 25));
				hostnameEdit.setText(hardSwitch.getHostname());				
				gbc.gridx=0;
				gbc.gridy++;
				if(!(n.getConnections(hardSwitch.getConnection()).isEmpty())){
					gbc.gridy++;
					JPanel heads = new JPanel();
					heads.add(headName);
					heads.add(headVlanC);
					heads.add(headInt);
					frame.add(heads, gbc);
					gbc.gridy++;
					gbc.gridx=0;
					int i = 1;	
					for( Connection c : n.getConnections(hardSwitch.getConnection())){ 
						intP = new  InterfacePanel(c,c.getConnectionID(),hardSwitch,n);
						panels.add(intP);
						frame.add(intP,gbc);
						gbc.gridy++;
						i++;
					}
					gbc.gridy++;
				}
				else {
					JLabel l = new JLabel("No connection found");
					l.setPreferredSize(new Dimension(140, 25));
					frame.add(l,gbc);
					gbc.gridy++;
				}
				frame.add(new JSeparator(), gbc);


				JPanel panel = new JPanel(new GridLayout(1,3));				
				panel.add(Save);			
				Save.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						boolean noError = true;
						hardSwitch.setHostname(hostnameEdit.getText());
						for(InterfacePanel intP : panels){
							n.getConnections(hardSwitch.getConnection()).get(intP.getNum()).setCompoName(hardSwitch.getID(), intP.getIntName());
						}
						if(noError){
							packSystem.Messages.showMessage("Saved successfully", "Confirmation");
							frame.dispose();
							SwitchConfigurationGUI gui = new SwitchConfigurationGUI(n, hardSwitch);
						}
						
					}
				});
				Generate.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String path = packSystem.Messages.savingFile(JFileChooser.OPEN_DIALOG).getPath();
						String conf = n.printConfig(hardSwitch.getID());
						try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
							out.print(conf);
							packSystem.Messages.showMessage("Generating completed", "Confirmation");;
						} catch (FileNotFoundException ex) {
							ex.printStackTrace();
						}						
					}
				});
				panel.add(Generate);
				frame.add(panel, gbc); 
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);
			}

		});
	}

	class InterfacePanel extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int num ;
		private JTextField intName = new JTextField();
		private JLabel vlanName = new JLabel();	
		private JLabel ipVlan = new JLabel();
		public  InterfacePanel(Connection c, int num, Switch s, Network n) {
			this.num = num;
			JTextField t = new JTextField((this.num)+".");
			t.setPreferredSize(new Dimension(15, 25));
			vlanName.setText(n.getVlans().get(c.getVlanID()).getName());
			vlanName.setPreferredSize(new Dimension(100,25));
			intName.setPreferredSize(new Dimension(100,25));
			ipVlan.setPreferredSize(new Dimension(125,25));
			this.add(t);
			t.setEditable(false);
			this.add(vlanName);
			this.add(ipVlan);
			if (c.getFirstCompo() == s.getID()){
				intName.setText(c.getCompoName(c.getFirstCompo()));
			}
			else {
				intName.setText(c.getCompoName(c.getSecondCompo()));
			}		
			ipVlan.setText(n.getVlans().get(c.getVlanID()).getSubnetwork().getInfo().getCidrSignature());

			intName.setPreferredSize(new Dimension(100, 25));
			this.add(intName);	
		}
		public int getNum() {		
			return this.num;
		}
		public String getVlanName(){
			return this.vlanName.getText();
		}
		public String getIntName(){
			return this.intName.getText();
		}
	}
	class VlansPanels extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int num ;

		private JTextField name = new JTextField();
		private JTextField vlanNum = new JTextField();
		private JCheckBox deleted = new JCheckBox();
		public  VlansPanels(Vlan vlan, int num) {
			this.num = num;
			JTextField t = new JTextField((this.num)+".");	
			t.setPreferredSize(new Dimension(15, 25));
			this.add(t);
			t.setEditable(false);						
			name.setPreferredSize(new Dimension(100, 25));
			vlanNum.setPreferredSize(new Dimension(50, 25));

			name.setText(vlan.getName());
			vlanNum.setText(""+vlan.getNum());
			this.add(name);			
			this.add(vlanNum);
			this.add(deleted);
		}		
		public int getNum() {		
			return this.num;
		}
		public String getName(){
			return this.name.getText();
		}
		public boolean getDeleteBox(){
			return this.deleted.isSelected();
		}

	}
	@Override
	public void actionPerformed(ActionEvent arg0) {

	}	
}

