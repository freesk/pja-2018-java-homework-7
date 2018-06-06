import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

class PieChart extends JComponent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private class Slice {
		int startAngle = 0;
		int arcAngle = 0;
		Item item = null;
		public Slice(Item item, int startAngle, int arcAngle) {
			this.item = item; 
			this.startAngle = startAngle;
			this.arcAngle = arcAngle;
		}
	}
	
	private ArrayList<Slice> slices = null;
	private int selectedIndex = 0;
	private int offset = 0;
	private boolean isAnimated = false;
	
	public PieChart(ArrayList<Item> collection) {
		slices = new ArrayList<Slice>();
		
		double total = 0.0D;

		for (int i = 0; i < collection.size(); i++) {
			total += collection.get(i).value;
		}
		
		int initialAngle = 90;
		int currentAngle = initialAngle;
		int sumOfarcAngles = 0;

		for (int i = 0; i < collection.size(); i++) {

			int arcAngle = (int) Math.round(collection.get(i).value * 360 / total);
			int startAngle = currentAngle - arcAngle;
			currentAngle -= arcAngle;
			sumOfarcAngles += arcAngle;
			
			// round the last slice size
			if (i == collection.size() - 1) {  
				// correct the size so it doesn't interfere with the nearby slices
				arcAngle = arcAngle + 360 - sumOfarcAngles;
				// a constant for the last slice 
				startAngle = -360 + initialAngle;
			}
			
			slices.add(new Slice(collection.get(i), startAngle, arcAngle));
		}
		
	}

	public void paint(Graphics g) {
		drawPie((Graphics2D) g, getBounds(), slices);
	}
	
	public void selectItemByIndex(int newIndex) {
		
		if (isAnimated) return;
		
		int offset = 0;
		int index = newIndex;
		
		if (newIndex > slices.size() - 1) {
			index = 0;
		}
		
		if (index > 0) {
			for (int i = 0; i < index; i++) {
				offset += slices.get(i).arcAngle; 
			}
		}
		
		this.selectedIndex = index;
		
		animateToOffset(offset);
	}
	
	private void animateToOffset(int newOffset) {
		isAnimated = true;
		
		Timer timer = new Timer();
		
		JComponent that = this;
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (offset == newOffset) {
					timer.cancel();
					isAnimated = false;
					return;
				} 
//				System.out.println("tick");
				offset = offset + 2;
				if (offset > 360) offset = 0;
				that.repaint();
				
			}
		}, 0, 1000/60);
	}
	
	public void nextItem() {
		selectItemByIndex(selectedIndex + 1);
	}

	void drawPie(Graphics2D g, Rectangle area, ArrayList<Slice> slices) {
		
		int radius = area.width;
		int textYoffset = radius/3;
		
		// draw the actual slices
		for (int i = 0; i < slices.size(); i++) {
			Slice slice = slices.get(i);
			int startAngle = slice.startAngle + this.offset;
			int arcAngle = slice.arcAngle;
			
			g.setColor(slice.item.color);
			g.fillArc(0, 0, radius, radius, startAngle, arcAngle);
			g.setColor(Color.white);
		}
		
		// draw the titles
		for (int i = 0; i < slices.size(); i++) {			
			Slice slice = slices.get(i);
			String title = slice.item.title;
			int startAngle = slice.startAngle + this.offset;
			int arcAngle = slice.arcAngle;
			
			// calculate the width of the string (title)
			FontMetrics metrics = g.getFontMetrics();
			int stringWidth = metrics.stringWidth(title);
			
			// calculate the position
			int x = (int) (textYoffset * Math.cos(Math.toRadians(startAngle + arcAngle / 2)));
			int y = -(int) (textYoffset * Math.sin(Math.toRadians(startAngle + arcAngle / 2)));
			
			// draw the title
			g.drawString(title, radius / 2 + x - stringWidth / 2, radius / 2 + y);
		}
	}
}