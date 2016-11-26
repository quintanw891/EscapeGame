package EscapeGame;

import java.awt.*;
import java.awt.geom.Line2D;

public class Guard extends Rectangle{
	public Point[] path;
	public int phase;
	public Line2D.Double sight = new Line2D.Double(0,0,0,0);
	//the guard's constructor takes an int that determines the # of points he visits in his walk cycle
	public Guard(int stops){
		path = new Point[stops];
		phase = 1;
	}
}
