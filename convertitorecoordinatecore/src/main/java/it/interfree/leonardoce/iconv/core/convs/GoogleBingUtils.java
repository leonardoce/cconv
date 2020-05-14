package it.interfree.leonardoce.iconv.core.convs;

/**
 * Funzioni che realizzano la conversione WGS84 / Google Mercator.
 * Fonte:
 * http://alastaira.wordpress.com/2011/01/23/the-google-maps-bing-maps-spherical-mercator-projection/
 */
public class GoogleBingUtils {
	double[] WGS84toGoogleBing(double lat, double lon) {
		double x = lon * 20037508.34 / 180;
		double y = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
		y = y * 20037508.34 / 180;
		return new double[] {x, y};
	}

	double[] GoogleBingtoWGS84Mercator (double x, double y) {
		double lon = (x / 20037508.34) * 180;
		double lat = (y / 20037508.34) * 180;

		lat = 180/Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
		return new double[] {lon, lat};
	}
}
