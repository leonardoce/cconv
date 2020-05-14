package it.interfree.leonardoce.iconv.math;

public class Punto2D 
{
	public double x, y;
	
	public Punto2D(double _x, double _y)
	{
		x = _x;
		y = _y;
	}
	
	public Punto2D()
	{
		x = 0;
		y = 0;
	}
	
	public Punto2D(Punto2D other)
	{
		x = other.x;
		y = other.y;
	}

	@Override
	public String toString()
	{
		return x + " - " + y;
	}
}
