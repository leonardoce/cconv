package leo.test;

import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;

import org.junit.Assert;


public class AssertUtils 
{
	public static void assertEquals2D(Punto2D orig, Punto2D compare, double delta)
	{
		Assert.assertEquals(orig.x, compare.x, delta);
		Assert.assertEquals(orig.y, compare.y, delta);
	}

	public static void assertEquals3D(Punto3D orig, Punto3D compare, double delta)
	{
		Assert.assertEquals(orig.x, compare.x, delta);
		Assert.assertEquals(orig.y, compare.y, delta);
		Assert.assertEquals(orig.z, compare.z, delta);
	}
}
