package packSystem;

import java.awt.Dimension;

import javax.swing.JTextField;

public class HeadsTable extends JTextField{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HeadsTable(String title,int dim){
		super.setText(title);
		super.setEditable(false);
		super.setHorizontalAlignment(JTextField.CENTER);
		super.setPreferredSize(new Dimension(dim, 25));
	}
}
