package leo.cconv.core.convs.test;

import it.interfree.leonardoce.iconv.core.convs.LatLongToGaussRoma;
import it.interfree.leonardoce.iconv.math.Punto3D;
import leo.test.AssertUtils;

import org.junit.Ignore;
import org.junit.Test;

public class TestLatLongToGaussRoma 
{
	@Test
	@Ignore
	public void testEsempio() throws Exception
	{
		double gauss_nord = 4848529.60;
		double gauss_est = 1680953.78;

		Punto3D orig = new Punto3D(11.247851116241, 43.767603051351, 45.063197800331); 
		LatLongToGaussRoma conv = new LatLongToGaussRoma();
		Punto3D dest = conv.convert(orig);
		
		AssertUtils.assertEquals3D(
				new Punto3D(gauss_est, gauss_nord, 0),
				dest,
				0.01
		);
		
	}
}
