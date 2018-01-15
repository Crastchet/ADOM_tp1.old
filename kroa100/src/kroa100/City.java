package kroa100;

import java.awt.Point;

public class City {

	private String name;
	private Point location;
	
	public City(String name, Point loc) {
		this.name = name;
		this.location = loc;
	}
	
	
	public double x() {
		return this.location.x;
	}
	
	public double y() {
		return this.location.y;
	}
	
	public boolean equals(Object o) {
		City c = (City) o;
		return c.name.equals(this.name) && c.location.equals(this.location);
	}
	
	
	public String toString() {
		return this.name;
	}
}
