package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.math.Punto3D;



public class WebMercatorToLatLong extends CoordinateConversionBase 
{
	private GoogleBingUtils trasformazione = new GoogleBingUtils();

	public WebMercatorToLatLong()
	{
	}

	@Override
	public String getDescription() {
		return "Retro-proiezione in coordinate Google Mercator";
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_X_Y;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		Punto3D result = new Punto3D();
		double coord[] = trasformazione.GoogleBingtoWGS84Mercator(orig.x, orig.y);
		result.x = coord[0];
		result.y = coord[1];
		return result;
	}

}
