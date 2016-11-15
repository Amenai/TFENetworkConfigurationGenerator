package packSystem;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

public interface Paintable {

    public void paint(JComponent parent, Graphics2D g2d);

    public boolean contains(Point p);

    public void moveTo(Point p);

    public Rectangle2D getBounds();

}