import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Driver {
	static CollisionSimulator col;
	static BallCounter bc;
	static JFrame frame;
	static boolean b1Click;
	static JCheckBox reflectingBorderCheckbox, velocityCheckbox, centerOfMassCheckbox, pathCheckbox, kineticEnergyCheckbox, gravityCheckbox;
	public static void main(String[] args) {

		frame = new JFrame();
		frame.setSize(1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.getContentPane().setBackground(Color.black);
		frame.setVisible(true);

		JButton collision = new JButton("Collision Simulator");
		JPanel collisionSpot = new JPanel();
		collisionSpot.setLayout(null);
		collisionSpot.setBounds(0, 0, 150, 50);
		collision.setBounds(0, 0, 150, 50);
		collisionSpot.add(collision);
		frame.add(collisionSpot);
        collision.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                b1Click = !b1Click;
                if (b1Click) {
                    addCollisionItems();
                } else {
                    removeCollisionItems();
                }
            }
        });

		frame.setVisible(true);	
	

        frame.repaint();
    
		Timer timer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (col != null) {
                    col.update();
                    frame.repaint();
                }
            }
        });
        timer.start();
	}
	
	private static void addCollisionItems() {
		col = new CollisionSimulator();
		col.setBounds(150, 100, 510, 510);
		frame.add(col);
		
		//--- CHECKBOXES ---
		int boxX=750, boxY=210;
		int boxW=300, boxH=50;
		int fontSize = 16;
		
		reflectingBorderCheckbox = new JCheckBox("Reflcting Border", true);
		reflectingBorderCheckbox.setBounds(boxX, boxY, boxW, boxH); 
		reflectingBorderCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		reflectingBorderCheckbox.setBackground(Color.WHITE);
		reflectingBorderCheckbox.setForeground(Color.BLACK);
		reflectingBorderCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.reflectingBorder = reflectingBorderCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(reflectingBorderCheckbox);
		
		velocityCheckbox = new JCheckBox("Display Velocity Vectors", false);
		velocityCheckbox.setBounds(boxX, boxY+boxH, boxW, boxH); 
		velocityCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		velocityCheckbox.setBackground(Color.WHITE);
		velocityCheckbox.setForeground(Color.BLACK);
		velocityCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.showVelocityVectors = velocityCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(velocityCheckbox);
		
		centerOfMassCheckbox = new JCheckBox("Display Center of Mass", false);
		centerOfMassCheckbox.setBounds(boxX, boxY+boxH*2, boxW, boxH); 
		centerOfMassCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		centerOfMassCheckbox.setBackground(Color.WHITE);
		centerOfMassCheckbox.setForeground(Color.BLACK);
		centerOfMassCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.showCenterOfMass = centerOfMassCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(centerOfMassCheckbox);
		
		pathCheckbox = new JCheckBox("Display Path", false);
		pathCheckbox.setBounds(boxX, boxY+boxH*3, boxW, boxH); 
		pathCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		pathCheckbox.setBackground(Color.WHITE);
		pathCheckbox.setForeground(Color.BLACK);
		pathCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.showPath = pathCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(pathCheckbox);
		
		kineticEnergyCheckbox = new JCheckBox("Display Kinetic Energy", false);
		kineticEnergyCheckbox.setBounds(boxX, boxY+boxH*4, boxW, boxH); 
		kineticEnergyCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		kineticEnergyCheckbox.setBackground(Color.WHITE);
		kineticEnergyCheckbox.setForeground(Color.BLACK);
		kineticEnergyCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.showKE = kineticEnergyCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(kineticEnergyCheckbox);
		
		
		gravityCheckbox = new JCheckBox("Activate gravity", false);
		gravityCheckbox.setBounds(boxX, boxY+boxH*5, boxW, boxH); 
		gravityCheckbox.setFont(new Font("Serif", Font.BOLD, fontSize));
		gravityCheckbox.setBackground(Color.WHITE);
		gravityCheckbox.setForeground(Color.BLACK);
		gravityCheckbox.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        col.gravity = gravityCheckbox.isSelected();
		        frame.repaint();  
		    }
		});
		frame.add(gravityCheckbox);
		
		bc = new BallCounter(col);
		frame.add(bc);
		
		
		
		frame.repaint();
	}
	
	private static void removeCollisionItems() {
		if(col == null)
			return;
		frame.remove(col);
		frame.remove(reflectingBorderCheckbox);
		frame.remove(centerOfMassCheckbox);
		frame.remove(velocityCheckbox);
		frame.remove(pathCheckbox);
		frame.remove(kineticEnergyCheckbox);
		frame.remove(gravityCheckbox);		
		frame.remove(bc);
		frame.repaint();
	}
	
	private static void wait(int milisecs) {
		try {
			Thread.sleep(milisecs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}