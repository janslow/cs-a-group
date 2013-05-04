package grouppractical.robot;

/**
 * 
 * @author Pete York
 *
 */
public class Vector2D {
	public Vector2D(double x, double y) {
		this.x=x;this.y=y; 
	}

	private double x, y;
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	
	 public void translate(Vector2D v)
     {
         x = x + v.getX();
         y = y + v.getY();
     }

     public void rotate(double theta)
     {
    	 double oldX = x;
    	 double oldY = y;
    	 x = Math.cos(theta) * oldX - Math.sin(theta) * oldY;
         y = Math.sin(theta) * oldX + Math.cos(theta) * oldY;
     }
}
