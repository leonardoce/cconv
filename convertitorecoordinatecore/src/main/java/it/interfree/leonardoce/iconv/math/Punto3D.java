package it.interfree.leonardoce.iconv.math;

public class Punto3D extends Punto2D 
{
	public double z;
	
	public Punto3D(double px, double py, double pz)
	{
		super(px,py);
		z = pz;
	}
	
	public Punto3D()
	{
		super(0,0);
		z = 0;
	}
	
	@Override
	public String toString()
	{
		return x + " - " + y + " - " + z;
	}
}
