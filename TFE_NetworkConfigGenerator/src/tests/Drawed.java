package tests;

import java.awt.Color;
import java.awt.Graphics;
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
	
	public Line(Point start,Point end){
		this.setStart(start);
		this.setEnd(end);	
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
	        {
	            addMouseListener(new MouseAdapter() {
	                public void mousePressed(MouseEvent e) {
	                	if (isInStartingPoint(e.getPoint())){
	                		pointStart = new Point(35,35);
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
								Lines.add(new Line(pointStart,pointEnd));
								pointStart = null;
							}
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
	        
	        public void paint(Graphics g) {
	            super.paint(g);
	            //g.drawLine(35, 35, 325, 325);
	            //POS X / Y , Hori / Vert
	            g.drawOval(10, 10, 50, 50);
	            g.drawOval(300, 300, 50, 50);
	            if (pointStart != null) {
	            	g.setColor(Color.RED);
	                g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);	 	              
	           }
	            for (Line l : Lines){	            	
                	g.drawLine(l.getStart().x, l.getStart().y, l.getEnd().x, l.getEnd().y);
                }
	        }
	    };
	    f.add(p);
	    f.setVisible(true);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
