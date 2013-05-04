package grouppractical.robot;


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
         x = Math.cos(theta) * x - Math.sin(theta) * y;
         y = Math.sin(theta) * x + Math.cos(theta) * y;
     }
}
