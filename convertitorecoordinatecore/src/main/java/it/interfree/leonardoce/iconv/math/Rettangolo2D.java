package it.interfree.leonardoce.iconv.math;

public class Rettangolo2D 
{
	public double x, y, w, h;

	public Rettangolo2D(double x, double y, double w, double h) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getWidth()
	{
		return w;
	}
	
	public double getHeight()
	{
		return h;
	}
	
	public void add(double px, double py)
	{
		double x2 = x + w;
		double y2 = y + h;
		
		x = Math.min(x, px);
		y = Math.min(y, py);
		w = Math.max(x2, px) - x;
		h = Math.max(y2, py) - y;
	}
	
}
