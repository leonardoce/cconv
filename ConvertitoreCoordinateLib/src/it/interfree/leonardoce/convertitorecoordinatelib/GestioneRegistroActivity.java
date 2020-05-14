package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.adapters.RegistroRisultatiAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class GestioneRegistroActivity extends AppCompatActivity
{
	private ListView lista;
	private RegistroRisultatiAdapter registroRisultati;
	private Button buttonGeneraHtml;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.gestione_registro);
		
		registroRisultati = new RegistroRisultatiAdapter(this);
		buttonGeneraHtml = (Button)findViewById(R.id.buttonGeneraHtml);
		lista = (ListView)findViewById(R.id.listaRegistro);
		lista.setAdapter(registroRisultati);
		
		buttonGeneraHtml.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				generaHtml();
			}
		});
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		registroRisultati.refresh();
	}
	
	private void generaHtml()
	{
		Intent i = new Intent(this, ReportRegistroActivity.class);
		startActivity(i);
	}
}
