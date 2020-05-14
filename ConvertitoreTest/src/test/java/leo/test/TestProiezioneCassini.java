package leo.test;

import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;

import org.junit.Assert;
import org.junit.Test;


/*

leonardo@piero:~$ proj -v +proj=cass +ellps=bessel  +lat_0=43d19\'05.727\"N +lon_0=11d19\'55.9583\"E 
#Cassini
#       Cyl, Sph&Ell
# +proj=cass +ellps=bessel +lat_0=43d19'05.727"N +lon_0=11d19'55.9583"E
11.247851116241 43.767603051351 45.063197801205
-6791.69        49921.74 45.063197801205

*/

public class TestProiezioneCassini 
{
	@Test
	public void testProiezioneCassini()
	{
		final String proiezioneCassini = "+proj=cass +ellps=bessel +lat_0=43d19\'05.727\"N +lon_0=11d19\'55.9583\"E";
		Projection cassini = ProjectionFactory.fromPROJ4Specification(proiezioneCassini.split(" "));
		
		Punto2D origine = new Punto2D(
			GeodesicUtils.degreeToDecimal(11, 19, 55.9583), 
			GeodesicUtils.degreeToDecimal(43, 19, 5.727) 
		);
		
		// Verifica che abbia decodificato bene, come stringa, il
		// punto di emanazione
		origine = GeodesicUtils.degreeToRadians(origine);
		Assert.assertEquals(origine.x, cassini.getProjectionLongitude(), 0.0001);
		Assert.assertEquals(origine.y, cassini.getProjectionLatitude(), 0.0001);

		Punto2D destinazione = new Punto2D();
		//cassini.project(11.247851116241, 43.767603051351, destinazione);
		cassini.transform(
			11.247851116241, 
			43.767603051351, 
			destinazione);
		
		AssertUtils.assertEquals2D(
			new Punto2D(-6791.46, 49921.59), 
			destinazione,
			0.5
		);
	}

}
