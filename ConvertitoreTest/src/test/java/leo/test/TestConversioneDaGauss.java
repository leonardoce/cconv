package leo.test;

import it.interfree.leonardoce.iconv.jhlabs_map.Ellipsoid;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.Projection;
import it.interfree.leonardoce.iconv.jhlabs_map.proj.ProjectionFactory;
import it.interfree.leonardoce.iconv.math.GeocentricInfo;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.math.Punto2D;
import it.interfree.leonardoce.iconv.math.Punto3D;

import org.junit.Assert;
import org.junit.Test;



/*
 * 


leonardo@piero:~/src/proj-4.7.0/src$ cat comando.sh 
./cs2cs -v -f %.12f +proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9 +to +proj=latlong +datum=WGS84



leonardo@piero:~/src/proj-4.7.0/src$ sh comando.sh 
# ---- From Coordinate System ----
#Transverse Mercator
#       Cyl, Sph&Ell
# +proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000
# +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9
# ---- To Coordinate System ----
#Lat/long (Geodetic alias)
#
# +proj=latlong +datum=WGS84 +ellps=WGS84 +towgs84=0,0,0
1680953.78 4848529.60
11.247851116241 43.767603051351 45.063197801205
leonardo@piero:~/src/proj-4.7.0/src$ 




 */

public class TestConversioneDaGauss 
{
	@Test
	public void testLatLong()
	{
		String proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9";
		Projection gauss = ProjectionFactory.fromPROJ4Specification(proiezioneGauss.split(" "));
		double gauss_nord = 4848529.60;
		double gauss_est = 1680953.78;
		
		Punto2D gausspt = new Punto2D(gauss_est, gauss_nord);
		Punto2D latlong = new Punto2D();
		gauss.inverseTransform(gausspt, latlong);

		// Questa conversione e' senza i dati del primo meridiano
		// e senza cambio di ellissoide
		
		Assert.assertEquals(-1.204235524670, latlong.x, 0.01);
		Assert.assertEquals(43.766913197959, latlong.y, 0.01);		
	}
	
	@Test
	public void testConConversioneDatum() throws Exception
	{
		String proiezioneGauss="+proj=tmerc +lat_0=0 +lon_0=-3.45233333333333 +k=0.999600 +x_0=1500000 +y_0=0 +ellps=intl +pm=rome +units=m +no_defs +towgs84=-225,-65,9";
		Projection gauss = ProjectionFactory.fromPROJ4Specification(proiezioneGauss.split(" "));
		double gauss_nord = 4848529.60;
		double gauss_est = 1680953.78;
		
		Punto2D gausspt = new Punto2D(gauss_est, gauss_nord);
		Punto2D latlong = new Punto2D();
		gauss.inverseTransform(gausspt, latlong);

		// Questa conversione e' senza i dati del primo meridiano
		// e senza cambio di ellissoide
		
		Assert.assertEquals(-1.204235524670, latlong.x, 0.01);
		Assert.assertEquals(43.766913197959, latlong.y, 0.01);		

		// Adesso aggiungo il primo meridiano di Roma		
		latlong.x += GeodesicUtils.degreeToDecimal(12, 27, 8.4);
		
		// Porto tutto in geocentriche
		Ellipsoid intl = new Ellipsoid("intl", 6378388.0, 0.0, 297.0, "International 1909 (Hayford)");
		GeocentricInfo geo = new GeocentricInfo();
		geo.setGeocentricParameters(intl.equatorRadius, intl.poleRadius);
		
		Punto2D latlong_radians = GeodesicUtils.degreeToRadians(latlong);
		Punto3D punto = geo.geodeticToGeocentric(latlong_radians.y, latlong_radians.x, 0);
		
		// System.out.println("prima del cambio di datum: " + punto);
		AssertUtils.assertEquals3D(
				new Punto3D(4525021.78699, 899926.62470, 4389502.93985), 
				punto, 
				0.001
		);
		
		// +towgs84=-225,-65,9 (cambio di Datum)
		// -------------------------------------

		punto.x += -225;
		punto.y += -65;
		punto.z += 9;
		
		AssertUtils.assertEquals3D(
				new Punto3D(4524796.78699, 899861.62470, 4389511.93985), 
				punto, 
				0.001
		);		
		
		// Riporto in geodesiche
		// --------------------------------------------------------				
		Ellipsoid wgs = Ellipsoid.WGS_1984;
		geo.setGeocentricParameters(wgs.equatorRadius, wgs.poleRadius);
		Punto3D latlong_dest_radians = geo.geocentricToGeodetic(punto.x, punto.y, punto.z);
		
		AssertUtils.assertEquals3D(
				new Punto3D(0.19631204, 0.76388878, 45.06319780), 
				latlong_dest_radians, 
				0.00001
		);		
		
		
		Punto3D latlong_dest = GeodesicUtils.radiansToDegree(latlong_dest_radians);
		// La Z si misura comunque in radianti
		latlong_dest.z = latlong_dest_radians.z;
		AssertUtils.assertEquals3D(
				new Punto3D(11.247851116241, 43.767603051351, 45.063197800331),
				latlong_dest,
				0.00001
		);
	}
}
