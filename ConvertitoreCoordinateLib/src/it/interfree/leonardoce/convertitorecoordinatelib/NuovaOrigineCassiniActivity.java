package it.interfree.leonardoce.convertitorecoordinatelib;

import it.interfree.leonardoce.convertitorecoordinatelib.components.LatLongView;
import it.interfree.leonardoce.iconv.db.IOriginiStorage;
import it.interfree.leonardoce.iconv.db.OrigineCassini;
import it.interfree.leonardoce.iconv.db.OriginiCassiniManager;
import it.interfree.leonardoce.iconv.math.Punto3D;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NuovaOrigineCassiniActivity extends AppCompatActivity
{
	private Button btInserisciCassini;
	private LatLongView inputOrigineCassini;
	private EditText txtNomeNuovaCassini;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.nuova_origine_cassini);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		inputOrigineCassini = (LatLongView)findViewById(R.id.inputOrigineCassini);
		btInserisciCassini = (Button)findViewById(R.id.btInserisciCassini);
		btInserisciCassini.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onInserisciCassini();
			}
		});
		
		txtNomeNuovaCassini = (EditText)findViewById(R.id.txtNomeNuovaCassini);
	}
	
	private void onInserisciCassini()
	{
		Punto3D punto = inputOrigineCassini.getPunto();
		String nome = txtNomeNuovaCassini.getText().toString();
		
		if (nome.trim().length()==0)
		{
			new AlertDialog.Builder(this)
			.setMessage(getString(R.string.msg_nome_origine_non_valido))
			.setTitle(getString(R.string.app_name))
			.setPositiveButton(getString(R.string.ok), null)
			.show();		
			return;
			
		}
		
		IOriginiStorage storage = new OriginiCassiniManager(this);
		try
		{
			OrigineCassini origine = new OrigineCassini(nome, punto);
			storage.save(origine);
		} finally
		{
			storage.close();
		}
		
		finish();
	}
}
