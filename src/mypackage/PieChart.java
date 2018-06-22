package mypackage;
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
	
	final private ArrayList<Slice> slices;
	
	private int selectedIndex = 0;
	private int offset = 0;
	private boolean isAnimated = false;
	
	public PieChart() {
		slices = null;
	}
	
	public PieChart(ArrayList<Item> collection) {
		slices = buildTheSlices(collection);
	}
	 
	private ArrayList<Slice> buildTheSlices(ArrayList<Item> collection) {
		ArrayList<Slice> slices = new ArrayList<Slice>();
		
		int initialAngle = 90;
		int currentAngle = initialAngle;
		int sumOfarcAngles = 0;
		
		double total = 0.0D;
		
		for (Item item : collection) total += item.value;

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
		
		return slices;
	}

	public void paint(Graphics g) {
		drawPie((Graphics2D) g, getBounds(), slices);
	}
	
	public void selectItemByIndex(int newIndex) {
		// doesn't accept new calls if there one already running 
		if (isAnimated) return;
		
		int numberOfSlices = slices.size();
		int offset = 0;
		int index = newIndex;
		
		// if the new index exceeds the number of slices
		if (index > numberOfSlices - 1) {
			index = 0;
		}
		
		// for all except 0 element, calculate the offset 
		if (index > 0) {
			for (int i = 0; i < index; i++) {
				offset += slices.get(i).arcAngle; 
			}
		}
				
		// get the arc angle of the previous slice
		int prevArcAngle = 0;
		
		if (index > 0) {
			prevArcAngle = slices.get(index - 1).arcAngle;
		} else {
			prevArcAngle = slices.get(numberOfSlices - 1).arcAngle;
		}
		
		this.selectedIndex = index;
		
		animateToOffset(offset, prevArcAngle);
	}
	
	private void animateToOffset(int newOffset, int prevArcAngle) {
		// block from overlaying calls 
		isAnimated = true;
		
		float scalar = 0.05f; 
		// calculate the rate by taking in account the size of 
		// the previous slice
		int rate = calculateTheRate(prevArcAngle, scalar);
		
		Timer timer = new Timer();
		
		// hello javascript
		JComponent that = this;
		
		// 60fps
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (offset == newOffset) {
					// turn off the timer
					timer.cancel();
					// release
					isAnimated = false;
					// stop
					return;
				}
				
				// update the global variable
				offset = calculateTheOffset(newOffset, offset, rate);
				
				// render the pie
				that.repaint();
			}
		}, 0, 1000/60);
	}
	
	private int calculateTheRate(int base, float scalar) {
		return (int) (Math.round(((float) base) * scalar));
	}
	
	private int calculateTheOffset(int newOffset, int currentOffset, int rate) {
		int offset = currentOffset;
		// here comes some logic that handles the spin 
		// when it goes beyond 360 degrees
		if (offset + rate > 360) {
			offset = (offset + rate) - 360;
		} else {
			offset = offset + rate;
		}
		
		// this is supposed to catch the current angle and  
		// force it to be equal to the destination angle
		// when it gets close to it
		if (newOffset == 0) {
			if (0 <= offset && offset <= rate) {
				offset = newOffset;
			}
		} else if (offset > newOffset) {
			offset = newOffset;
		}
		
		return offset;
	}
	
	public void nextItem() {
		selectItemByIndex(selectedIndex + 1);
	}

	void drawPie(Graphics2D g, Rectangle area, ArrayList<Slice> slices) {
		
		if (slices == null) return;
		
		int radius = area.width;
		int textYoffset = radius/3;
		
		// draw the actual slices
		for (int i = 0; i < slices.size(); i++) {
			Slice slice = slices.get(i);
			int startAngle = slice.startAngle + this.offset;
			int arcAngle = slice.arcAngle;
			
			g.setColor(slice.getColor());
			g.fillArc(0, 0, radius, radius, startAngle, arcAngle);
			g.setColor(Color.white);
		}
		
		// draw the titles
		for (int i = 0; i < slices.size(); i++) {			
			Slice slice = slices.get(i);
			String title = slice.getTitle();
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