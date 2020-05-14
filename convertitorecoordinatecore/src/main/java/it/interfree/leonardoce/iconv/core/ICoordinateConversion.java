package it.interfree.leonardoce.iconv.core;

import it.interfree.leonardoce.iconv.math.Punto3D;

/**
 * Implementata da tutte le conversioni
 * supportate dal programma 
 */
public interface ICoordinateConversion 
{
	/**
	 * @return La descrizione visualizzata all'interno del 
	 * programma
	 */
	public String getDescription();
	
	/**
	 * @return Stile in input utilizzato nell'interfaccia
	 */
	public CoordanateStyle getInputStyle();

	/**
	 * @return Stile in input utilizzato nell'interfaccia
	 */
	public CoordanateStyle getOutputStyle();
	
	/**
	 * Esegue l'operazione di conversione
	 * @param orig Origine
	 * @return Destinazione
	 */
	public Punto3D convert(Punto3D orig) throws ConversionException;

	/**
	 * Esegue l'operazione di conversione
	 * @param orig Origine
	 * @return Destinazione
	 */
	public Punto3D convert(double x, double y, double z) throws ConversionException;
}
