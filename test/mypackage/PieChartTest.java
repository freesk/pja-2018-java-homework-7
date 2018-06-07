package mypackage;

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

// I guess something went wrong with the design if 
// I have to mess with the private methods. 
// But since it works just fine, let it be this way.

@SuppressWarnings("deprecation")
public class PieChartTest {
	
	private PieChart pc = null;
	private Class<? extends PieChart> c = null;
	private Method method = null;
	private Field field = null;
	private int n = 0;
	private boolean flag = false;
	
	@Before
	public void setUp() {
		pc = new PieChart();
		c = pc.getClass();
	}

	@Test
	public void calculateTheOffsetPlusOne() {
		int offset = 0;
		
		try {
			method = c.getDeclaredMethod("calculateTheOffset", int.class, int.class, int.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			offset = (int) method.invoke(pc, 1, offset, 1);
			// try to get out of bounds 
			offset = (int) method.invoke(pc, 1, offset, 1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Assert.assertEquals(1, offset);	
	}
	
	@Test
	public void calculateTheOffsetThatMakesTheFullSpin() {
		int offset = 0;
		
		try {
			method = c.getDeclaredMethod("calculateTheOffset", int.class, int.class, int.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			// jump to the 0 angle
			offset = (int) method.invoke(pc, 0, 360, 1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Assert.assertEquals(0, offset);	
	}
	
	@Test
	public void calculateTheOffsetChangeCaseOne() {
		int offset = 0;
		
		try {
			method = c.getDeclaredMethod("calculateTheOffset", int.class, int.class, int.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			// jump to the 0 angle
			offset = (int) method.invoke(pc, 4, 359, 5);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Assert.assertEquals(4, offset);
	}
	
	@Test
	public void calculateTheOffsetChangeCaseTwo() {
		int offset = 0;
		
		try {
			method = c.getDeclaredMethod("calculateTheOffset", int.class, int.class, int.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			// jump to the 0 angle
			offset = (int) method.invoke(pc, 104, 100, 5);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Assert.assertEquals(104, offset);
	}
	
	@Test
	public void animateToOffsetBlindTest() {
		try {
			method = c.getDeclaredMethod("animateToOffset", int.class, int.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// start with the 0 offset and animate it to 90
		// 270 is an arbitrary number (must be greater than 0) that affects 
		// the speed of the animation
		
		try {
			method.invoke(pc, 90, 270);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Timer timer = new Timer();
		
		flag = true;
		n = 0;
		
		// wait for the animation completion and check the results
		timer.schedule(new TimerTask() {
			@Override
			public void run() {				
				try {
					field = c.getDeclaredField("offset");
					field.setAccessible(true);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				try {
					n = field.getInt(pc);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				// release
				flag = false;			
			}
		}, 1000);
		
		while (flag) System.out.println("wait");
		
		Assert.assertEquals(90, n);
		
	}
	
	@Test
	public void buildOneSlice() {
		
		ArrayList<Item> collection = new ArrayList<Item>();
		collection.add(new Item("University", 1000, Color.black));
		ArrayList<Slice> slices = null;
		
		try {
			method = c.getDeclaredMethod("buildTheSlices", ArrayList.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			slices = (ArrayList<Slice>) method.invoke(pc, collection);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// the expected slice should cover the entire surface of the cirlce
		Assert.assertEquals(360, slices.get(0).arcAngle);
	}

	@Test
	public void buildTwoSlices() {
		
		ArrayList<Item> collection = new ArrayList<Item>();
		collection.add(new Item("University", 1000, Color.black));
		collection.add(new Item("University", 1000, Color.black));
		ArrayList<Slice> slices = null;
		
		try {
			method = c.getDeclaredMethod("buildTheSlices", ArrayList.class);
			method.setAccessible(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			slices = (ArrayList<Slice>) method.invoke(pc, collection);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// the expected slice should cover half of the surface
		Assert.assertEquals(180, slices.get(0).arcAngle);
	}
}
