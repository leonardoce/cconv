package it.interfree.leonardoce.iconv.core.convs;

import it.interfree.leonardoce.iconv.core.ConversionException;
import it.interfree.leonardoce.iconv.core.CoordanateStyle;
import it.interfree.leonardoce.iconv.math.Punto3D;
import it.interfree.leonardoce.iconv.math.PuntoMGRS;
import it.interfree.leonardoce.iconv.utils.IConvDatumNames;
import it.interfree.leonardoce.iconv.utils.StringUtils;

public class LatLongToMGRS extends CoordinateConversionBase {

	@Override
	public String getDescription() {
		return "Conversione da LatLong/WGS84 a MGPRS";
	}

	@Override
	public CoordanateStyle getInputStyle() {
		return CoordanateStyle.INPUT_STRING;
	}

	@Override
	public CoordanateStyle getOutputStyle() {
		return CoordanateStyle.INPUT_LATLONG;
	}

	@Override
	public Punto3D convert(Punto3D orig) throws ConversionException {
		double longitudine = orig.x;
		double latitude = orig.y;

		PuntoMGRS risultato = new PuntoMGRS();
		
		// la longitudine varia da -180 a 180. 
		// ogni zona UTM e' grande 6 gradi
		int zona = (int)(Math.floor((longitudine+180)/6)+1);
		risultato.setZonaUtm(zona);
		
		// Controllo di non essere ai poli
		if (latitude<=-80 || latitude>=84) {
			throw new ConversionException("Non si possono convertire le coordinate ai poli");
		}
		
		// Trovare la banda
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

		risultato.setBanda(banda);
		
		// Conversione in UTM
		LatLongToUTM toUtm = new LatLongToUTM(IConvDatumNames.WGS84);
		Punto3D coordinataUtm = toUtm.convert(orig);
		
		// Lettere iniziali delle griglie militari
		int insiemeGriglie = (risultato.getZonaUtm()%6) - 1;
		if (insiemeGriglie==-1) {
			insiemeGriglie += CostantiMGRS.INSIEMI.length;
		}
		
		int scostamentoOrizzontale = Math.min(7, (int)(Math.floor(coordinataUtm.x/100000)-1));
		risultato.x = coordinataUtm.x - 100000*Math.floor(coordinataUtm.x/100000);

		int scostamentoVerticale = (int)Math.floor((Math.abs(coordinataUtm.y) % 2000000)/100000);
		risultato.y = Math.abs(coordinataUtm.y) - 100000*Math.floor(Math.abs(coordinataUtm.y)/100000);
		
		String quadrante =
			"" +
			StringUtils.avanzaLettera(CostantiMGRS.LETTERE_GRIGLIA_ORIZZONTALI,
			CostantiMGRS.INSIEMI[insiemeGriglie].charAt(0),
			scostamentoOrizzontale) +
			StringUtils.avanzaLettera(CostantiMGRS.LETTERE_GRIGLIA_VERTICALI,
					CostantiMGRS.INSIEMI[insiemeGriglie].charAt(1),
			scostamentoVerticale);
		
		risultato.setQuadrante(quadrante);
		
		return risultato;
				
	}

}
