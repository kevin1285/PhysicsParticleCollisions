import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class RailSimulator extends JPanel {
	private boolean mdirection, edirection, charge;
	private int lastx, lasty;

	public RailSimulator() {
		mdirection = true;
		edirection = true;
		setLayout(null);
		JButton mag = new JButton("Change Magnetic Field Direction");
		mag.setBounds(1300, 100, 250, 50);
		add(mag);
		mag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mdirection = !mdirection;
			}
			
		});
		JButton ele = new JButton("Change Electric Field Direction");
		ele.setBounds(1300, 200, 250, 50);
		add(ele);
		ele.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				edirection = !edirection;
			}
			
		});
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Font font = new Font("Arial", Font.BOLD, 30);
		g.setFont(font);
		drawSetting(g);
	}

	private void drawSetting(Graphics g) {
		// super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(65, 375, 600, 25);
		g.fillRect(65, 575, 600, 25);
		
		for (int i = 0; i < 13; i++) {
			for (int k = 0; k < 10; k++) {
				if(mdirection)
					g.drawString("X", i * 100, k * 100);
				else
					g.drawString("O", i*100, k*100);
			}
		}
		
		eField(g);
		
		
	}
	
	private void eField(Graphics g) { //make sure to account for direction changes
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.green);
		g.setColor(Color.green);
		for(int i = 0; i< 7; i++) {
			g.fillRect(80 + i*90, 410, 16, 150);
			drawArrowHead(g2d, 80+i*90+8, 420, 10);
		}
	}
	
	private void drawArrowHead(Graphics2D g2d, int x, int y, int inc) {
		int s = 40;
		int[] xs = {x-s/2, x, x+s/2};
		int[] ys = {y, y-s/2, y};
		g2d.fillPolygon(xs, ys, 3);
	}
}