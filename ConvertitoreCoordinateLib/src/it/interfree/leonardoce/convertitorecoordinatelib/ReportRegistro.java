package it.interfree.leonardoce.convertitorecoordinatelib;

import android.content.Context;

import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.utils.ResUtils;
import it.interfree.leonardoce.iconv.utils.StringUtils;

import java.util.Date;
import java.text.DecimalFormat;

public class ReportRegistro {
	private Context context;

	public ReportRegistro(Context context) {
		this.context = context;
	}

	public void aggiungiRisultatoInReportTesto(StringBuilder build, RisultatoConversione ris) {
		java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
		java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
		DecimalFormat dec = new DecimalFormat("###,##0.00");
		DecimalFormat decGradiDecimali = new DecimalFormat("###,##0.000000");

		build.append(""+escape(context.getString(R.string.log_data_punto))+"");
		build.append(": ");
		build.append(escape(dateFormat.format(new Date(ris.time_last))));
		build.append(" ");
		build.append(escape(timeFormat.format(new Date(ris.time_last))));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_tipo_conversione))+"");
		build.append(": ");
		build.append(escape(ris.tipo_ingresso));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_descrizione))+"");
		build.append(": ");
		build.append(escape(ris.descrizione_punto));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_latlong))+"");
		build.append(": ");
		build.append(escape(ResUtils.formatLatLong(context, ris.lat, ris.longi)));
		build.append("\n\n");
		build.append(""+escape(context.getString(R.string.log_latlong_dec))+"");
		build.append(": ");
		build.append(escape(decGradiDecimali.format(ris.lat)));
		build.append(" ");
		build.append(escape(decGradiDecimali.format(ris.longi)));
		build.append("\n\n");

		build.append(""+escape(context.getString(R.string.log_latlong_ed50))+"");
		build.append(": ");
		build.append(escape(ResUtils.formatLatLong(context, ris.lat_ed50, ris.longi_ed50)));
		build.append("\n\n");
		build.append(""+escape(context.getString(R.string.log_latlong_dec_ed50))+"");
		build.append(": ");
		build.append(escape(decGradiDecimali.format(ris.lat_ed50)));
		build.append(" ");
		build.append(escape(decGradiDecimali.format(ris.longi_ed50)));
		build.append("\n\n");
		
		
		build.append(""+escape(context.getString(R.string.log_fuso_gauss))+"");
		build.append(": ");
		build.append(escape(ResUtils.fuso2string(context, ris.fuso)));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_gauss))+"");
		build.append(": ");
		build.append("Nord ");
		build.append(escape(dec.format(ris.nord)));
		build.append(" Est ");
		build.append(escape(dec.format(ris.est)));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_origine_cassini))+"");
		build.append(": ");
		build.append(escape(ris.idOrigineCassini));
		build.append("\n\n");
		
		build.append(""+escape(context.getString(R.string.log_cassini))+"");
		build.append(": ");
		build.append("X ");
		build.append(escape(dec.format(ris.x)));
		build.append(" Y ");
		build.append(escape(dec.format(ris.y)));
		build.append("\n\n");

		build.append(""+escape(context.getString(R.string.log_zona_utm))+"");
		build.append(": ");
		build.append(escape("" + ris.zonaUtm));
		build.append("\n\n");

		build.append(""+escape(context.getString(R.string.log_datum_utm))+"");
		build.append(": ");
		build.append(escape("" + ris.utm_datum));
		build.append("\n\n");

		build.append(""+escape(context.getString(R.string.log_utm))+"");
		build.append(": ");
		build.append("X ");
		build.append(escape(dec.format(ris.utm_est)));
		build.append(" Y ");
		build.append(escape(dec.format(ris.utm_nord)));
		build.append("\n\n");

		build.append(""+escape(context.getString(R.string.log_mgrs))+"");
		build.append(": ");
		build.append(ris.puntoMgrs);
	}

	private String escape(String s) {
		return s;
	}
}
