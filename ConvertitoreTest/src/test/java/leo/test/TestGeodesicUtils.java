package leo.test;

import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;

import org.junit.Assert;
import org.junit.Test;

public class TestGeodesicUtils {
	@Test
	public void testDegreeRadians() throws Exception
	{
		Punto2D latlong_orig = new Punto2D(-1.204235524670, 43.766913197959);
		Punto2D latlong_conv = GeodesicUtils.degreeToRadians(latlong_orig);
		
		Punto2D latlong = new Punto2D(latlong_orig);
		latlong.x = latlong.x * Math.PI / 180;
		latlong.y = latlong.y * Math.PI / 180;
		
		AssertUtils.assertEquals2D(latlong, latlong_conv, 0.01);
	}
	
	@Test
	public void testRadiansToDegree() throws Exception
	{
		Punto2D latlong_orig = new Punto2D(-1.204235524670, 43.766913197959);		
		Punto2D latlong = new Punto2D(latlong_orig);
		latlong.x = latlong.x * Math.PI / 180;
		latlong.y = latlong.y * Math.PI / 180;

		Punto2D reverse = GeodesicUtils.radiansToDegree(latlong);
		AssertUtils.assertEquals2D(latlong_orig, reverse, 0.01);		
	}
	
	@Test
	public void testDegreeToRadians() throws Exception
	{
		Assert.assertEquals(12.45233, GeodesicUtils.degreeToDecimal(12, 27, 8.4), 0.111);
	}
}
