package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Questa classe implementa la conversione da coordinate LatLong/WGS84 a coordinate
 * Google Mercator.
 * @author leonardo
 */
public class LatLongToWebMercator extends CoordinateConversionBase {
	private GoogleBingUtils trasformazione = new GoogleBingUtils();

	@Override
	public String getDescription() {
		return "Conversione da LatLong/WGS84 a Google Mercator";
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_X_Y;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		Punto3D result = new Punto3D();
		double coord[] = trasformazione.WGS84toGoogleBing(orig.x, orig.y);
		result.x = coord[0];
		result.y = coord[1];
		return result;
	}

}
