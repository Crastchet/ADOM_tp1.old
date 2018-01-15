package kroa100;

public class Distance {

	private City a,b;
	private double distance;
	
	public Distance(City a, City b) {
		this.a = a;
		this.b = b;
	}
	
	
	public double getValue() {
		return this.distance;
	}
	
	public void setValue(double dist) {
		this.distance = Math.round(dist);
	}
	
	public City getCityA() {
		return this.a;
	}
	
	public City getCityB() {
		return this.b;
	}
	
	/*public double update() {
		this.distance = Math.sqrt( Math.pow(a.x-b.x, 2) + Math.pow(a.y-b.y, 2) );
		return this.distance;
	}*/
	
	@Override
	public boolean equals(Object o) {
		Distance dist = (Distance) o;
		return this.a.equals(dist.a) && this.b.equals(dist.b)
			|| this.b.equals(dist.a) && this.a.equals(dist.b);
	}
}
