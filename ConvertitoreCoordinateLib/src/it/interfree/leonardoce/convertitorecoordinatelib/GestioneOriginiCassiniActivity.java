package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.iconv.adapters.OriginiCassiniAdapter;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GestioneOriginiCassiniActivity extends AppCompatActivity
{
	private ListView listaOriginiCassini;
	private ImageButton btAggiungiCassini;
	private OriginiCassiniAdapter adapter;
    private TextView tvOrigineCorrente;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gestione_cassini);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		listaOriginiCassini = (ListView) findViewById(R.id.listaOriginiCassini);
		btAggiungiCassini = (ImageButton) findViewById(R.id.btAggiungiCassini);
        tvOrigineCorrente = (TextView) findViewById(R.id.tvOrigineCorrente);
		btAggiungiCassini.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				aggiungiCassini();
			}
		});
		
		adapter = new OriginiCassiniAdapter(this);
        adapter.setPostCambiataOrigine(new Runnable() {
            @Override
            public void run() {
                refreshOrigineCorrente();
            }
        });
		listaOriginiCassini.setAdapter(adapter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		adapter.refresh();
        refreshOrigineCorrente();
	}

	private void aggiungiCassini()
	{
		Intent i = new Intent(this, NuovaOrigineCassiniActivity.class);
		this.startActivity(i);
	}

    private void refreshOrigineCorrente() {
        IOriginiStorage db = new OriginiCassiniManager(this);
        this.tvOrigineCorrente.setText(db.getIdOrigineSelezionata());
        db.close();

    }
}
