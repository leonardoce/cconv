package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.db.RisultatoConversione;
import it.interfree.leonardoce.iconv.math.GeodesicUtils;
import it.interfree.leonardoce.iconv.utils.ResUtils;
import it.interfree.leonardoce.iconv.utils.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class ReportRegistroActivity extends AppCompatActivity
{
	private WebView registroHtmlView;
	private String htmlString, csvString;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.report_registro);
		registroHtmlView = (WebView)findViewById(R.id.registroHtmlView);
		
		refresh();
	}
	
	private void refresh()
	{
		List<RisultatoConversione> risultati = null;		
		IOriginiStorage db = new OriginiCassiniManager(ReportRegistroActivity.this);
		
		try
		{
			risultati = db.getLog();
		}
		finally
		{
			db.close();
		}
		
		// = Generazione dell'HTML =
		
		StringBuilder build = new StringBuilder();
		
		build.append("<html><head><title>Log Conversioni</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head>");
		
		java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
		java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
    	DecimalFormat dec = new DecimalFormat("###,##0.00");
    	DecimalFormat decGradiDecimali = new DecimalFormat("###,##0.000000");
    	boolean primaRiga = true;
		
		for(RisultatoConversione ris : risultati)
		{
			if (!primaRiga)
			{
				build.append("<hr>");
			}
			primaRiga = false;
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_data_punto))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(dateFormat.format(new Date(ris.time_last))));
			build.append(" ");
			build.append(TextUtils.htmlEncode(timeFormat.format(new Date(ris.time_last))));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_tipo_conversione))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(ris.tipo_ingresso));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_descrizione))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(ris.descrizione_punto));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong))+"</b>");
			build.append(": ");
			build.append("<a href=\"" + ris.getUrlGoogleMaps() + "\">");
			build.append(TextUtils.htmlEncode(ResUtils.formatLatLong(this, ris.lat, ris.longi)));
			build.append("</a>");			
			build.append("<br>");
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong_dec))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(decGradiDecimali.format(ris.lat)));
			build.append(" ");
			build.append(TextUtils.htmlEncode(decGradiDecimali.format(ris.longi)));
			build.append("<br>");
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong_min_dec))+"</b>");
			build.append(": ");
			build.append(ResUtils.formatMinutesLatLong(this, ris.lat, ris.longi));
			build.append("<br>");

			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong_ed50))+"</b>");
			build.append(": ");
			build.append("<a href=\"" + ris.getUrlGoogleMaps() + "\">");
			build.append(TextUtils.htmlEncode(ResUtils.formatLatLong(this, ris.lat_ed50, ris.longi_ed50)));
			build.append("</a>");			
			build.append("<br>");
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong_dec_ed50))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(decGradiDecimali.format(ris.lat_ed50)));
			build.append(" ");
			build.append(TextUtils.htmlEncode(decGradiDecimali.format(ris.longi_ed50)));
			build.append("<br>");
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_latlong_min_dec_ed50))+"</b>");
			build.append(": ");
			build.append(ResUtils.formatMinutesLatLong(this, ris.lat_ed50, ris.longi_ed50));
			build.append("<br>");
			
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_fuso_gauss))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(ResUtils.fuso2string(this, ris.fuso)));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_gauss))+"</b>");
			build.append(": ");
			build.append("Nord ");
			build.append(TextUtils.htmlEncode(dec.format(ris.nord)));
			build.append(" Est ");
			build.append(TextUtils.htmlEncode(dec.format(ris.est)));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_origine_cassini))+"</b>");
			build.append(": ");
			build.append(TextUtils.htmlEncode(ris.idOrigineCassini));
			build.append("<br>");
			
			build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_cassini))+"</b>");
			build.append(": ");
			build.append("X ");
			build.append(TextUtils.htmlEncode(dec.format(ris.x)));
			build.append(" Y ");
			build.append(TextUtils.htmlEncode(dec.format(ris.y)));
			build.append("<br>");

            build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_zona_utm))+"</b>");
            build.append(": ");
            build.append(TextUtils.htmlEncode("" + ris.zonaUtm));
            build.append("<br>");

            build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_datum_utm))+"</b>");
            build.append(": ");
            build.append(TextUtils.htmlEncode("" + ris.utm_datum));
            build.append("<br>");

            build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_utm))+"</b>");
            build.append(": ");
            build.append("X ");
            build.append(TextUtils.htmlEncode(dec.format(ris.utm_est)));
            build.append(" Y ");
            build.append(TextUtils.htmlEncode(dec.format(ris.utm_nord)));
            build.append("<br>");

            build.append("<b>"+TextUtils.htmlEncode(getString(R.string.log_mgrs))+"</b>");
            build.append(": ");
            build.append(ris.puntoMgrs);
        }
		
		// = Faccio anche un report CSV =
		StringBuilder reportCsvBuilder = new StringBuilder();
    	DecimalFormat csvDec = new DecimalFormat("#####0.00");
		
		reportCsvBuilder.append ("Data");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Tipo_Ingresso");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Descrizione");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Latitudine");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Longitudine");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Fuso_Gauss");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Nord_Gauss");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Est_Gauss");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Origine_Cassini");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("X_Cassini");
		reportCsvBuilder.append (',');
		reportCsvBuilder.append ("Y_Cassini");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("N_UTM");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("E_UTM");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("ZONE_UTM");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("ELLPS_UTM");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("MGRS");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("Latitudine ED50");
        reportCsvBuilder.append (',');
        reportCsvBuilder.append ("Longitudine ED50");
        reportCsvBuilder.append ("\n");
			
		for(RisultatoConversione ris : risultati)
		{
			reportCsvBuilder.append (StringUtils.toSafeCsv(dateFormat.format(new Date(ris.time_last))));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(ris.tipo_ingresso));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (ris.descrizione_punto);
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.lat)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.longi)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(ResUtils.fuso2string(this, ris.fuso)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.nord)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.est)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(ris.idOrigineCassini));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.x)));
			reportCsvBuilder.append (',');
			reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.y)));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.utm_nord)));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.utm_est)));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(""+ris.zonaUtm));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(ris.utm_datum));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(ris.puntoMgrs));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.lat_ed50)));
            reportCsvBuilder.append (',');
            reportCsvBuilder.append (StringUtils.toSafeCsv(csvDec.format(ris.longi_ed50)));
            reportCsvBuilder.append ("\n");
		}
		csvString = reportCsvBuilder.toString();
		
		// = Visualizzo tutto =
		// Qua c'e' un buco di Android
		// http://code.google.com/p/android/issues/detail?id=1733#c23
		//registroHtmlView.loadData(build.toString(), "text/html", "utf-8");
		htmlString =  build.toString();
		registroHtmlView.loadDataWithBaseURL("fake://not/needed", htmlString, "text/html", "utf-8", "");
	}
	
	private void pulisciRegistro()
	{
		new AlertDialog.Builder(this)
			.setTitle(getString(R.string.app_name))
			.setMessage(getString(R.string.msg_pulisci_registro))
			.setNegativeButton(getString(R.string.no), null)
			.setPositiveButton(getString(R.string.si),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							IOriginiStorage db = new OriginiCassiniManager(ReportRegistroActivity.this);
							
							try
							{
								db.clearLogConversioni();
							}
							finally
							{
								db.close();
							}
							
							refresh();
						}
					}		
				)
			.show();
	}

	private void condividiRegistro()
	{
		File reportPath = new File(this.getFilesDir(), "reports");
		File newFile = new File(reportPath, "report.html");
		reportPath.mkdirs();
		try {
			salvaFileRegistro(newFile);
		} catch(IOException ioe) {
			Toast
				.makeText(this, getString(R.string.msg_errore_scrittura_sd) + " " + ioe.toString(), Toast.LENGTH_LONG)
				.show();
			return;
		}
		Uri contentUri = FileProvider.getUriForFile(this, "it.interfree.leonardoce.fileprovider", newFile);
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "iConv report");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.setType(getContentResolver().getType(contentUri));
		//shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));

		ArrayList<Uri> listaAllegati = new ArrayList<Uri>();
		listaAllegati.add(contentUri);

		shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listaAllegati);

		// Ci sono alcune applicazioni alle quali non basta un accesso temporaneo
		// e che quindi hanno bisogno di questo:

		List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(shareIntent, 0);
		for (ResolveInfo resolveInfo : resInfoList) {
			String packageName = resolveInfo.activityInfo.packageName;
			grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}

		startActivity(Intent.createChooser(shareIntent,"Share"));		
	}
	
	private void salvaRegistroHtml()
	{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_scheda_sd_non_presente))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}
		
		File externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!externalStorage.exists())
		{
			externalStorage.mkdirs();
		}
		final File backupRegistro = new File(externalStorage, "iConv_registro.html");
		
		if (backupRegistro.exists())
		{
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_file_registro_esistente))
				.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						salvaFileRegistroPoiInformaUtente(backupRegistro);
						
					}
				})
				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(ReportRegistroActivity.this, 
								getString(R.string.msg_file_non_scritto_perche_esistente), 
								Toast.LENGTH_LONG);
					}
				})
				.show();
		}
		else
		{
			salvaFileRegistroPoiInformaUtente(backupRegistro);
		}
	}
	
	private void salvaFileRegistro(File backupRegistro) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(backupRegistro, false);
		try
		{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-8")));
			bw.write(htmlString);
			bw.close();
		}
		finally
		{
			fos.close();
		}
	}

	private void salvaFileRegistroPoiInformaUtente(File backupRegistro)
	{
		try
		{
			salvaFileRegistro(backupRegistro);
			
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_file_scritto))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
		}
		catch(IOException ioe)
		{
			Toast
				.makeText(this, getString(R.string.msg_errore_scrittura_sd) + " " + ioe.toString(), Toast.LENGTH_LONG)
				.show();
		}
	}
	
	private void salvaRegistroCsv()
	{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_scheda_sd_non_presente))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
			return;
		}
		
		File externalStorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
		if (!externalStorage.exists())
		{
			externalStorage.mkdirs();
		}
		final File backupRegistro = new File(externalStorage, "iConv_registro_csv.txt");
		
		if (backupRegistro.exists())
		{
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_file_registro_esistente))
				.setPositiveButton(getString(R.string.si), new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						salvaFileCsv(backupRegistro);
						
					}
				})
				.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(ReportRegistroActivity.this, 
								getString(R.string.msg_file_non_scritto_perche_esistente), 
								Toast.LENGTH_LONG);
					}
				})
				.show();
		}
		else
		{
			salvaFileCsv(backupRegistro);
		}
	}
	
	private void salvaFileCsv(File backupCsv)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(backupCsv, false);
			try
			{
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, Charset.forName("UTF-8")));
				bw.write(csvString);
				bw.close();
			}
			finally
			{
				fos.close();
			}
			
			new AlertDialog.Builder(this)
				.setTitle(getString(R.string.app_name))
				.setMessage(getString(R.string.msg_file_scritto))
				.setPositiveButton(getString(R.string.ok), null)
				.show();
		}
		catch(IOException ioe)
		{
			Toast
				.makeText(this, getString(R.string.msg_errore_scrittura_sd) + " " + ioe.toString(), Toast.LENGTH_LONG)
				.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.report_conversione_menu_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i;

		if (item.getItemId() == R.id.menu_share) {
			condividiRegistro();
			return true;
		} else if (item.getItemId() == R.id.menu_salva_html) {
			salvaRegistroHtml();
			return true;
		} else if (item.getItemId() == R.id.menu_salva_csv) {
			salvaRegistroCsv();
			return true;
		} else if (item.getItemId() == R.id.menu_cancella) {
			pulisciRegistro();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
}
