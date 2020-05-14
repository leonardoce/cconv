package leo.cconv.core.convs.test;

import it.interfree.leonardoce.iconv.core.convs.LatLongToCassini;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;
import leo.test.AssertUtils;

import org.junit.Ignore;
import org.junit.Test;

public class TestLatlongToCassini 
{
	@Test
	@Ignore
	public void testConversione() throws Exception
	{
		double lat_0;
		double lon_0;
		
		lat_0 = GeodesicUtils.degreeToDecimal(43, 19, 5.727);
		lon_0 = GeodesicUtils.degreeToDecimal(11, 19, 55.9583);
		
		LatLongToCassini proiezione = new LatLongToCassini(lat_0, lon_0);
		Punto3D origine = new Punto3D(
			11.247851116241, 
			43.767603051351,
			0
		);
		Punto3D destinazione = proiezione.convert(
			origine
		);
		
		AssertUtils.assertEquals2D(
			new Punto2D(-6791.46, 49921.59), 
			destinazione,
			0.5
		);
	}
}
