package tests;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Drawing extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Point> points;
	private int sizeX = 20;
	private int sizeY = 20;
	public Drawing() {
		points = new ArrayList<Point>();
		setBackground(Color.WHITE);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {                
				if (SwingUtilities.isLeftMouseButton(e)){
					points.add(new Point(e.getX()-(sizeX/2), e.getY()-(sizeY/2)));
					repaint();
				}
				if ( SwingUtilities.isRightMouseButton(e)){
					points = new ArrayList<Point>();
					repaint();
				}                  
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {			
			public void mouseDragged(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)){
					points.add(new Point(e.getX()-(sizeX/2), e.getY()-(sizeY/2)));
					repaint();
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLUE);
		for (Point point : points) {
			g2.fillOval(point.x, point.y, sizeX, sizeY);
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.add(new Drawing());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(400, 400);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
			}
		});
	}

}