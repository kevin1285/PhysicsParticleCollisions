import java.awt.Color;
import java.util.*;
public class Particle {
	final int r, m;
	int x, y;
	double vx, vy;
	Color color;
	Queue<int[]> path = new LinkedList<>();
	final double g = 1;
	final double velocityRestitution = .7;
	static final int MAX_PATH_LEN = 50;
	public Particle(int r, int x, int y, double vx, double vy, Color color) {
		this.r = r;
		this.m = r; //assume density is 1
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.color = color;
	}
	
	public Particle(int r, int x, int y, Color c) {
		this(r,x,y,20,20, c);
	}
	public Particle(int x, int y, Color c) {
		this(25,x,y,20,20, c);
	}
	public void updatePosition(double dt) {
		int oldx = x, oldy = y;
		x += (int)(Math.ceil(vx*dt));
		y += (int)(Math.ceil(vy*dt));
		if(CollisionSimulator.gravity)
			vy += g;
		
		if(CollisionSimulator.reflectingBorder) {
			if (x < r + CollisionSimulator.LEFT_BORDER) { 
	            vx *= -1;
	            x = oldx;
			}else if(x > CollisionSimulator.RIGHT_BORDER - r) {
				vx *= -1;
	            x = oldx;
			}
			
	        if (y < r + CollisionSimulator.TOP_BORDER) {
	            vy *= -1;
	            y = oldy;
	        }else if(y > CollisionSimulator.BOTTOM_BORDER - r) {
	        	vy *= -1;
	        	if(CollisionSimulator.gravity)
	        		vy *= velocityRestitution;
	        	if(CollisionSimulator.gravity && Math.abs(vy)<.5)
	        		vy=0;
	            y = oldy;
	          
	        }
		}
		if(path.size() > MAX_PATH_LEN)
			path.poll();
        path.add(new int[] {x,y});
        

	}
	
	public boolean isTouching(Particle o) {
		return this.dist(o) <= (r+o.r);
	}
	
	private double dist(Particle o) {
		int dx = x-o.x;
		int dy = y-o.y;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	public void separate(Particle o) {// two particles may sometimes overlap after a collision. This prevents that.
        double dx = o.x - x;
        double dy = o.y - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        double sumRadius = r + o.r;

        if (this.isTouching(o)) {
            double overlap = (sumRadius - distance) / 2.0; // get half the overlap to adjust each particle equally
            double nx = dx / distance; 
            double ny = dy / distance; 

            x -= overlap * nx;
            y -= overlap * ny;
            o.x += overlap * nx;
            o.y += overlap * ny;
        }
    }
	
	
	public void collide(Particle o) {
		double tempVx = vx;
		double tempVy = vy;
		
		vx = o.vx;
        vy = o.vy;
        o.vx = tempVx;
        o.vy = tempVy;
        separate(o);
 
	}
	
	
	
}