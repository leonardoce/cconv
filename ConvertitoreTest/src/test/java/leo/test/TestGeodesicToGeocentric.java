package leo.test;

import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;

import org.junit.Test;


public class TestGeodesicToGeocentric {
	@Test
	public void testGeocentric() throws Exception
	{
		// prova per la conversione geodetiche/geocentriche
		Punto2D latlong_orig = new Punto2D(-1.204235524670, 43.766913197959);
		Punto2D latlong = new Punto2D(latlong_orig);
		latlong.x = latlong.x * Math.PI / 180;
		latlong.y = latlong.x * Math.PI / 180;
		Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
		
		GeocentricInfo geo = new GeocentricInfo();
		geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);
		Punto3D punto = geo.geodeticToGeocentric(latlong.y, latlong.x, 0);

		//System.out.println(latlong_orig);
		//System.out.println(latlong);
		//System.out.println(punto);
		Punto3D latlong_dest = geo.geocentricToGeodetic(punto.x, punto.y, punto.z);
		//System.out.println(latlong_dest);
		
		AssertUtils.assertEquals2D(latlong, latlong_dest, 0.01);
	}

}
