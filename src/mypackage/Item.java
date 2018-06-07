package mypackage;
import java.awt.Color;

public class Item {

	final public Color color;
	final public String title;
	final public int value;

	public Item(String title, int value, Color color) {
		this.title = title;
		this.value = value;
		this.color = color;
	}

}
