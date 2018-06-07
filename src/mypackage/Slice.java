package mypackage;

import java.awt.Color;

public class Slice {
	final public int startAngle;
	final public int arcAngle;
	final private Item item;
	
	public Slice(Item item, int startAngle, int arcAngle) {
		this.item = item; 
		this.startAngle = startAngle;
		this.arcAngle = arcAngle;
	}
	
	public String getTitle() {
		return item.title;
	}
	
	public Color getColor() {
		return item.color;
	}
}
