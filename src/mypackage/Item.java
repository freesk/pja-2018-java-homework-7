import java.awt.Color;

public class Item {

	public Color color = null;
	public String title = null;
	public int value = 0;

	public Item(String title, int value, Color color) {
		this.title = title;
		this.value = value;
		this.color = color;
	}

}
