package tests;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Line {
	private Point Start;
	private Point End;
	private Point Link1;
	private Point Link2;
	public Line(Point start,Point end,Point link1,Point link2){
		this.setStart(start);
		this.setEnd(end);	
		this.setLink1(link1);
		this.setLink2(link2);
	}

	public Point getStart() {
		return Start;
	}

	public void setStart(Point start) {
		Start = start;
	}

	public Point getEnd() {
		return End;
	}

	public void setEnd(Point end) {
		End = end;
	}		
	public Point getLink1() {
		return Link1;
	}

	public void setLink1(Point link1) {
		Link1 = link1;
	}

	public Point getLink2() {
		return Link2;
	}

	public void setLink2(Point link2) {
		Link2 = link2;
	}
	public String toString(){
		return "Lines = " + Start.x +"/"+ Start.y + "-" + End.x +"/"+ End.y;
	}
}

public class Drawed {
	public static void main(String args[]) throws Exception {
		ArrayList<Line> Lines = new ArrayList<>();
		JFrame f = new JFrame("Drawing TEST");
		f.setSize(400, 400);
		f.setLocationRelativeTo(null);
		f.setResizable(false);
		JPanel p = new JPanel() {
			Point pointStart = null;
			Point pointEnd   = null;	    
			Point link1 = null;
			Point link2 = null;
			{
				addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						if (isInStartingPoint(e.getPoint())){
							pointStart = new Point(35,35);				
							link1 = calculateCoord(pointStart, e.getPoint());
						}
					}					
					private boolean isInStartingPoint(Point point) {
						if (point.getX() <= 60 && point.getX() >= 10) {
							if (point.getY() <= 60 && point.getY() >=10){
								return true;
							}
						}						
						return false;
					}

					public void mouseReleased(MouseEvent e) {
						if (pointStart != null) {
							if (isInEndingPoint(e.getPoint())){					
								pointEnd = new Point(325, 325);
								//---------------------------------------------------------------------------------CALCUL LINK2
								link2=calculateCoord(pointStart, pointEnd);
								Lines.add(new Line(pointStart,pointEnd,link1,link2));
								pointStart = null;
							}
							else pointStart = null;
						}
						repaint();
					}

					private boolean isInEndingPoint(Point point) {
						if (point.getX() <= 350 && point.getX() >= 300) {
							if (point.getY() <= 350 && point.getY() >=300){
								return true;
							}
						}						
						return false;
					}
				});
				addMouseMotionListener(new MouseMotionAdapter() {
					public void mouseMoved(MouseEvent e) {
						if (pointStart != null) {
							pointEnd = e.getPoint();
						}
					}

					public void mouseDragged(MouseEvent e) {
						if (pointStart != null) {
							pointEnd = e.getPoint();
							repaint();
						}
					}
				});
			}
			public void drawCenteredCircle(Graphics g, int d, int e) {
				d = d-(6/2);
				e = e-(6/2);
				g.drawOval(d,e,6,6);
			}
			public Point calculateCoord(Point s, Point e){
				Point p = null;
				int coordx = 0;
				int coordy = 0;
				double a = Math.atan((s.getY()-e.getY())/(e.getX()-s.getX())); // Angle
				int r = 25; // Rayon
				if (s.x <= e.x){
					if (s.y <= e.y){
						//DROITE - BAS Q4		
						coordx = (int) (s.x + r*Math.cos(a));
						coordy = (int) (s.y - r*Math.sin(a));
					}
					else {
						//DROITE - HAUT Q1
						
						coordx = (int) (s.x + r*Math.cos(a));
						coordy = (int) (s.y - r*Math.sin(a));
					}
				}
				else {
					if (s.y <= e.y){
						//GAUCHE - BAS Q3
						coordx = (int) (s.x - r*Math.cos(a));
						coordy = (int) (s.y + r*Math.sin(a));
					}
					else {
						//GAUCHE - HAUT Q2
						coordx = (int) (s.x - r*Math.cos(a));
						coordy = (int) (s.y + r*Math.sin(a));
					}
				}
				p = new Point(coordx,coordy);
				return p;
			}
						
			public void paint(Graphics g) {
				super.paint(g);
				//g.drawLine(35, 35, 325, 325);
				//POS X / Y , Hori / Vert
				g.drawOval(10, 10, 50, 50);
				g.drawOval(300, 300, 50, 50);				
				if (pointStart != null) {
					g.setColor(Color.RED);
					g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);	 
					link1 = calculateCoord(pointStart, pointEnd);
					drawCenteredCircle(g,link1.x,link1.y);
				}
				for (Line l : Lines){	            	
					g.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
					drawCenteredCircle(g, l.getLink1().x,l.getLink1().y );
				}
			}
		};
		f.add(p);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
