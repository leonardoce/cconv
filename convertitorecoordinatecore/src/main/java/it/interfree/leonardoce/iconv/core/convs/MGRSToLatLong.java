package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoMGRS;
import it.interfree.leonardoce.iconv.math.PuntoUTM;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;
import it.interfree.leonardoce.iconv.utils.StringUtils;

/**
 * Conversione da MGRS a latlong/wgs84
 * @author leonardo
 *
 */
public class MGRSToLatLong extends CoordinateConversionBase {

	@Override
	public String getDescription() {
		return "Conversione da MGRS a LatLong/WGS84";
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_STRING;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_X_Y;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		if (!(orig instanceof PuntoMGRS))
		{
			throw new ConversionException("Punto di origine errato");
		}
		
		PuntoMGRS mgrs = (PuntoMGRS)orig;
		int zonaUtm = mgrs.getZonaUtm();
		int insiemeGriglie = (zonaUtm%6) - 1;
		if (insiemeGriglie<0) {
			insiemeGriglie+=6;
		}
		String inizialeGriglia = CostantiMGRS.INSIEMI[insiemeGriglie];
		
		double inquadramentoOrizzontale = 
				100000*(1+StringUtils.differenzaLettere(CostantiMGRS.LETTERE_GRIGLIA_ORIZZONTALI, mgrs.getQuadrante().charAt(0), inizialeGriglia.charAt(0)));
		
		double inquadramentoVerticale = 
				100000*(StringUtils.differenzaLettere(CostantiMGRS.LETTERE_GRIGLIA_VERTICALI, mgrs.getQuadrante().charAt(1), inizialeGriglia.charAt(1)));
		
		UTMToLatLong utmToLatlong = new UTMToLatLong(IConvDatumNames.WGS84);
		
		// tentativo di riduzione
		Punto3D risultato = null;
		
		for(int i=0;i<10;i++)
		{
			// Qua deve semplicemente provare a convertire il
			// punto e vedere come diventa la zona
			// punto.x = inquadramentoOrizzontale + orig.x;
			// punto.y = inquadramentoVerticale + orig.y + 2000000*i;
			PuntoUTM punto = new PuntoUTM(inquadramentoOrizzontale+orig.x, 
				inquadramentoVerticale + orig.y + 2000000*i, 
				0, zonaUtm);
			
			try
			{
				Punto3D test = utmToLatlong.convert(punto);
				
				double latitude = test.y;
				int idxBanda = (int)Math.floor((latitude+80)/8);
				char banda;
				
				if (idxBanda>19)
				{
					banda = CostantiMGRS.LETTERE_BANDA[19];
				}
				else
				{
					banda = CostantiMGRS.LETTERE_BANDA[idxBanda];
				}
				
				if (banda==mgrs.getBanda())
				{
					risultato = test;
					break;
				}
			} catch(ConversionException cx)
			{
				// sfiga... succede
			}

			// Attenzione: deve essere anche provato il punto
			// nell'emisfero SUD. Non si sa mai cosa succede

			punto = new PuntoUTM(inquadramentoOrizzontale+orig.x,
					-1 * (inquadramentoVerticale + orig.y + 2000000*i),
					0, zonaUtm);

			try
			{
				Punto3D test = utmToLatlong.convert(punto);

				double latitude = test.y;
				int idxBanda = (int)Math.floor((latitude+80)/8);
				char banda;

				if (idxBanda>19)
				{
					banda = CostantiMGRS.LETTERE_BANDA[19];
				}
				else if (idxBanda<0)
				{
					banda = CostantiMGRS.LETTERE_BANDA[0];
				}
				else
				{
					banda = CostantiMGRS.LETTERE_BANDA[idxBanda];
				}

				if (banda==mgrs.getBanda())
				{
					risultato = test;
					break;
				}
			} catch(ConversionException cx)
			{
				// sfiga... succede
			}
		}
		
		if (risultato == null)
		{
			throw new ConversionException("dati errati");
		}
		
		return risultato;
	}

}
