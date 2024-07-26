import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class CollisionSimulator extends JPanel{
	static final int TOP_BORDER=5, LEFT_BORDER=5, BOTTOM_BORDER=500, RIGHT_BORDER=500;
	static double elasticity = 1;
	static boolean reflectingBorder=true, gravity;
	boolean showVelocityVectors, showCenterOfMass, showPath, showKE;
	ArrayList<Particle> particles = new ArrayList<>();
	Color[] colors = new Color[] { 
			new Color(255, 0, 0),    // Red
		    new Color(0, 255, 0),    // Green
		    new Color(0, 0, 255),    // Blue
		    new Color(255, 165, 0),  // Orange
		    new Color(255, 0, 255),  // Magenta
		    new Color(0, 255, 255),  // Cyan
		    new Color(255, 192, 203),// Pink
		    new Color(128, 0, 128),  // Purple
		    new Color(255, 215, 0),  // Gold
		    new Color(75, 0, 130)    // Indigo
	};
	int colorIdx = 0;
	
	public CollisionSimulator() {
		for(int i=0; i<2; i++) {		
			addParticle();
		}
	}
	public CollisionSimulator(ArrayList<Particle> particles) {
		this.particles = particles;
	}
	
	public void addParticle() {
		while(true) {
			int r = 20;
			int x =(int)(Math.random()*(RIGHT_BORDER-2*r)) + r;
			int y =(int)(Math.random()*(BOTTOM_BORDER-2*r)) + r;
			Particle toAdd = new Particle(r, x, y, Math.random()*10+2, Math.random()*10+2, colors[colorIdx]);
			boolean touched = false;
			for(Particle p : particles) {
				if(p.isTouching(toAdd)) {
					touched = true;
					break;
				}
			}
			if(!touched) {
				particles.add(toAdd);
				colorIdx = (colorIdx+1)%colors.length;
				break;
			}
			
		}
	}
	
	private double getTotalKE() {
		double K = 0;
		for(Particle p : particles) {
			K += .5 * p.m * (p.vx*p.vx + p.vy*p.vy);
		}
		return K;
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.LIGHT_GRAY);
		for(Particle p : particles) {
			g.setColor(p.color);
			g.fillOval(p.x-p.r, p.y-p.r, 2*p.r, 2*p.r);
			
			if(showVelocityVectors && p.vx != 0 && p.vy != 0)
				drawVelocityVector((Graphics2D) g, p, Color.BLACK);
			
			if(showPath)
				drawPath((Graphics2D)g, p);
		}
		
		drawBorders(g);
		
		if(showCenterOfMass) {
			drawCenterOfMass((Graphics2D)(g));
		}
		if(showKE) {
			g.setFont(new Font("Serif", Font.BOLD, 18));
			g.drawString("Kinetic Energy: " + Math.round(100*getTotalKE())/100.0 + "J", 20, 485);
		}
		
	}
	private void drawBorders(Graphics g) {
		final int thickness = 5;
		g.setColor(Color.BLACK);
		g.fillRect(LEFT_BORDER, TOP_BORDER, RIGHT_BORDER, thickness);
		g.fillRect(LEFT_BORDER, TOP_BORDER, thickness, BOTTOM_BORDER);
		g.fillRect(LEFT_BORDER, BOTTOM_BORDER, RIGHT_BORDER, thickness);
		g.fillRect(RIGHT_BORDER, TOP_BORDER, thickness, BOTTOM_BORDER);
	}
	
	public void update() {
        for (Particle p1 : particles) {
            p1.updatePosition(1);
            for (Particle p2 : particles) {
                if (p1 != p2 && p1.isTouching(p2)) {
                    p1.collide(p2);
                }
            }
        }
        repaint();
    }
	
	private int[] getCenterOfMass() {
		int sumX = 0, sumY = 0;
		int totalMass = 0;
		for(Particle p : particles) {
			sumX += p.x * p.m;
			sumY += p.y * p.m;
			totalMass += p.m;
		}
		return new int[] {sumX/totalMass, sumY/totalMass};
	}
    private void drawVelocityVector(Graphics2D g2, Particle particle, Color c) {
        g2.setColor(c); // Color of the velocity vector
        int x1 = particle.x;
        int y1 = particle.y;
        int x2 = (int) (x1 + particle.vx*15); 
        int y2 = (int) (y1 + particle.vy*15);

        drawArrow(g2, x1, y1, x2, y2);
    }
    
    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.drawLine(x1, y1, x2, y2); 
        drawArrowHead(g2, x1, y1, x2, y2); 
    }
    private void drawArrowHead(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowLen = 5; 
        g2.drawLine(x2, y2, (int) (x2 - arrowLen * Math.cos(angle - Math.PI / 6)),
                    (int) (y2 - arrowLen * Math.sin(angle - Math.PI / 6)));
        g2.drawLine(x2, y2, (int) (x2 - arrowLen * Math.cos(angle + Math.PI / 6)),
                    (int) (y2 - arrowLen * Math.sin(angle + Math.PI / 6)));
    }
    
    private void drawCenterOfMass(Graphics g2) {
    	int[] centerOfMass = getCenterOfMass();
    	int xcm = centerOfMass[0], ycm = centerOfMass[1];
    	drawX(g2, xcm, ycm, 10);
    }
    private void drawX(Graphics g2, int x, int y, int size) {
    	((Graphics2D) g2).setStroke(new BasicStroke(3));
    	g2.setColor(Color.BLACK);  
        g2.drawLine(x - size, y - size, x + size, y + size);
        g2.drawLine(x + size, y - size, x - size, y + size);
        
        ((Graphics2D) g2).setStroke(new BasicStroke(1));
    }
    
    private void drawPath(Graphics2D g2, Particle p) {
    	int alpha = 255;  // full opacity
    	if(p.path.size() ==0)
    		return;
        int step = alpha / p.path.size();  
        ArrayList<int[]> path = new ArrayList<>(p.path);
        Collections.reverse(path);
        int[] prevPoint = path.get(0);  // Start from the oldest point
        for (int i = 1; i < path.size(); i++) {
            int[] curPoint = path.get(i);
            g2.setColor(new Color(p.color.getRed(), p.color.getGreen(), p.color.getBlue(), alpha));
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));  
            g2.drawLine(prevPoint[0], prevPoint[1], curPoint[0], curPoint[1]);  
            prevPoint = curPoint; 
            alpha -= step; 
            if (alpha < 0) {
                alpha = 0;  
            }
        }
        g2.setStroke(new BasicStroke(1));  
    }
}


