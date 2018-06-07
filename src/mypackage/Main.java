package mypackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class Main extends JFrame implements ComponentListener {

	private static JPanel contentPane = null;
	private static ArrayList<Item> collection = new ArrayList<Item>();  
	
	final JButton btn;
	final PieChart pc;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		collection.add(new Item("University", 900, Color.black));
		collection.add(new Item("College", 300, Color.green));
		collection.add(new Item("Trade School", 100, Color.blue));
		collection.add(new Item("Primary School", 200, Color.red));
	    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setTitle("Pie Chart");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		contentPane.setLayout(null);
		
		btn = new JButton();	
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		
		contentPane.add(btn);
		
		pc = new PieChart(collection);
		
		updatePositioning(this.getWidth(), this.getHeight());
			
		contentPane.add(pc);
		
		getContentPane().addComponentListener(this);
		
		// on click
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pc.nextItem();
			}
		});
	}
	
	public void updatePositioning(int width, int height) {
		// 2/3 of the window area 
		float fraction = 0.666666f;
		float n = ((float) Math.min(this.getHeight(), this.getWidth())) * fraction;
		int side = (int) Math.round(n);
	
		int yOffset = 25;
		
		int x = (width / 2) - (side / 2);
		int y = (height / 2) - ((side + yOffset) / 2);
		
		btn.setBounds(0, 0, width, height);
		pc.setBounds(x, y, side, side);
	}

	@Override
	public void componentResized(ComponentEvent e) {
	    updatePositioning(this.getWidth(), this.getHeight());
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	
	}

	@Override
	public void componentShown(ComponentEvent e) {
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

}
