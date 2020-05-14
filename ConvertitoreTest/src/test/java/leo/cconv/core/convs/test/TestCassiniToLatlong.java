package leo.cconv.core.convs.test;

import it.interfree.leonardoce.iconv.core.convs.CassiniToLatLong;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;
import leo.test.AssertUtils;

import org.junit.Test;

public class TestCassiniToLatlong 
{
	@Test
	public void testConversione() throws Exception
	{
		double lat_0;
		double lon_0;
		
		lat_0 = GeodesicUtils.degreeToDecimal(43, 19, 5.727);
		lon_0 = GeodesicUtils.degreeToDecimal(11, 19, 55.9583);
		
		CassiniToLatLong proiezione = new CassiniToLatLong(lat_0, lon_0);
		Punto3D origine = new Punto3D(
			-6791.46, 
			49921.59, 
			0
		);
		Punto3D destinazione = proiezione.convert(
			origine
		);
		
		AssertUtils.assertEquals2D(
			new Punto2D(
					11.247851116241, 
					43.767603051351
				), 
			destinazione,
			0.5
		);
	}
}