class BallCounter extends JPanel {
	CollisionSimulator col;
    JLabel countLabel;
    JButton increaseButton, decreaseButton;

    public BallCounter(CollisionSimulator c) {
    	col = c;
        setLayout(null);
        setSize(1024, 768);
        setBackground(Color.BLACK);


        countLabel = new JLabel(col.particles.size()+"");
        countLabel.setBackground(Color.BLACK);
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(JLabel.CENTER);
        countLabel.setVerticalAlignment(JLabel.CENTER);
        countLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        countLabel.setText("Particles: " + col.particles.size()+"");
        countLabel.setFont(new Font("Serif", Font.BOLD, 16));
        increaseButton = new JButton("▲");
        decreaseButton = new JButton("▼");

        
        
        int x=750, y=100;
        countLabel.setBounds(x, y, 120, 60); 
        increaseButton.setBounds(x+countLabel.getBounds().width, y, 50, 30); 
        decreaseButton.setBounds(x+countLabel.getBounds().width, y+30, 50, 30); 
 
        add(countLabel);
        add(increaseButton);
        add(decreaseButton);

        increaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                col.addParticle();
                countLabel.setText("Particles: " + col.particles.size()+"");
            }
        });

        decreaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (col.particles.size() > 1) { 
                    col.particles.remove(col.particles.size()-1);
                    countLabel.setText("Particles: " + col.particles.size()+"");
                }
                countLabel.setText("Particles: " + col.particles.size()+"");
            }
        });
    }
}