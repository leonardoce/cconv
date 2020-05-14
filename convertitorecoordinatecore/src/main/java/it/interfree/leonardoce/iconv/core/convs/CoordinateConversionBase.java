package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.ICoordinateConversion;
import it.interfree.leonardoce.iconv.math.Punto3D;

public abstract class CoordinateConversionBase implements ICoordinateConversion 
{
	public Punto3D convert(double x, double y, double z) throws ConversionException
	{
		return convert(new Punto3D(x,y,z));
	}

}
